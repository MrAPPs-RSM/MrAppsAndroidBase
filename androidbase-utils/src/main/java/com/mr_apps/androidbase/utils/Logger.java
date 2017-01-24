package com.mr_apps.androidbase.utils;

import android.util.Log;


/**
 * Class that provides method to write on the console log
 *
 * @author Denis Brandi
 */
public class Logger {

    private static boolean hideLog = false;

    /**
     * Sets that every log created with this class has to be ignored
     *
     * @param hideLog true if the log should be hidden, false otherwise
     */
    public static void setHideLog(boolean hideLog) {
        Logger.hideLog = hideLog;
    }

    /**
     * Sends a debug log message
     *
     * @param Tag the tag of the log
     * @param msg the message of the log
     */
    public static void d(String Tag, String msg) {
        if (!hideLog)
            Log.d(Tag, msg);
    }

    /**
     * Sends a debug info message
     *
     * @param Tag the tag of the log
     * @param msg the message of the log
     */
    public static void i(String Tag, String msg) {
        if (!hideLog)
            Log.i(Tag, msg);
    }

    /**
     * Sends a error log message
     *
     * @param Tag the tag of the log
     * @param msg the message of the log
     */
    public static void e(String Tag, String msg) {
        if (!hideLog)
            Log.e(Tag, msg);
    }

    /**
     * Sends a verbose log message
     *
     * @param Tag the tag of the log
     * @param msg the message of the log
     */
    public static void v(String Tag, String msg) {
        if (!hideLog)
            Log.v(Tag, msg);
    }

    /**
     * Sends a warn log message
     *
     * @param Tag the tag of the log
     * @param msg the message of the log
     */
    public static void w(String Tag, String msg) {
        if (!hideLog)
            Log.w(Tag, msg);
    }

    /**
     * Sends a special error log message
     *
     * @param Tag the tag of the log
     * @param msg the message of the log
     */
    public static void wtf(String Tag, String msg) {
        if (!hideLog)
            Log.wtf(Tag, msg);
    }

}
