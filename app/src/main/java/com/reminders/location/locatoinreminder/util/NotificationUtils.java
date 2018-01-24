package com.reminders.location.locatoinreminder.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;
import com.reminders.location.locatoinreminder.view.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 25/9/17.
 */

public class NotificationUtils extends ContextWrapper {

    private NotificationManager mManager;
    public static final String ANDROID_CHANNEL_ID = "Reminders";
    public static final String ANDROID_CHANNEL_NAME = "Reminder Recieved";

    public static final String ANDROID_CHANNEL_ID1 = "Reminder Trigger";
    public static final String ANDROID_CHANNEL_NAME1 = "Reminder Triggered";

    public NotificationUtils(Context base, int choice) {
        super(base);
        if(choice==0){
            createChannel1();
        }else {
            createChannel2();
        }

    }

    public void createChannel1() {

        // create android channel
        NotificationChannel androidChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                    ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            androidChannel.enableLights(true);
            androidChannel.enableVibration(true);
            androidChannel.setLightColor(Color.RED);
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(androidChannel);
        }

    }

    public void createChannel2() {

        // create android channel
        NotificationChannel androidChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID1,
                    ANDROID_CHANNEL_NAME1, NotificationManager.IMPORTANCE_HIGH);
            androidChannel.enableLights(true);
            androidChannel.enableVibration(true);
            androidChannel.setLightColor(Color.RED);
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(androidChannel);
        }

    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(Bitmap largeIcon,List<ChatCards_Entity> allDetails) {
        Intent i = new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putParcelableArrayListExtra("TEST", (ArrayList<? extends Parcelable>) allDetails);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 2, i, PendingIntent.FLAG_ONE_SHOT);
        return new Notification.Builder(getApplicationContext())
                .setContentIntent(resultPendingIntent)
                .setContentTitle("You have reminders close by")
                .setContentText("Touch to view them")
                //.setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setChannelId(ANDROID_CHANNEL_ID);
    }


}
