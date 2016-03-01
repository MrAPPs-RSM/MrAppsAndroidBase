package com.mr_apps.androidbase.utils;

import android.util.Log;


/**
 * Created by denis on 23/09/15.
 */
public class Logger {

    private static boolean hideLog=false;

    public static void setHideLog(boolean hideLog) {
        Logger.hideLog = hideLog;
    }

    public static void d(String Tag, String msg)
    {
        if(!hideLog)
            Log.d(Tag, msg);
    }

}
