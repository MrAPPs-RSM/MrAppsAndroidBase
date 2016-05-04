package com.mr_apps.androidbase.webservice;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by denis on 02/02/16
 */
public abstract class WebServiceUtils {

    private static final String TAG = "WebServiceUtils";

    public static JsonObject putJsonObject(JsonElement object) {
        if (object == null || object.isJsonNull() || !object.isJsonObject())
            return null;
        else
            return object.getAsJsonObject();
    }

    public static String putJsonString(JsonElement object) {
        try {

            if (object != null && !object.isJsonNull())
                return object.getAsString();
            else
                return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static int putJsonInt(JsonElement object) {
        if (object != null && !object.isJsonNull() && !object.getAsString().equals(""))
            return object.getAsInt();
        else
            return 0;

    }

    public static int putJsonIntRounded(JsonElement object) {
        if (object != null && !object.isJsonNull() && !object.getAsString().equals(""))
            return (int) Math.round(object.getAsDouble());
        else
            return 0;

    }

    public static int putJsonIntFromString(JsonElement object) {
        if (object != null && !object.isJsonNull())
            return Integer.parseInt(object.getAsString());
        else
            return 0;

    }

    public static int putJsonIntFromBoolean(JsonElement object) {
        if (object != null && !object.isJsonNull() && !object.getAsString().equals(""))
            if (object.getAsBoolean())
                return 1;
            else
                return 0;
        else
            return 0;

    }

    public static boolean putJsonBoolean(JsonElement object) {
        if (object != null && !object.isJsonNull() && !object.getAsString().equals(""))
            if (object.getAsBoolean())
                return true;
            else
                return false;
        else
            return false;

    }

    public static long putJsonLong(JsonElement object) {
        if (object != null && !object.isJsonNull() && !object.getAsString().equals(""))
            return object.getAsLong() * 1000L;
        else
            return -1;

    }

    public static long putJsonLongFromString(JsonElement object) {
        if (object != null && !object.isJsonNull() && !object.getAsString().equals(""))
            if (object.getAsString().equals(""))
                return 0;
            else
                return Long.parseLong(object.getAsString()) * 1000L;
        else
            return -1;

    }

    public static float putJsonFloat(JsonElement object) {
        if (object != null && !object.isJsonNull() && !object.getAsString().equals(""))
            return object.getAsFloat();
        else
            return 0;

    }

    public static double putJsonDouble(JsonElement object) {
        if (object != null && !object.isJsonNull() && !object.getAsString().equals(""))
            return object.getAsDouble();
        else
            return 0;

    }


}
