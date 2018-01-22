package com.reminders.location.locatoinreminder.boadCastReciever;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.reminders.location.locatoinreminder.service.LocationService;
import com.reminders.location.locatoinreminder.singleton.ToastMessage;

/**
 * Created by ayush on 21/1/18.
 */

public class GPSLocationReciever extends BroadcastReceiver {

    boolean isGpsOn = false;
    boolean isNetworkEnabled = false;
    LocationManager locationManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            Intent service=new Intent(context, LocationService.class);
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            isGpsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isGpsOn && isNetworkEnabled) {
                Log.v("FromReciever", "Start Service");
                context.startService(service);
            }
            else{
                Log.v("FromReciever", "Stop Service");
                context.stopService(service);
            }
        }


    }



}
