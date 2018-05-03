package com.mattbaby.vega.lib.utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * 一些细微比如dp的操作和转化
 * Created by kevin on 16/7/9.
 */

public class RuleUtils {
    /**
     * 将dp转换成对应的px值
     */
    public static int dp2Px(float dp) {
        DisplayMetrics metrics = ContextUtils.appContext.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    /**
     * 将px转换成对应的dp值
     */
    public static int px2Dp(float px) {
        final float scale = ContextUtils.appContext.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 将sp转换成对应的px值
     */
    public static int sp2Px(float sp) {
        DisplayMetrics metrics = ContextUtils.appContext.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }
}
