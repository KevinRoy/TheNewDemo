package com.mattbaby.vega.lib;

import com.mattbaby.vega.lib.utils.SharedPreferencesUtils;

/**
 * Created by kevin on 2017/7/13.
 */

public enum CommonManager {

    INSTANCE;

    public static final String KEY_VERSION = "key_version";
    public static final String KEY_PLATFORM = "key_platform";
    public static final String KEY_DEVICEID = "key_deviceId";

    public void saveVersion(String version) {
        SharedPreferencesUtils.save(KEY_VERSION, version);
    }

    public void savePlatform(String platform) {
        SharedPreferencesUtils.save(KEY_PLATFORM, platform);
    }

    public void saveDeviceId(String deviceId) {
        SharedPreferencesUtils.save(KEY_DEVICEID, deviceId);
    }

    public String getKeyVersion() {
        return SharedPreferencesUtils.get(KEY_VERSION, "");
    }

    public String getKeyPlatform() {
        return SharedPreferencesUtils.get(KEY_PLATFORM, "");
    }

    public String getKeyDeviceid() {
        return SharedPreferencesUtils.get(KEY_DEVICEID, "");
    }
}
