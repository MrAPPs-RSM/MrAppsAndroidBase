package com.mr_apps.androidbase.preferences;

import android.content.Context;

/**
 * Class that manages general shared preferences
 *
 * @author Denis Brandi
 */
public class GlobalPreferences extends AbstractBasePreferences {

    protected static final String namePreferences = "GlobalPreferences";
    private static final String latitude = "latitude";
    private static final String longitude = "longitude";
    private static final String altitude = "altitude";
    private static final String id = "id";
    private static final String username = "username";
    private static final String password = "password";
    private static final String hashedPassword = "hashedPassword";

    /**
     * Sets the value for the id key
     *
     * @param context the context
     * @param value   the value for the id key
     */
    public static void setId(Context context, int value) {
        setPreferences(context, namePreferences, id, value);
    }

    /**
     * Gets the value of the id key
     *
     * @param context the context
     * @return the id value
     */
    public static int getId(Context context) {
        return getPreferences(context, namePreferences).getInt(id, 0);
    }

    /**
     * Sets the value for the username key
     *
     * @param context the context
     * @param value   the value for the username key
     */
    public static void setUsername(Context context, String value) {
        setPreferences(context, namePreferences, username, value);
    }

    /**
     * Gets the value of the username key
     *
     * @param context the context
     * @return the username value
     */
    public static String getUsername(Context context) {
        return getPreferences(context, namePreferences).getString(username, "");
    }

    /**
     * Sets the value for the password key
     *
     * @param context the context
     * @param value   the value for the password key
     */
    public static void setPassword(Context context, String value) {
        setPreferences(context, namePreferences, password, value);
    }

    /**
     * Gets the value of the password key
     *
     * @param context the context
     * @return the password value
     */
    public static String getPassword(Context context) {
        return getPreferences(context, namePreferences).getString(password, "");
    }

    /**
     * Sets the value for the hashed password key
     *
     * @param context the context
     * @param value   the value for the hashed password key
     */
    public static void setHashedPassword(Context context, String value) {
        setPreferences(context, namePreferences, hashedPassword, value);
    }

    /**
     * Gets the value of the hashed password key
     *
     * @param context the context
     * @return the hashed password value
     */
    public static String getHashedPassword(Context context) {
        return getPreferences(context, namePreferences).getString(hashedPassword, "");
    }

    /**
     * Sets the value for the latitude key
     *
     * @param context the context
     * @param value   the value for the latitude key
     */
    public static void setLatitude(Context context, String value) {
        setPreferences(context, namePreferences, latitude, value);
    }

    /**
     * Gets the value of the latitude key
     *
     * @param context the context
     * @return the latitude value
     */
    public static double getLatitude(Context context) {
        return Double.valueOf(getPreferences(context, namePreferences).getString(latitude, "0"));
    }

    /**
     * Sets the value for the longitude key
     *
     * @param context the context
     * @param value   the value for the longitude key
     */
    public static void setLongitude(Context context, String value) {
        setPreferences(context, namePreferences, longitude, value);
    }

    /**
     * Gets the value of the longitude key
     *
     * @param context the context
     * @return the longitude value
     */
    public static double getLongitude(Context context) {
        return Double.parseDouble(getPreferences(context, namePreferences).getString(longitude, "0"));
    }

    /**
     * Sets the value for the altitude key
     *
     * @param context the context
     * @param value   the value for the altitude key
     */
    public static void setAltitude(Context context, String value) {
        setPreferences(context, namePreferences, altitude, value);
    }

    /**
     * Gets the value of the altitude key
     *
     * @param context the context
     * @return the altitude value
     */
    public static String getAltitude(Context context) {
        return getPreferences(context, namePreferences).getString(altitude, "0");
    }


}
