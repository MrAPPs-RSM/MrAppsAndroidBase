package com.mr_apps.androidbase.utils;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

/**
 * Created by mattia on 27/10/2015.
 *
 * @author Mattia Ruggiero
 */
public class Utils {

    public static float distanceInMeters(double latitude1, double longitude1, double latitude2, double longitude2)
    {
        Location location1=new Location("");
        location1.setLatitude(latitude1);
        location1.setLongitude(longitude1);

        Location location2=new Location("");
        location2.setLatitude(latitude2);
        location2.setLongitude(longitude2);

        return location1.distanceTo(location2);
    }

    /**
     * Metodo utile per controllare rapidamente se una stringa è vuota o null
     *
     * @param string la stringa da controllare
     * @return "true" se la stringa è vuota o nulla, "false" altrimenti
     */
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static boolean containsIgnoreCase(String s1, String s2) {
        return s1.toLowerCase().contains(s2.toLowerCase());
    }

    public static String subString(String string, int end) {
        if (end > string.length())
            return string;
        else
            return string.substring(0, end);
    }

    public static boolean isNullOrEmptyNoNull(String string) {
        return string == null || string.trim().isEmpty() || string.equalsIgnoreCase("NULL");
    }

    public static int dpToPx(float dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    //metodo che verifica se il dispositivo è connesso alla rete
//ritorna un boolean, true connesso, false non connesso
    public static boolean isOnline(Context context) {
        //inizializzo il connectivity manager
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //prendo informazioni sulle connessioni attive
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //se ho informazioni e  sono connesso o in attesa di connessione
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        //else di default, non sono connesso
        //Toast.makeText(context.getApplicationContext(), context.getString(R.string.offline), Toast.LENGTH_LONG).show();
        return false;
    }
}
