package com.mattbaby.vega.lib.json;

import com.mattbaby.vega.lib.utils.LogUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * Created by kevin on 16/8/12.
 */

public class MoshiUtils {

    private static Moshi.Builder mBuilder = new Moshi.Builder();
    public static Moshi mMoshi = mBuilder.build();

    public static <T> T fromJson(String json, Class<T> clasz) {
        T result = null;
        try {
            JsonAdapter<T> jsonAdapter = mMoshi.adapter(clasz);
            result = jsonAdapter.fromJson(json);
        } catch (Exception e) {
            LogUtils.d("不能解析" + clasz.getName());
        }
        return result;
    }

    public static String toJson(Object object) {
        String result = "";
        try {
            JsonAdapter<Object> jsonAdapter = mMoshi.adapter(Object.class);
            result = jsonAdapter.toJson(object);
        } catch (Exception e) {
            LogUtils.d("对象无法转换json");
        }
        return result;
    }

    public static void addAdapter(MoshiAdapter.MoshiAdapterListener moshiAdapterListener) {
        MoshiAdapter moshiAdapter = new MoshiAdapter();
        moshiAdapter.setMoshiAdapterListener(moshiAdapterListener);

        if (moshiAdapter != null)
            mMoshi = mBuilder.add(moshiAdapter).build();
    }
}
