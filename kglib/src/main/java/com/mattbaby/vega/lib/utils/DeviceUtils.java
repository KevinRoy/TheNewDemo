package com.mattbaby.vega.lib.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Method;

/**
 * 和硬件相关的 util
 * Created by kevin on 16/8/10.
 */

public class DeviceUtils {
    /**
     * 获取设备的制造商
     *
     * @return 设备制造商
     */
    public static String getDeviceManufacture() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取设备名称
     *
     * @return 设备名称
     */
    public static String getDeviceName() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取设备号
     *
     * @return
     */
    public static String getDeviceIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) ContextUtils.appContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null || TextUtils.isEmpty(telephonyManager.getDeviceId())) {
            return Settings.Secure.getString(ContextUtils.appContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            return telephonyManager.getDeviceId();
        }
    }

    /**
     * 获取应用的版本名
     */
    public static String getAppVersion() {
        PackageManager packageManager = ContextUtils.appContext.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(ContextUtils.appContext.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用的包名
     *
     * @return
     */
    public static String getPackageName() {
        return ContextUtils.appContext.getPackageName();
    }

    /**
     * 获取应用的版本号
     */
    public static int getVersionCode() {
        int versionCode;
        try {
            versionCode = ContextUtils.appContext.getPackageManager()
                    .getPackageInfo(ContextUtils.appContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            versionCode = -1;
        }
        return versionCode;
    }

    /**
     * 获取屏幕的宽度
     */
    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = ContextUtils.appContext.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕的高度
     */
    public static int getScreenHeight() {
        DisplayMetrics displayMetrics = ContextUtils.appContext.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * 隐藏输入键盘
     *
     * @param view
     */
    public static void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) ContextUtils.appContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 隐藏输入键盘
     */
    public static void hideSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) ContextUtils.appContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getWindowToken(),
                0);
    }

    /**
     * 隐藏输入键盘
     */
    public static void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) ContextUtils.appContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 获取虚拟按键的高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    public static String getUUID() {
        return "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10;
    }

    /**
     * 判断是否有虚拟键
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }
}
