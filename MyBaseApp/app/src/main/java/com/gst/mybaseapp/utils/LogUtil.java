package com.gst.mybaseapp.utils;

import android.util.Log;

import me.jessyan.autosize.utils.LogUtils;

/**
 * author: GuoSongtao on 2019/4/9 13:57
 * email: 157010607@qq.com
 */
public class LogUtil {
    private static final String TAG = "MyBaseApp";
    private static boolean debug;

    private LogUtil() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        LogUtil.debug = debug;
    }

    public static void d(String message) {
        if (debug) {
            Log.d(TAG, message);
        }
    }

    public static void w(String message) {
        if (debug) {
            Log.w(TAG, message);
        }
    }

    public static void e(String message) {
        if (debug) {
            Log.e(TAG, message);
        }
    }
}
