package com.mr_apps.androidbase.preferences;

import android.content.Context;

/**
 * Created by denis on 03/02/16
 */
public class SecurityPreferences extends AbstractBasePreferences {

    public static final String namePreferences = "SecurityPreferences";

    public static final String user = "user";
    public static final String token = "token";
    public static final String secret = "secret";
    public static final String header = "header";
    public static final String header_date = "header_date";
    public static final String authorization = "authorization";

    public static String getUser(Context context) {
        return getPreferences(context, namePreferences).getString(user, "");
    }

    public static void setUser(Context context, String value) {
        setPreferences(context, namePreferences, user, value);
    }

    public static long getHeader_date(Context context) {
        return getPreferences(context, namePreferences).getLong(header_date, 0);
    }

    public static void setHeader_date(Context context, long value) {
        setPreferences(context, namePreferences, header_date, value);
    }

    public static String getToken(Context context) {
        return getPreferences(context, namePreferences).getString(token, "");
    }

    public static void setToken(Context context, String value) {
        setPreferences(context, namePreferences, token, value);
    }

    public static String getSecret(Context context) {
        return getPreferences(context, namePreferences).getString(secret, "");
    }

    public static void setSecret(Context context, String value) {
        setPreferences(context, namePreferences, secret, value);
    }

    public static String getHeader(Context context) {

        return getPreferences(context, namePreferences).getString(header, "");
    }

    public static void setHeader(Context context, String value) {
        setPreferences(context, namePreferences, header, value);
    }

    public static String getAuthorization(Context context) {
        return getPreferences(context, namePreferences).getString(authorization, "");
    }

    public static void setAuthorization(Context context, String value) {
        setPreferences(context, namePreferences, authorization, value);
    }

}
