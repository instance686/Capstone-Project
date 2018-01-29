package com.reminders.location.locatoinreminder;

import com.reminders.location.locatoinreminder.database.AppDatabase;


/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;


    private DataRepository(final AppDatabase database) {
        mDatabase = database;

    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

}
