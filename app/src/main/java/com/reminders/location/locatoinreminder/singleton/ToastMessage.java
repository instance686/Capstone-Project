package com.reminders.location.locatoinreminder.singleton;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ayush on 15/1/18.
 */

public class ToastMessage {

    public static void showMessageShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showMessageLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
