package com.reminders.location.locatoinreminder;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 26/1/18.
 */

public class WidgetDataProvider  implements RemoteViewsService.RemoteViewsFactory {

    Context context;
    Intent intent;
    List<ChatCards_Entity> list=new ArrayList<>();

    private void init(){
        list.clear();
    }
    public WidgetDataProvider(Context context,Intent intent) {
        this.context=context;
        this.intent=intent;
    }

    @Override
    public void onCreate() {
        init();
    }

    @Override
    public void onDataSetChanged() {
        init();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
