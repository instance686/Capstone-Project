package com.reminders.location.locatoinreminder.boadcastreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import com.reminders.location.locatoinreminder.service.LocationService;

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
            Intent service = new Intent(context, LocationService.class);
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            isGpsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isGpsOn && isNetworkEnabled) {
                Log.v("FromReciever", "Start Service");
                context.startService(service);
            } else {
                Log.v("FromReciever", "Stop Service");
                context.stopService(service);
            }
        }
    }


}




