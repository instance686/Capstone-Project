package com.reminders.location.locatoinreminder.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.database.dao.CardDoa;
import com.reminders.location.locatoinreminder.database.dao.ContactDoa;
import com.reminders.location.locatoinreminder.database.dao.ReminderContactDoa;
import com.reminders.location.locatoinreminder.database.entity.ChatCardsEntity;
import com.reminders.location.locatoinreminder.database.entity.ContactEntity;
import com.reminders.location.locatoinreminder.database.entity.ReminderContact;

/**
 * Created by ayush on 25/12/17.
 */
@Database(entities = {ContactEntity.class, ChatCardsEntity.class, ReminderContact.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = ConstantVar.DBNAME;
    private static AppDatabase sInstance;

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
    private static AppDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract ContactDoa contactDao();

    public abstract CardDoa cardDoa();

    public abstract ReminderContactDoa reminderContactDoa();


}
