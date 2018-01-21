package com.reminders.location.locatoinreminder.service;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.reminders.location.locatoinreminder.MyApplication;
import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;

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


    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 40;// 40 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10;


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
                        Log.v("Distance",location.distanceTo(dest_location)+"");

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
        for(ChatCards_Entity ce:allDetails){
            String[] loc=ce.getLocation().split(" ");
            dest_location.setLatitude(Double.parseDouble(loc[0]));
            dest_location.setLongitude(Double.parseDouble(loc[1]));
            float distance=location.distanceTo(dest_location);
            if(distance<=1000){
                //TODO-Notify User using notification
            }
        }

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
}
