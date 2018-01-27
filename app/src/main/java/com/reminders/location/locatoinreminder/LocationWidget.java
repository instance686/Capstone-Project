package com.reminders.location.locatoinreminder;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.reminders.location.locatoinreminder.constants.ConstantVar;

/**
 * Implementation of App Widget functionality.
 */
public class LocationWidget extends AppWidgetProvider {

    public static int[] WIDGET_KEYS;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.location_widget);
        ComponentName watchWidget = new ComponentName(context, LocationWidget.class);

        Intent intent = new Intent(context, LocationWidget.class);
        intent.setAction(ConstantVar.WIDGET_BUTTON_CLICKED);
        intent.putExtra(ConstantVar.APPWIDGET_ID, appWidgetId);
        intent.putExtra(ConstantVar.WIDGET_IDS, LocationWidget.WIDGET_KEYS);

        AppWidgetManager man = AppWidgetManager.getInstance(context);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.location, pendingIntent);

        // Set up the collection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setRemoteAdapter(context, views);
        } else {
            setRemoteAdapterV11(context, views);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.list,
                new Intent(context, WidgetService.class));
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @SuppressWarnings("deprecation")
    private static void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {

        views.setRemoteAdapter(0, R.id.list,
                new Intent(context, WidgetService.class));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        LocationWidget.WIDGET_KEYS = appWidgetIds;
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ConstantVar.WIDGET_BUTTON_CLICKED.equalsIgnoreCase(intent.getAction())) {
            Log.v("FROMWIDGETCLICK", intent.getAction());
            this.onUpdate(context, AppWidgetManager.getInstance(context), intent.getIntArrayExtra(ConstantVar.WIDGET_IDS));
        }
    }
}

