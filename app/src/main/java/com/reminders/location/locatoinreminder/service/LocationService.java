package com.reminders.location.locatoinreminder.service;


import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;
import com.reminders.location.locatoinreminder.executor.ShoutsListUpdate;
import com.reminders.location.locatoinreminder.singleton.SharedPreferenceSingleton;
import com.reminders.location.locatoinreminder.util.NotificationUtils;
import com.reminders.location.locatoinreminder.view.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 21/1/18.
 */

public class LocationService extends Service implements LocationListener {
    private Context context;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    int flag = 0;
    Location location;
    Notification notification;
    Location mylocation = new Location("");
    Location dest_location = new Location("");
    NotificationManager notifier;
    public double latitude;
    double longitude;
    LocationManager locationManager;
    AppDatabase databaseReference;
    List<ChatCards_Entity> allDetails=new ArrayList<>();
    Intent broadcastIntent = new Intent();
    int locationCounter=1;
    ShoutsListUpdate shoutsListUpdate;
        private SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();



    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;// 40 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 5;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("FromService", "on create");
        databaseReference=getMyapp().getDatabase();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(databaseReference==null){
            databaseReference=getMyapp().getDatabase();
        }

        databaseReference=getMyapp().getDatabase();


        dest_location.setLatitude(28.4401501);
        dest_location.setLatitude(77.065132);
        mylocation = getLocation(this);

        Log.v("FromService","onStartCommand");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("FromService", "Service Destroyed");
        flag = 0;
        stopUsingGPS();
        stopSelf();
    }


    public Location getLocation(Context context) {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isGPSEnabled && isNetworkEnabled) {
                this.canGetLocation = true;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return null;
                }
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if(locationManager!=null){
                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location!=null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        //Log.v("Distance",location.distanceTo(dest_location)+"");
                        Log.v("FromService","onLocationChanged");


                    }
                }
            }
        }
        catch (Exception e){

        }

        return location;
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(LocationService.this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        getList();
        List<ChatCards_Entity> chatCardsEntities=new ArrayList<>();
        if(!allDetails.isEmpty()) {
            if (!chatCardsEntities.isEmpty())
                chatCardsEntities.clear();
            for (ChatCards_Entity ce : allDetails) {
                String[] loc = ce.getLocation().split(" ");
                dest_location.setLatitude(Double.parseDouble(loc[0]));
                dest_location.setLongitude(Double.parseDouble(loc[1]));
                float distance = location.distanceTo(dest_location);
                Log.v("FromServiceDis", distance + "");
                //if(distance<=2500)//TODO uncomment this
                chatCardsEntities.add(ce);
            }
        }
        if(appInForeGround()){
            if(!chatCardsEntities.isEmpty()) {
                broadcastIntent.setAction(ConstantVar.mBroadcastArrayListAction);
                broadcastIntent.putExtra(ConstantVar.CURRENT_LATITUDE,location.getLatitude());
                broadcastIntent.putExtra(ConstantVar.CURRENT_LONGITUDE,location.getLongitude());
                broadcastIntent.putParcelableArrayListExtra("TEST", (ArrayList<? extends Parcelable>) chatCardsEntities);
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
            }
        }
        else
            if(!chatCardsEntities.isEmpty())
                sendNotification();



        Log.v("FromService","onLocationChanged");

    }
    public MyApplication getMyapp() {
        return (MyApplication) getApplicationContext();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.v("FromService",provider);

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.v("FromService",provider);
    }

    public void getList(){
        allDetails=databaseReference.cardDoa().getCardsForLocation();
    }


    public boolean appInForeGround(){
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

        ComponentName componentInfo = taskInfo.get(0).topActivity;
        if (componentInfo.getPackageName().equalsIgnoreCase("com.reminders.location.locatoinreminder")) {
            return true;
        } else {
            return false;
        }
    }

    private void sendNotification() {
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationUtils mNotificationUtils = new NotificationUtils(this,0);
            Notification.Builder nb = mNotificationUtils.
                    getChannelNotification(largeIcon,allDetails);
            mNotificationUtils.getManager().notify(0, nb.build());
        } else {

            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putParcelableArrayListExtra("TEST", (ArrayList<? extends Parcelable>) allDetails);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 2, i, PendingIntent.FLAG_ONE_SHOT);
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setPriority(Notification.PRIORITY_HIGH);
            mBuilder.setGroupSummary(true);
            mBuilder.setAutoCancel(true);
            mBuilder.setContentTitle("You have reminders near by");
            mBuilder.setContentText("Touch to view them");
            mBuilder.setOngoing(false);
            mBuilder.setSound(defaultSoundUri);
            mBuilder.setSmallIcon(R.drawable.ic_notification);
            mBuilder.setLargeIcon(largeIcon);
            mBuilder.setVibrate(new long[]{100, 100});
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (mNotificationManager != null) {
                mNotificationManager.notify(0, mBuilder.build());
            }
        }
    }
}
