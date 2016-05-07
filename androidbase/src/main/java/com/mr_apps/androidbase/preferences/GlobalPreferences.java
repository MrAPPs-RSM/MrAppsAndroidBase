package com.mr_apps.androidbase.preferences;

import android.content.Context;

/**
 *
 *
 * @author Denis Brandi
 */
public class GlobalPreferences extends AbstractBasePreferences {

    public static final String namePreferences = "GlobalPreferences";
    public static final String latitude = "latitude";
    public static final String longitude = "longitude";
    public static final String altitude = "altitude";
    public static final String id = "id";
    public static final String username = "username";
    public static final String password = "password";
    public static final String hashedPassword = "hashedPassword";

    public static void setId(Context context, int value) {
        setPreferences(context, namePreferences, id, value);
    }

    public static int getId(Context context) {
        return getPreferences(context, namePreferences).getInt(id, 0);
    }

    public static void setUsername(Context context, String value) {
        setPreferences(context, namePreferences, username, value);
    }

    public static void setPassword(Context context, String value) {
        setPreferences(context, namePreferences, password, value);
    }

    public static void setHashedPassword(Context context, String value) {
        setPreferences(context, namePreferences, hashedPassword, value);
    }

    public static String getUsername(Context context) {
        return getPreferences(context, namePreferences).getString(username, "");
    }

    public static String getPassword(Context context) {
        return getPreferences(context, namePreferences).getString(password, "");
    }

    public static String getHashedPassword(Context context) {
        return getPreferences(context, namePreferences).getString(hashedPassword, "");
    }

    public static void setLatitude(Context context, String value) {
        setPreferences(context, namePreferences, latitude, value);
    }

    public static void setLongitude(Context context, String value) {
        setPreferences(context, namePreferences, longitude, value);
    }

    public static void setAltitude(Context context, String value) {
        setPreferences(context, namePreferences, altitude, value);
    }

    public static double getLatitude(Context context) {
        return Double.valueOf(getPreferences(context, namePreferences).getString(latitude, "0"));
    }

    public static double getLongitude(Context context) {
        return Double.parseDouble(getPreferences(context, namePreferences).getString(longitude, "0"));
    }

    public static String getAltitude(Context context) {
        return getPreferences(context, namePreferences).getString(altitude, "0");
    }


}
