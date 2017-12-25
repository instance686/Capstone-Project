package com.reminders.location.locatoinreminder;

import android.app.Application;

import com.reminders.location.locatoinreminder.database.AppDatabase;

/**
 * Created by ayush on 25/12/17.
 */

public class MyApplication extends Application {

    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}

