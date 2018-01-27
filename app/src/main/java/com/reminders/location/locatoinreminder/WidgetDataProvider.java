package com.reminders.location.locatoinreminder;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.pojo.ListData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 26/1/18.
 */

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "WidgetDataProvider";

    List<ListData> mCollection = new ArrayList<>();
    Context mContext = null;
    String data = "";
    private int appWidgetId;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        data = intent.getStringExtra(ConstantVar.REMINDER_DATA);
        initData(data);
    }

    @Override
    public void onCreate() {
        initData(data);
    }

    @Override
    public void onDataSetChanged() {
        initData(data);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mCollection != null)
            return mCollection.size();
        else
            return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_rows);
        view.setTextViewText(R.id.title, mCollection.get(position).getTitle());
        view.setTextViewText(R.id.note, mCollection.get(position).getNote());
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData(String s) {
        mCollection = new Gson().fromJson(s, new TypeToken<List<ListData>>() {
        }.getType());
    }
}
