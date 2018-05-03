package com.mattbaby.vega.lib.net;

import android.text.TextUtils;

import com.mattbaby.vega.lib.LibBaseConfig;
import com.mattbaby.vega.lib.event.LoginoutEvent;
import com.mattbaby.vega.lib.net.cookie.ClearableCookieJar;
import com.mattbaby.vega.lib.net.cookie.PersistentCookieJar;
import com.mattbaby.vega.lib.net.cookie.cache.SetCookieCache;
import com.mattbaby.vega.lib.net.cookie.persistence.SharedPrefsCookiePersistor;
import com.mattbaby.vega.lib.net.tool.ConverterFactoryHelper;
import com.mattbaby.vega.lib.net.tool.HttpLoggingHelper;
import com.mattbaby.vega.lib.rx.RxBus;
import com.mattbaby.vega.lib.utils.ContextUtils;
import com.mattbaby.vega.lib.utils.FileUtils;
import com.mattbaby.vega.lib.utils.NetWorkUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


/**
 * Created by kevin on 16/6/17.
 */

public class NetworkManager {

    private static final int TIMEOUT = 15;
    private static volatile NetworkManager networkManager;
    private static Retrofit retrofit;
    private OkHttpClient okHttpClient;
    private String baseUrl;
    private Converter.Factory factory;
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
        Request request = chain.request();

        if (!NetWorkUtils.isConnected()) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(request);

        return response
                .newBuilder()
                .header("Cache-Control", "public, max-age=5")
                .removeHeader("Pragma").build();
    };

    public static NetworkManager getInstance() {
        if (networkManager == null) {
            synchronized (NetworkManager.class) {
                if (networkManager == null) {
                    networkManager = new NetworkManager();
                }
            }
        }
        return networkManager;
    }

    private NetworkManager() {
        baseUrl = LibBaseConfig.baseUrl;

        if (TextUtils.isEmpty(baseUrl))
            throw new NullPointerException("baseUrl is null");

        factory = LibBaseConfig.factory;

        if (factory == null)
            factory = ConverterFactoryHelper.createMoshiFactory();

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(ContextUtils.appContext));

        RxBus.INSTACE.asFlowable()
                .subscribe(o -> {
                    if (o instanceof LoginoutEvent) {
                        cookieJar.clear();
                    }
                });

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                .cache(getCache())
                .addInterceptor(HttpLoggingHelper.setBody())
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient);

        retrofit = builder.build();
    }

    /**
     * 缓存
     *
     * @return
     */
    private Cache getCache() {
        int cacheSize;
        String cachePath = FileUtils.getCacheDir();
        if (FileUtils.isSDCardMounted()) {
            cacheSize = 100 * 1024 * 1024;
        } else {
            cacheSize = 20 * 1024 * 1024;
        }
        return new Cache(new File(cachePath), cacheSize);
    }

    public <T> T create(Class<T> clasz) {
        return retrofit.create(clasz);
    }
}
