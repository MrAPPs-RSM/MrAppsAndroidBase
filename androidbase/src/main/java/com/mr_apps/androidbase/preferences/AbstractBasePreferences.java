package com.mr_apps.androidbase.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by denis on 29/02/16.
 */
public abstract class AbstractBasePreferences {

    public static SharedPreferences getPreferences(Context context, String namePreferences) {
        return context.getSharedPreferences(namePreferences, Context.MODE_PRIVATE);
    }

    public static void removeAll(Context context, String namePreferences) {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.clear();
        editor.commit();
    }

    public static SharedPreferences.Editor getEditor(Context context, String namePreferences) {
        return getPreferences(context, namePreferences).edit();
    }

    public static void setPreferences(Context context, String namePreferences, String key, String value)
    {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.putString(key, value);
        editor.commit();
    }

    public static void setPreferences(Context context, String namePreferences, String key, float value)
    {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void setPreferences(Context context, String namePreferences, String key, long value)
    {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.putLong(key, value);
        editor.commit();
    }

    public static void setPreferences(Context context, String namePreferences, String key, boolean value)
    {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.putBoolean(key, value);
        editor.commit();
    }

}
