package com.mr_apps.androidbase.webservice;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mr_apps.androidbase.preferences.SecurityPreferences;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

/**
 * Created by denis on 02/02/16.
 */
public abstract class WebServiceUtils {

    private static final String TAG="WebServiceUtils";

    public static Map<String, List<String>> getSecurity(Context context)
    {

        long diffInMinutes=diffInMinutes((new Date()).getTime(), SecurityPreferences.getHeader_date(context));

        if(SecurityPreferences.getHeader(context).length()==0 || diffInMinutes>4) {
            updateSecurity(context);
        }

        Map<String, List<String>> map= new HashMap<>();

        map.put(WsseToken.HEADER_AUTHORIZATION, Arrays.asList(SecurityPreferences.getAuthorization(context)));
        map.put(WsseToken.HEADER_WSSE, Arrays.asList(SecurityPreferences.getHeader(context)));

        return map;
    }

    private static void updateSecurity(Context context)
    {
        WsseToken wsseToken=new WsseToken(context);
        SharedPreferences preferences=context.getSharedPreferences(SecurityPreferences.namePreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(SecurityPreferences.authorization, wsseToken.getAuthorizationHeader());
        editor.putLong(SecurityPreferences.header_date, (new Date()).getTime());
        editor.putString(SecurityPreferences.header, wsseToken.getWsseHeader());
        editor.commit();
    }

    private static long diffInMinutes(long date1, long date2)
    {
        long diffInMillisec=date1-date2;

        return TimeUnit.MILLISECONDS.toMinutes(diffInMillisec);
    }

    public static JsonObject putJsonObject(JsonElement object)
    {
        if(object==null || object.isJsonNull() || !object.isJsonObject())
            return null;
        else
            return object.getAsJsonObject();
    }

    public static String putJsonString(JsonElement object)
    {
        try {

            if (object != null && !object.isJsonNull())
                return object.getAsString();
            else
                return "";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }

    }

    public static int putJsonInt(JsonElement object)
    {
        if(object!=null && !object.isJsonNull() && !object.getAsString().equals(""))
            return object.getAsInt();
        else
            return 0;

    }

    public static int putJsonIntRounded(JsonElement object)
    {
        if(object!=null && !object.isJsonNull() && !object.getAsString().equals(""))
            return (int) Math.round(object.getAsDouble());
        else
            return 0;

    }

    public static int putJsonIntFromString(JsonElement object)
    {
        if(object!=null && !object.isJsonNull())
            return Integer.parseInt(object.getAsString());
        else
            return 0;

    }

    public static int putJsonIntFromBoolean(JsonElement object)
    {
        if(object!=null && !object.isJsonNull() && !object.getAsString().equals(""))
            if(object.getAsBoolean())
                return 1;
            else
                return 0;
        else
            return 0;

    }

    public static boolean putJsonBoolean(JsonElement object)
    {
        if(object!=null && !object.isJsonNull() && !object.getAsString().equals(""))
            if(object.getAsBoolean())
                return true;
            else
                return false;
        else
            return false;

    }

    public static long putJsonLong(JsonElement object)
    {
        if(object!=null && !object.isJsonNull() && !object.getAsString().equals(""))
            return object.getAsLong()*1000L;
        else
            return -1;

    }

    public static long putJsonLongFromString(JsonElement object)
    {
        if(object!=null && !object.isJsonNull() && !object.getAsString().equals(""))
            if(object.getAsString().equals(""))
                return 0;
            else
                return Long.parseLong(object.getAsString())*1000L;
        else
            return -1;

    }

    public static float putJsonFloat(JsonElement object)
    {
        if(object!=null && !object.isJsonNull() && !object.getAsString().equals(""))
            return object.getAsFloat();
        else
            return 0;

    }

    public static double putJsonDouble(JsonElement object)
    {
        if(object!=null && !object.isJsonNull() && !object.getAsString().equals(""))
            return object.getAsDouble();
        else
            return 0;

    }


}
