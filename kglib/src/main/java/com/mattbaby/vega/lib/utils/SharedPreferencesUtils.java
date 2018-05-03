package com.mattbaby.vega.lib.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SharedPreferences相关的util
 * Created by kevin on 16/8/9.
 */

public class SharedPreferencesUtils {

    public static <T> T get(String key, T defValue) {
        if (defValue instanceof String) {
            return (T) getPreference().getString(key, (String) defValue);
        } else if (defValue instanceof Integer) {
            Integer value = getPreference().getInt(key, (Integer) defValue);
            return (T) value;
        } else if (defValue instanceof Boolean) {
            Boolean value = getPreference().getBoolean(key, (Boolean) defValue);
            return (T) value;
        } else if (defValue instanceof Float) {
            Float value = getPreference().getFloat(key, (Float) defValue);
            return (T) value;
        } else if (defValue instanceof Double) {
            Float value = getPreference().getFloat(key, ((Double) defValue).floatValue());
            Double dValue = value.doubleValue();
            return (T) dValue;
        } else if (defValue instanceof Long) {
            Long value = getPreference().getLong(key, (Long) defValue);
            return (T) value;
        }
        return (T) new Object();
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean save(String key, T value) {
        SharedPreferences.Editor editor = getPreference().edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Double) {
            editor.putFloat(key, ((Double) value).floatValue());
        }
        return editor.commit();
    }

    public static SharedPreferences getPreference() {
        return PreferenceManager.getDefaultSharedPreferences(ContextUtils.appContext);
    }

    public static SharedPreferences getPreference(String name) {
        return ContextUtils.appContext.getSharedPreferences(name, 0);
    }
}
