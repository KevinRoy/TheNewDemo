package com.mattbaby.vega.lib.rx;

import java.util.concurrent.TimeUnit;

import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * common compose
 * Created by kevin on 16/8/12.
 */

public class Compose {

    public static final int Time = 5000;  //5s

    /**
     * normal
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * delay time
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> applyTimeSchedulers() {
        return observable -> observable.throttleFirst(Time, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * set delay time
     *
     * @param time
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> applyTimeSchedulers(int time) {
        return observable -> observable.throttleFirst(time, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
