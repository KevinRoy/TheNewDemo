package com.mattbaby.vega.lib.json;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

/**
 * Created by kevin on 16/8/12.
 */

public class MoshiAdapter {

    private MoshiAdapterListener moshiAdapterListener;

    public void setMoshiAdapterListener(MoshiAdapterListener moshiAdapterListener) {
        this.moshiAdapterListener = moshiAdapterListener;
    }

    @FromJson
    Object fromJson(Object p) {
        return moshiAdapterListener.fromJson(p);
    }

    @ToJson
    Object toJson(Object t) {
        return moshiAdapterListener.toJson(t);
    }

    public interface MoshiAdapterListener<T, P> {
        T fromJson(P p);

        P toJson(T t);
    }
}
