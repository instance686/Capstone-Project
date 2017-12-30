package com.reminders.location.locatoinreminder;

import android.support.multidex.MultiDexApplication;

import com.reminders.location.locatoinreminder.database.AppDatabase;

/**
 * Created by ayush on 25/12/17.
 */

public class MyApplication extends MultiDexApplication {
    private String UID;




    @Override
    public void onCreate() {
        super.onCreate();
        getDatabase();
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }


    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}

