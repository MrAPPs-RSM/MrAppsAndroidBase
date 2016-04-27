package com.mr_apps.androidbase.utils;

import android.util.Log;


/**
 * Created by denis on 23/09/15
 */
public class Logger {

    private static boolean hideLog = false;

    public static void setHideLog(boolean hideLog) {
        Logger.hideLog = hideLog;
    }

    public static void d(String Tag, String msg) {
        if (!hideLog)
            Log.d(Tag, msg);
    }

    public static void i(String Tag, String msg) {
        if (!hideLog)
            Log.i(Tag, msg);
    }

    public static void e(String Tag, String msg) {
        if (!hideLog)
            Log.e(Tag, msg);
    }

    public static void v(String Tag, String msg) {
        if (!hideLog)
            Log.v(Tag, msg);
    }

    public static void w(String Tag, String msg) {
        if (!hideLog)
            Log.w(Tag, msg);
    }

    public static void wtf(String Tag, String msg) {
        if (!hideLog)
            Log.wtf(Tag, msg);
    }

}
