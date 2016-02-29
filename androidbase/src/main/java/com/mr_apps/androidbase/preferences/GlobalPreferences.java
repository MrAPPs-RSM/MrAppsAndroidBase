package com.mr_apps.androidbase.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.mr_apps.androidbase.utils.Utils;

/**
 * Created by denis on 09/02/16.
 */
public class GlobalPreferences extends AbstractBasePreferences{

    private static final String namePreferences="GlobalPreferences";
    private static final String latitude="latitude";
    private static final String longitude="longitude";
    private static final String altitude="altitude";
    private static final String username="username";
    private static final String password="password";
    private static final String hashedPassword="hashedPassword";

    public static void setUsername(Context context, String value)
    {
        setPreferences(context, namePreferences, username, value);
    }

    public static void setPassword(Context context, String value)
    {
        setPreferences(context, namePreferences, password, value);
    }

    public static void setHashedPassword(Context context, String value)
    {
        setPreferences(context, namePreferences, hashedPassword, value);
    }

    public static String getUsername(Context context)
    {
        return getPreferences(context, namePreferences).getString(username, "");
    }

    public static String getPassword(Context context)
    {
        return getPreferences(context, namePreferences).getString(password, "");
    }

    public static String getHashedPassword(Context context)
    {
        return getPreferences(context, namePreferences).getString(hashedPassword, "");
    }

    public static void setLatitude(Context context, String value)
    {
        setPreferences(context, namePreferences, latitude, value);
    }

    public static void setLongitude(Context context, String value)
    {
        setPreferences(context, namePreferences, longitude, value);
    }

    public static void setAltitude(Context context, String value)
    {
        setPreferences(context, namePreferences, altitude, value);
    }

    public static double getLatitude(Context context)
    {
        return Double.valueOf(getPreferences(context, namePreferences).getString(latitude, "0"));
    }

    public static double getLongitude(Context context)
    {
        return Double.parseDouble(getPreferences(context, namePreferences).getString(longitude, "0"));
    }

    public static String getAltitude(Context context)
    {
        return getPreferences(context, namePreferences).getString(altitude, "0");
    }


}
