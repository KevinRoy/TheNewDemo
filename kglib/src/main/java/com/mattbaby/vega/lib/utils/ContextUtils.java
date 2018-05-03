package com.mattbaby.vega.lib.utils;

import android.content.Context;

/**
 * context 相关的util
 */

public class ContextUtils {

    public static Context appContext;

    public static void init(Context context) {
        appContext = context.getApplicationContext();
    }
}
