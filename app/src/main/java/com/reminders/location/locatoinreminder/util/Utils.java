package com.reminders.location.locatoinreminder.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ayush on 25/12/17.
 */

public class Utils {


    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public String getInitial(String s) {
        int spaceIndex = s.indexOf(' ');
        try {
            if (spaceIndex != -1) {
                return "" + s.charAt(0) + s.charAt(spaceIndex + 1);
            }
        } catch (Exception e) {
        }

        return "" + s.charAt(0);
    }

    public String getFullNumber(String number) {
        if (number.contains("+91"))
            return number;
        else
            return "+91" + number;
    }

    public String getCoordinates(String location) {
        String[] latlang = location.split(" ");
        String loc = "";
        /*if(latlang[0].length()>5) {
            loc = loc + latlang[0].substring(0, 4) + ",";
        }
        else {
            loc = loc+latlang[0] + ",";
        }
        if(latlang[1].length()>5) {
            loc = loc + latlang[1].substring(0, 4);
        }else
        {
            loc=loc+latlang[1];
        }*/
        loc = loc + latlang[2];

        return loc;
    }
}
