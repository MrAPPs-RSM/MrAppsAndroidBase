package com.mr_apps.androidbase.utils;

import android.util.Log;

import com.mr_apps.androidbase.BuildConfig;


/**
 * Created by denis on 23/09/15.
 */
public class Logger {

    public static void d(String Tag, String msg)
    {
        if(BuildConfig.DEBUG)
            Log.d(Tag, msg);
    }

}
