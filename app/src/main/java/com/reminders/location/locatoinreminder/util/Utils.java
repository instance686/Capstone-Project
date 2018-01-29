package com.reminders.location.locatoinreminder.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.singleton.SharedPreferenceSingleton;

/**
 * Created by ayush on 25/12/17.
 */

public class Utils {
    private SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();


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

    public String getFullNumber(Context context,String number) {
        String countryCode=sharedPreferenceSingleton.getSavedString(context, ConstantVar.COUNTRY_CODE);
        if (number.contains(countryCode))
            return number;
        else
            return countryCode + number;
    }

    public String getCoordinates(String location) {
        String[] latlang = location.split(" ");
        String loc = "";
        loc = loc + latlang[2];

        return loc;
    }
}
