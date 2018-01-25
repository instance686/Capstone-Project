package com.reminders.location.locatoinreminder;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by ayush on 26/1/18.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this, intent);
    }
}
