package app.witkey.live.utils;

import android.util.Log;

import app.witkey.live.BuildConfig;


/**
 * created by developer on 6/9/2017.
 */
public class LogUtils {
    private static final boolean isLogEnabled = BuildConfig.DEBUG;

    public static void d(String tag, String msg) {
        if (isLogEnabled) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isLogEnabled) {
            Log.i(tag, msg);
        }
    }
    public static void w(String tag, String msg) {
        if (isLogEnabled) {
            Log.w(tag, msg);
        }
    }
    public static void v(String tag, String msg) {
        if (isLogEnabled) {
            Log.v(tag, msg);
        }
    }

    public static void wtf(String tag, String msg) {
        if (isLogEnabled) {
            Log.wtf(tag, msg);
        }
    }
    public static void e(String tag, String msg) {
        if (isLogEnabled) {
            Log.e(tag, msg);
        }
    }



}
