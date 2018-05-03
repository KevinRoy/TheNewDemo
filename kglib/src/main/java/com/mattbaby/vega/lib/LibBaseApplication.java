package com.mattbaby.vega.lib;

import android.app.Application;

import com.mattbaby.vega.lib.utils.ContextUtils;

import retrofit2.Converter;

/**
 * Created by kevin on 16/6/18.
 */

public abstract class LibBaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ContextUtils.init(this);

        LibBaseConfig.baseUrl = getDefaultBaseUrl();
        LibBaseConfig.factory = getConverterFactory();
    }

    /**
     * 设置主URL
     *
     * @return
     */
    protected abstract String getDefaultBaseUrl();

    /**
     * 设置CoverterFactory 目前主要是Gson和Moshi
     *
     * @return
     */
    protected abstract Converter.Factory getConverterFactory();
}
