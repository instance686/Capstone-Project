package com.reminders.location.locatoinreminder;

import android.*;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;
import com.reminders.location.locatoinreminder.pojo.ContactFetch;
import com.reminders.location.locatoinreminder.pojo.ListData;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 26/1/18.
 */

public class WidgetService extends RemoteViewsService implements LocationListener {
    Location currentLocation=new Location("");
    Location destination=new Location("");
    LocationManager locationManager;
    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        Cursor cursor=getApplicationContext().getContentResolver().query(MyContentProvider.URI_CHATENTITY,null,null,null,null);
        List<ListData> chatCards_entityList=getAllCardsList(cursor);

        if(!chatCards_entityList.isEmpty()) {
            List<ListData> nearByCard = getNearByCards(chatCards_entityList);
            if(!nearByCard.isEmpty()) {
                 intent.putExtra(ConstantVar.REMINDER_DATA,getStringFromList(nearByCard));
            }
            else
                intent.putExtra(ConstantVar.REMINDER_DATA,"");
        }
        else
            intent.putExtra(ConstantVar.REMINDER_DATA,"");

        Log.v("FROMWIDGET","onGetViewFactory");
        return new WidgetDataProvider(this.getApplicationContext(), intent);
    }

    String getStringFromList(List<ListData> ce){
        Gson gson=new Gson();
        String s=gson.toJson(ce, new TypeToken<List<ListData>>() {}.getType());
        return s;
    }

    List<ListData> getNearByCards(List<ListData> listData){
        List<ListData> nearby=new ArrayList<>();
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnabled && isNetworkEnabled) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    0,
                    0, this);
                currentLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(currentLocation!=null){
                    for(ListData le:listData){
                        String[] loc=le.getLocation().split(" ");
                        destination.setLatitude(Double.parseDouble(loc[0]));
                        destination.setLongitude(Double.parseDouble(loc[1]));
                        float distance = currentLocation.distanceTo(destination);
                        if(distance<=1000)
                            nearby.add(le);
                        Log.v("FROMWIDGET",le.getNote());
                    }
                }

        }
        return nearby;
    }

    List<ListData> getAllCardsList(Cursor cursor){
        List<ListData> allList=new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                int cardId=cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0)));
                String cardTitle=cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));
                String note=cursor.getString(cursor.getColumnIndex("notes_data"));
                String location=cursor.getString(cursor.getColumnIndex("location"));
                Log.v("FROMWIDGETLATLONG",location);
                allList.add(new ListData(cardId,cardTitle,note,"",location));
            }
            while (cursor.moveToNext());
        }
        return allList;
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
