package com.mattbaby.vega.lib.utils;

import android.text.TextUtils;
import android.util.Log;

import com.mattbaby.vega.lib.BuildConfig;

/**
 * Created by kevin on 17/5/16.
 */

public class LogUtils {

    public static final String LOGTAG = "logtag";
    public static final String LOGTAG_ERROR = "logtagerror";

    private static final boolean isLog = BuildConfig.DEBUG ? true : false;

    public static void d(String tag, String d) {
        if (isLog)
            Log.d(TextUtils.isEmpty(tag) ? LOGTAG : tag, d);
    }

    public static void d(String d) {
        d("", d);
    }

    public static void e(String tag, String e) {
        if (isLog)
            Log.e(TextUtils.isEmpty(tag) ? LOGTAG : tag, e);
    }

    public static void e(String e) {
        e("", e);
    }

    public static void i(String tag, String i) {
        if (isLog)
            Log.i(TextUtils.isEmpty(tag) ? LOGTAG : tag, i);
    }

    public static void i(String i) {
        i("", i);
    }
}
