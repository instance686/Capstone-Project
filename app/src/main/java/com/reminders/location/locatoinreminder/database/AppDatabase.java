package com.reminders.location.locatoinreminder.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.reminders.location.locatoinreminder.database.dao.cardDoa;
import com.reminders.location.locatoinreminder.database.dao.contactDoa;
import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;
import com.reminders.location.locatoinreminder.database.entity.Contact_Entity;

/**
 * Created by ayush on 25/12/17.
 */
@Database(entities = {Contact_Entity.class, ChatCards_Entity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract contactDoa contactDao();
    public abstract cardDoa cardDoa();

    private static AppDatabase sInstance;
    public static final String DATABASE_NAME="My_DB";

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }


    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext)
                                             {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .build();
    }


}
