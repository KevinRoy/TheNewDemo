package com.mattbaby.vega.lib.rx;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * RxBus
 * Created by kevin on 16/5/22.
 */

public enum RxBus {

    INSTACE;

    private static final Relay<Object> _bus = PublishRelay.create().toSerialized();

    public void send(Object o) {
        if (hasObservers()) {
            try {
                _bus.accept(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Flowable<Object> asFlowable() {
        return _bus.toFlowable(BackpressureStrategy.LATEST)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }
}
