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
     * Gets the SharedPreferences for the given name.
     *
     * @param context         the context
     * @param namePreferences the name of the preferences to get
     * @return the preferences associated with the given name
     */
    public static SharedPreferences getPreferences(Context context, String namePreferences) {
        return context.getSharedPreferences(namePreferences, Context.MODE_PRIVATE);
    }

    /**
     * Clean the SharedPreferences for the given name.
     *
     * @param context         the context
     * @param namePreferences the name of the preferences to remove
     */
    public static void removeAll(Context context, String namePreferences) {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.clear();
        editor.commit();
    }


    /**
     * Get the editor for the SharedPreferences for the given name.
     *
     * @param context         the context
     * @param namePreferences the name of the preferences to get
     * @return the editor of the given preferences' name
     */
    public static SharedPreferences.Editor getEditor(Context context, String namePreferences) {
        return getPreferences(context, namePreferences).edit();
    }

    /**
     * Set a String value for a key value in the SharedPreferences with a given name.
     *
     * @param context         the context
     * @param namePreferences the name of the preferences
     * @param key             the key of the value to set
     * @param value           the value to set
     */
    public static void setPreferences(Context context, String namePreferences, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Set a float value for a key value in the SharedPreferences with a given name.
     *
     * @param context         the context
     * @param namePreferences the name of the preferences
     * @param key             the key of the value
     * @param value           the value to set
     */
    public static void setPreferences(Context context, String namePreferences, String key, float value) {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * Set a long value for a key value in the SharedPreferences with a given name.
     *
     * @param context         the context
     * @param namePreferences the name of the preferences
     * @param key             the key of the value
     * @param value           the value to set
     */
    public static void setPreferences(Context context, String namePreferences, String key, long value) {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * Set a int value for a key value in the SharedPreferences with a given name.
     *
     * @param context         the context
     * @param namePreferences the name of the preferences
     * @param key             the key of the value
     * @param value           the value to set
     */
    public static void setPreferences(Context context, String namePreferences, String key, int value) {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * Set a boolean value for a key value in the SharedPreferences with a given name.
     *
     * @param context         the context
     * @param namePreferences the name of the preferences
     * @param key             the key of the value
     * @param value           the value to set
     */
    public static void setPreferences(Context context, String namePreferences, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(context, namePreferences);
        editor.putBoolean(key, value);
        editor.commit();
    }

}
