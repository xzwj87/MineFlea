package com.github.xzwj87.mineflea.utils;

import android.util.Log;

/**
 * Created by seaice on 2016/8/12.
 */
public class LogUtils {
    private static final String TAG = "zhihuribao";

    public static final int LEVEL_NONE = 0;
    public static final int LEVEL_VERBOSE = 1;
    public static final int LEVEL_DEBUG = 2;
    public static final int LEVEL_INFO = 3;
    public static final int LEVEL_WARN = 4;
    public static final int LEVEL_ERROR = 5;

    private static int mDebugLevel = LEVEL_ERROR;

    public static void v(String msg) {
        if (mDebugLevel >= LEVEL_VERBOSE) {
            Log.v(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (mDebugLevel >= LEVEL_DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (mDebugLevel >= LEVEL_INFO) {
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (mDebugLevel >= LEVEL_WARN) {
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (mDebugLevel >= LEVEL_ERROR) {
            Log.e(TAG, msg);
        }
    }
}
