package com.mr_apps.androidbase.webservice;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Class to handle JsonElements to avoid crashes and additional code
 *
 * @author Denis Brandi
 */
public abstract class WebServiceUtils {

    private static final String TAG = "WebServiceUtils";

    /**
     * Get the JsonArray for the given JsonElement
     *
     * @param object the Json Object
     * @return null if the JsonElement is not a JsonArray or is null
     */
    public static JsonArray putJsonArray(JsonElement object) {
        if (object == null || object.isJsonNull() || !object.isJsonArray())
            return null;
        else
            return object.getAsJsonArray();
    }

    /**
     * Get the JsonObject for the given JsonElement
     *
     * @param object the Json Object
     * @return null if the JsonElement is not a JsonObject or is null
     */
    public static JsonObject putJsonObject(JsonElement object) {
        if (object == null || object.isJsonNull() || !object.isJsonObject())
            return null;
        else
            return object.getAsJsonObject();
    }

    /**
     * Get the String for the given JsonElement
     *
     * @param object the Json Object
     * @return null if the JsonElement is not a String or is null
     */
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

    /**
     * Get the int for the given JsonElement
     *
     * @param object the Json Object
     * @return 0 if the JsonElement is not a int or is null
     */
    public static int putJsonInt(JsonElement object) {
        if (object != null && !object.isJsonNull() && !object.getAsString().equals(""))
            return object.getAsInt();
        else
            return 0;

    }

    /**
     * Get the rounded int for the given JsonElement
     *
     * @param object the Json Object
     * @return 0 if the JsonElement is not a int or is null
     */
    public static int putJsonIntRounded(JsonElement object) {
        if (object != null && !object.isJsonNull() && !object.getAsString().equals(""))
            return (int) Math.round(object.getAsDouble());
        else
            return 0;

    }

    /**
     * Get the int for the given JsonElement that may contain an int or a String
     *
     * @param object the Json Object
     * @return null if the JsonElement is not a String or is null
     */
    public static int putJsonIntFromString(JsonElement object) {
        if (object != null && !object.isJsonNull())
            return Integer.parseInt(object.getAsString());
        else
            return 0;

    }

    /**
     * Get the int for the given JsonElement that contains a boolean value
     *
     * @param object the Json Object
     * @return null if the JsonElement is not a boolean or is null
     */
    public static int putJsonIntFromBoolean(JsonElement object) {
        if (object != null && !object.isJsonNull() && !object.getAsString().equals(""))
            if (object.getAsBoolean())
                return 1;
            else
                return 0;
        else
            return 0;

    }

    /**
     * Get the boolean for the given JsonElement
     *
     * @param object the Json Object
     * @return null if the JsonElement is not a boolean or is null
     */
    public static boolean putJsonBoolean(JsonElement object) {
        if (object != null && !object.isJsonNull() && !object.getAsString().equals(""))
            if (object.getAsBoolean())
                return true;
            else
                return false;
        else
            return false;

    }

    /**
     * Get the timestamp in millis (long) for the given JsonElement
     *
     * @param object the Json Object
     * @return null if the JsonElement is not a long or is null
     */
    public static long putJsonLong(JsonElement object) {
        if (object != null && !object.isJsonNull() && !object.getAsString().equals(""))
            return object.getAsLong() * 1000L;
        else
            return -1;

    }

    /**
     * Get the timestamp in millis (long) for the given JsonElement that may contain an long or a String
     *
     * @param object the Json Object
     * @return null if the JsonElement is not a String or is null
     */
    public static long putJsonLongFromString(JsonElement object) {
        if (object != null && !object.isJsonNull() && !object.getAsString().equals(""))
            if (object.getAsString().equals(""))
                return 0;
            else
                return Long.parseLong(object.getAsString()) * 1000L;
        else
            return -1;

    }

    /**
     * Get the float for the given JsonElement
     *
     * @param object the Json Object
     * @return null if the JsonElement is not a float or is null
     */
    public static float putJsonFloat(JsonElement object) {
        if (object != null && !object.isJsonNull() && !object.getAsString().equals(""))
            return object.getAsFloat();
        else
            return 0;

    }

    /**
     * Get the double for the given JsonElement
     *
     * @param object the Json Object
     * @return null if the JsonElement is not a double or is null
     */
    public static double putJsonDouble(JsonElement object) {
        if (object != null && !object.isJsonNull() && !object.getAsString().equals(""))
            return object.getAsDouble();
        else
            return 0;

    }


}
