package com.reminders.location.locatoinreminder;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.reminders.location.locatoinreminder.database.AppDatabase;

public class MyContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.reminders.location.locatoinreminder.contentprovider";
    public static final String SCHEME = "content://";
    public static final String CHATCARDS_ENTITY = SCHEME + AUTHORITY + "/CHATCARDS_ENTITY";
    public static final Uri URI_CHATENTITY = Uri.parse(CHATCARDS_ENTITY);
    public static final String CHAT_BASE = CHATCARDS_ENTITY + "/";
    AppDatabase appDatabase;

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        appDatabase = getMyapp().getDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.

        Cursor result = null;
        if (URI_CHATENTITY.equals(uri)) {
            appDatabase = getMyapp().getDatabase();
            result = getAllReminders();
        } else if (uri.toString().startsWith(CHAT_BASE)) {

        } else
            throw new UnsupportedOperationException("Not yet implemented");
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    synchronized Cursor getAllReminders() {
        return appDatabase.cardDoa().getAllCards();
    }

    public MyApplication getMyapp() {
        return (MyApplication) getContext();
    }

}
