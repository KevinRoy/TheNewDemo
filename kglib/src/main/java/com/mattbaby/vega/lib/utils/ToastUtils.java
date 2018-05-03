package com.mattbaby.vega.lib.utils;

import android.widget.Toast;

/**
 * Created by kevin on 16/10/10.
 */

public class ToastUtils {
    public static void show(String string) {
        Toast.makeText(ContextUtils.appContext, string, Toast.LENGTH_LONG).show();
    }

    public static void show(int resId) {
        Toast.makeText(ContextUtils.appContext, resId, Toast.LENGTH_LONG).show();
    }

    public static void showShort(String String) {
        Toast.makeText(ContextUtils.appContext, String, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(int resId) {
        Toast.makeText(ContextUtils.appContext, resId, Toast.LENGTH_SHORT).show();
    }

}
