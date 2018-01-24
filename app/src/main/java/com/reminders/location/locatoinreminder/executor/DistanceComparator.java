package com.reminders.location.locatoinreminder.executor;

import android.location.Location;

import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;

import java.util.Comparator;

/**
 * Created by ayush on 24/1/18.
 */

public class DistanceComparator implements Comparator<ChatCards_Entity> {
    Location current=new Location("");
    Location d1=new Location("");
    Location d2=new Location("");
    public DistanceComparator(Location location){
        current=location;
    }
    @Override
    public int compare(ChatCards_Entity o1, ChatCards_Entity o2) {
        String[] loc1=o1.getLocation().split(" ");
        String[] loc2=o2.getLocation().split(" ");
        try {
            d1.setLatitude(Double.parseDouble(loc1[0]));
            d1.setLongitude(Double.parseDouble(loc1[1]));
            d2.setLatitude(Double.parseDouble(loc2[0]));
            d2.setLongitude(Double.parseDouble(loc2[1]));
            float distance1 = current.distanceTo(d1);
            float distance2 = current.distanceTo(d2);
            if (distance1 == distance2)
                return 0;
            else if (distance1 > distance2)
                return 1;
            else
                return -1;
        }
        catch (Exception e){
            return 0;
        }

    }
}
