package com.reminders.location.locatoinreminder.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ayush on 25/12/17.
 */

public class Utils {


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

    public String getFullNumber(String number){
        if(number.contains("+91"))
            return number;
        else
            return "+91"+number;
    }
    public static boolean isConnectedToNetwork(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
