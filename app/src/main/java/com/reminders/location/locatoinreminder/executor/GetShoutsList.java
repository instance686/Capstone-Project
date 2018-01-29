package com.reminders.location.locatoinreminder.executor;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ChatCardsEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 23/1/18.
 */

public class GetShoutsList extends AsyncTask<Void, Void, Void> {

    Context context;
    AppDatabase appDatabase;
    Location current = new Location("");
    Location destination = new Location("");
    ;
    List<ChatCardsEntity> chatCardsEntities;
    LocationUpdateList locationUpdateList;

    public GetShoutsList(Context context, AppDatabase appDatabase, Location location, LocationUpdateList locationUpdateList) {
        this.context = context;
        this.appDatabase = appDatabase;
        this.current = location;
        destination = new Location("");
        chatCardsEntities = new ArrayList<>();
        this.locationUpdateList = locationUpdateList;
        chatCardsEntities = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<ChatCardsEntity> cards = appDatabase.cardDoa().getCardsForLocation();
        if (!cards.isEmpty()) {
            for (ChatCardsEntity ce : cards) {
                String[] loc = ce.getLocation().split(" ");
                destination.setLatitude(Double.parseDouble(loc[0]));
                destination.setLongitude(Double.parseDouble(loc[1]));
                float distance = current.distanceTo(destination);
                if (distance <= 1000)
                    chatCardsEntities.add(ce);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        locationUpdateList.locationList(chatCardsEntities);
    }
}
