package com.mattbaby.vega.lib.net.tool;

import android.util.Log;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * http log helper
 * Created by kevin on 16/8/10.
 */

public class HttpLoggingHelper {

    public static final String TAG = "okhttp";

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> {
        Log.d(TAG, message);
    });

    public static HttpLoggingInterceptor setBody() {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    public static HttpLoggingInterceptor setBasic() {
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return logging;
    }

    public static HttpLoggingInterceptor setHeaders() {
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return logging;
    }
}
