package com.mr_apps.androidbase.utils;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by bronx on 23/10/14
 */
public class KeyboardUtil {

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // Ignore exceptions if any
            Log.e("KeyBoardUtil", e.toString(), e);
        }
    }

    public static void hideKeyboard(Activity activity, View view) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // Ignore exceptions if any
            Log.e("KeyBoardUtil", e.toString(), e);
        }
    }
}
