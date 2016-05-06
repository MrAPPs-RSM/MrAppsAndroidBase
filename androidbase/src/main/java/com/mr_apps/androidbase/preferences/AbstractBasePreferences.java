package com.mr_apps.androidbase.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Abstract class with base methods to handle SharedPreferences
 *
 * @author Denis Brandi
 */
public abstract class AbstractBasePreferences {

    /**
     *
     * Get the SharedPreferences for the given name.
     *
     * @param context
     * @param namePreferences
     * @return
     */
    public static SharedPreferences getPreferences(Context context, String namePreferences) {
        return context.getSharedPreferences(namePreferences, Context.MODE_PRIVATE);
    }

    /**
     *
     * Clean the SharedPreferences for the given name.
     *
     * @param context
     * @param namePreferences
     */
    public static void removeAll(Context context, String namePreferences) {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.clear();
        editor.commit();
    }


    /**
     *
     * Get the editor for the SharedPreferences for the given name.
     *
     * @param context
     * @param namePreferences
     * @return
     */
    public static SharedPreferences.Editor getEditor(Context context, String namePreferences) {
        return getPreferences(context, namePreferences).edit();
    }

    /**
     *
     * Set a String value for a key value in the SharedPreferences with a given name.
     *
     * @param context
     * @param namePreferences
     * @param key
     * @param value
     */
    public static void setPreferences(Context context, String namePreferences, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.putString(key, value);
        editor.commit();
    }

    /**
     *
     * Set a float value for a key value in the SharedPreferences with a given name.
     *
     * @param context
     * @param namePreferences
     * @param key
     * @param value
     */
    public static void setPreferences(Context context, String namePreferences, String key, float value) {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     *
     * Set a long value for a key value in the SharedPreferences with a given name.
     *
     * @param context
     * @param namePreferences
     * @param key
     * @param value
     */
    public static void setPreferences(Context context, String namePreferences, String key, long value) {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     *
     * Set a int value for a key value in the SharedPreferences with a given name.
     *
     * @param context
     * @param namePreferences
     * @param key
     * @param value
     */
    public static void setPreferences(Context context, String namePreferences, String key, int value) {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     *
     * Set a boolean value for a key value in the SharedPreferences with a given name.
     *
     * @param context
     * @param namePreferences
     * @param key
     * @param value
     */
    public static void setPreferences(Context context, String namePreferences, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.putBoolean(key, value);
        editor.commit();
    }

}
