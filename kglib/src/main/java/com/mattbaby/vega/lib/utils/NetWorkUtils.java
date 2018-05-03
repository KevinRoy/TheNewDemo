package com.mattbaby.vega.lib.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * FileName: NetWorkUtils.java
 * description:网络状态
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/12
 */
public class NetWorkUtils {
    /**
     * 网络类型 - 无连接
     */
    public static final int NETWORK_TYPE_NO_CONNECTION = -1231545315;

    public static final String NETWORK_TYPE_WIFI = "wifi";
    public static final String NETWORK_TYPE_3G = "eg";
    public static final String NETWORK_TYPE_2G = "2g";
    public static final String NETWORK_TYPE_WAP = "wap";
    public static final String NETWORK_TYPE_UNKNOWN = "unknown";
    public static final String NETWORK_TYPE_DISCONNECT = "disconnect";

    /**
     * Get network type
     *
     * @return 网络状态
     */
    public static int getNetworkType() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ContextUtils.appContext.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager == null
                ? null
                : connectivityManager.getActiveNetworkInfo();
        return networkInfo == null ? -1 : networkInfo.getType();
    }


    /**
     * Get network type name
     *
     * @return NetworkTypeName
     */
    public static String getNetworkTypeName() {
        ConnectivityManager manager
                = (ConnectivityManager) ContextUtils.appContext.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        String type = NETWORK_TYPE_DISCONNECT;
        if (manager == null ||
                (networkInfo = manager.getActiveNetworkInfo()) == null) {
            return type;
        }
        ;

        if (networkInfo.isConnected()) {
            String typeName = networkInfo.getTypeName();
            if ("WIFI".equalsIgnoreCase(typeName)) {
                type = NETWORK_TYPE_WIFI;
            } else if ("MOBILE".equalsIgnoreCase(typeName)) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                type = TextUtils.isEmpty(proxyHost)
                        ? (isFastMobileNetwork()
                        ? NETWORK_TYPE_3G
                        : NETWORK_TYPE_2G)
                        : NETWORK_TYPE_WAP;
            } else {
                type = NETWORK_TYPE_UNKNOWN;
            }
        }
        return type;
    }


    /**
     * Whether is fast mobile network
     *
     * @return FastMobileNetwork
     */
    private static boolean isFastMobileNetwork() {
        TelephonyManager telephonyManager
                = (TelephonyManager) ContextUtils.appContext.getSystemService(
                Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return false;
        }

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }


    /**
     * 获取当前网络的状态
     *
     * @return 当前网络的状态。具体类型可参照NetworkInfo.State.CONNECTED、NetworkInfo.State.CONNECTED.DISCONNECTED等字段。当前没有网络连接时返回null
     */
    public static NetworkInfo.State getCurrentNetworkState() {
        NetworkInfo networkInfo
                = ((ConnectivityManager) ContextUtils.appContext.getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null ? networkInfo.getState() : null;
    }


    /**
     * 获取当前网络的类型
     *
     * @return 当前网络的类型。具体类型可参照ConnectivityManager中的TYPE_BLUETOOTH、TYPE_MOBILE、TYPE_WIFI等字段。当前没有网络连接时返回NetworkUtils.NETWORK_TYPE_NO_CONNECTION
     */
    public static int getCurrentNetworkType() {
        NetworkInfo networkInfo
                = ((ConnectivityManager) ContextUtils.appContext.getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null
                ? networkInfo.getType()
                : NETWORK_TYPE_NO_CONNECTION;
    }


    /**
     * 获取当前网络的具体类型
     *
     * @return 当前网络的具体类型。具体类型可参照TelephonyManager中的NETWORK_TYPE_1xRTT、NETWORK_TYPE_CDMA等字段。当前没有网络连接时返回NetworkUtils.NETWORK_TYPE_NO_CONNECTION
     */
    public static int getCurrentNetworkSubtype() {
        NetworkInfo networkInfo
                = ((ConnectivityManager) ContextUtils.appContext.getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null
                ? networkInfo.getSubtype()
                : NETWORK_TYPE_NO_CONNECTION;
    }


    /**
     * 判断当前网络是否已经连接
     *
     * @return 当前网络是否已经连接。false：尚未连接
     */
    public static boolean isConnected() {
        return getCurrentNetworkState() == NetworkInfo.State.CONNECTED;
    }


    /**
     * 判断当前网络是否正在连接
     *
     * @return 当前网络是否正在连接
     */
    public static boolean isConnecting() {
        return getCurrentNetworkState() == NetworkInfo.State.CONNECTING;
    }


    /**
     * 判断当前网络是否已经断开
     *
     * @return 当前网络是否已经断开
     */
    public static boolean isDisconnected() {
        return getCurrentNetworkState() == NetworkInfo.State.DISCONNECTED;
    }


    /**
     * 判断当前网络是否正在断开
     *
     * @return 当前网络是否正在断开
     */
    public static boolean isDisconnecting() {
        return getCurrentNetworkState() == NetworkInfo.State.DISCONNECTING;
    }


    /**
     * 判断当前网络是否已经暂停
     *
     * @return 当前网络是否已经暂停
     */
    public static boolean isSuspended() {
        return getCurrentNetworkState() == NetworkInfo.State.SUSPENDED;
    }


    /**
     * 判断当前网络是否处于未知状态中
     *
     * @return 当前网络是否处于未知状态中
     */
    public static boolean isUnknown() {
        return getCurrentNetworkState() == NetworkInfo.State.UNKNOWN;
    }


    /**
     * 判断当前网络的类型是否是蓝牙
     *
     * @return 当前网络的类型是否是蓝牙。false：当前没有网络连接或者网络类型不是蓝牙
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static boolean isBluetooth() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            return false;
        } else {
            return getCurrentNetworkType() ==
                    ConnectivityManager.TYPE_BLUETOOTH;
        }
    }

    /**
     * 判断当前网络的类型是否是Wifi
     *
     * @return 当前网络的类型是否是Wifi。false：当前没有网络连接或者网络类型不是wifi
     */
    public static boolean isWifi() {
        return getCurrentNetworkType() == ConnectivityManager.TYPE_WIFI;
    }


    /**
     * 判断当前网络的具体类型是否是GPRS
     * EVDO_Bam context 上下文
     *
     * @return false：当前网络的具体类型是否是GPRS。false：当前没有网络连接或者具体类型不是GPRS
     */
    public static boolean isGPRS() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_GPRS;
    }

    /**
     * 判断当前网络的具体类型是否是LTE
     *
     * @return false：当前网络的具体类型是否是LTE。false：当前没有网络连接或者具体类型不是LTE
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static boolean isLTE() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return false;
        } else {
            return getCurrentNetworkSubtype() ==
                    TelephonyManager.NETWORK_TYPE_LTE;
        }
    }

    /**
     * 获取Wifi的状态，需要ACCESS_WIFI_STATE权限
     *
     * @return 取值为WifiManager中的WIFI_STATE_ENABLED、WIFI_STATE_ENABLING、WIFI_STATE_DISABLED、WIFI_STATE_DISABLING、WIFI_STATE_UNKNOWN之一
     * @throws Exception 没有找到wifi设备
     */
    public static int getWifiState() throws Exception {
        WifiManager wifiManager = ((WifiManager) ContextUtils.appContext.getSystemService(
                Context.WIFI_SERVICE));
        if (wifiManager != null) {
            return wifiManager.getWifiState();
        } else {
            throw new Exception("wifi device not found!");
        }
    }


    /**
     * 判断Wifi是否打开，需要ACCESS_WIFI_STATE权限
     *
     * @return true：打开；false：关闭
     */
    public static boolean isWifiOpen() throws Exception {
        int wifiState = getWifiState();
        return wifiState == WifiManager.WIFI_STATE_ENABLED ||
                wifiState == WifiManager.WIFI_STATE_ENABLING
                ? true
                : false;
    }

    /**
     * 设置Wifi，需要CHANGE_WIFI_STATE权限
     *
     * @param enable wifi状态
     * @return 设置是否成功
     */
    public static boolean setWifi(boolean enable)
            throws Exception {
        //如果当前wifi的状态和要设置的状态不一样
        if (isWifiOpen() != enable) {
            ((WifiManager) ContextUtils.appContext.getSystemService(
                    Context.WIFI_SERVICE)).setWifiEnabled(enable);
        }
        return true;
    }

    /**
     * 判断移动网络是否打开，需要ACCESS_NETWORK_STATE权限
     *
     * @return true：打开；false：关闭
     */
    public static boolean isMobileNetworkOpen() {
        return (((ConnectivityManager) ContextUtils.appContext.getSystemService(
                Context.CONNECTIVITY_SERVICE)).getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE)).isConnected();
    }

    /**
     * 获取本机IP地址
     *
     * @return null：没有网络连接
     */
    public static String getIpAddress() {
        try {
            NetworkInterface nerworkInterface;
            InetAddress inetAddress;
            for (Enumeration<NetworkInterface> en
                 = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                nerworkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr
                     = nerworkInterface.getInetAddresses();
                     enumIpAddr.hasMoreElements(); ) {
                    inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
            return null;
        } catch (SocketException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
