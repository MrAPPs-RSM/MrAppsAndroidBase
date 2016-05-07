package com.mr_apps.androidbase.preferences;

import android.content.Context;

/**
 * Class that handle the SharedPreferences for the WSSE Security
 *
 * @author Denis Brandi
 */
public class SecurityPreferences extends AbstractBasePreferences {

    public static final String namePreferences = "SecurityPreferences";

    public static final String user = "user";
    public static final String token = "token";
    public static final String secret = "secret";
    public static final String header = "header";
    public static final String header_date = "header_date";
    public static final String authorization = "authorization";

    /**
     *
     * Get the user for the WSSE Security.
     *
     * @param context
     * @return
     */
    public static String getUser(Context context) {
        return getPreferences(context, namePreferences).getString(user, "");
    }

    /**
     *
     * Set the user for the WSSE Security.
     *
     * @param context
     * @param value
     */
    public static void setUser(Context context, String value) {
        setPreferences(context, namePreferences, user, value);
    }

    /**
     *
     * Get the timestamp of the last update of the Header for the WSSE Security.
     *
     * @param context
     * @return
     */
    public static long getHeader_date(Context context) {
        return getPreferences(context, namePreferences).getLong(header_date, 0);
    }

    /**
     *
     * Set the timestamp of the last update of the Header for the WSSE Security.
     *
     * @param context
     * @param value
     */
    public static void setHeader_date(Context context, long value) {
        setPreferences(context, namePreferences, header_date, value);
    }

    /**
     *
     * Get the password for the WSSE Security.
     *
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        return getPreferences(context, namePreferences).getString(token, "");
    }

    /**
     *
     * Set the password for the WSSE Security.
     *
     * @param context
     * @param value
     */
    public static void setToken(Context context, String value) {
        setPreferences(context, namePreferences, token, value);
    }


    /**
     *
     * Get the additional parameter for the WSSE Security.
     *
     * @param context
     * @return
     */
    public static String getSecret(Context context) {
        return getPreferences(context, namePreferences).getString(secret, "");
    }

    /**
     *
     * Set the additional parameter for the WSSE Security.
     *
     * @param context
     * @param value
     */
    public static void setSecret(Context context, String value) {
        setPreferences(context, namePreferences, secret, value);
    }

    /**
     *
     * Get the Header for the WSSE Security.
     *
     * @param context
     * @return
     */
    public static String getHeader(Context context) {

        return getPreferences(context, namePreferences).getString(header, "");
    }

    /**
     *
     * Set the Header for the WSSE Security.
     *
     * @param context
     * @param value
     */
    public static void setHeader(Context context, String value) {
        setPreferences(context, namePreferences, header, value);
    }

    /**
     *
     * Get the Authorization for the WSSE Security.
     *
     * @param context
     * @return
     */
    public static String getAuthorization(Context context) {
        return getPreferences(context, namePreferences).getString(authorization, "");
    }

    /**
     *
     * Set the Authorization for the WSSE Security.
     *
     * @param context
     * @param value
     */
    public static void setAuthorization(Context context, String value) {
        setPreferences(context, namePreferences, authorization, value);
    }

}
