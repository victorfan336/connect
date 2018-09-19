package com.victor.remotedebug;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fanwentao
 * @Description
 * @date 2018/9/19
 */
public class NetUtils {

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo;
        try {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return networkInfo != null && networkInfo.isConnected()
                && networkInfo.getState() == NetworkInfo.State.CONNECTED;
    }

    /**
     * @return 判断网络是否可用，并返回网络类型，ConnectivityManager.TYPE_WIFI，
     * ConnectivityManager.TYPE_MOBILE，ConnectivityManager.TYPE_ETHERNET,不可用返回-1
     */
    public static final int getNetWorkConnectionType(Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        final NetworkInfo mInternetNetWorkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);

        if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) {
            return ConnectivityManager.TYPE_WIFI;
        } else if (mobileNetworkInfo != null && mobileNetworkInfo.isConnected()) {
            return ConnectivityManager.TYPE_MOBILE;
        } else if (mInternetNetWorkInfo != null && mInternetNetWorkInfo.isConnected()) {
            return ConnectivityManager.TYPE_ETHERNET;
        } else {
            return -1;
        }
    }

    /**
     * 获取wifi ip
     *
     * @param context
     * @return
     */
    public static String getWifiIP(Context context) {
        String ip = null;
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            ip = "" + (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                    + "." + (i >> 24 & 0xFF);
        }
        return ip;
    }

    /**
     * 获取移动数据网络ip
     *
     * @return
     */
    public static String getMobileIP(Context context) {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * 获取网络ip，条件：Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
     * @param context
     * @return
     */
    public static String getIp(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            Network network = connectivityManager.getActiveNetwork();

            LinkProperties properties = connectivityManager.getLinkProperties(network);
            if (properties != null) {
                List<LinkAddress> linkAddresses = properties.getLinkAddresses();
                final String tag = "\\d+.\\d+.\\d+.\\d+";
                for (LinkAddress address : linkAddresses) {
                    String ipString = address.getAddress().toString();
                    final Pattern pattern = Pattern.compile(tag);
                    Matcher matcher = pattern.matcher(ipString);
                    if (matcher.find()) {
                        String ip = matcher.group();
                        Log.e("hehe", "ip = " + ip);
                        return ip;
                    }
                }
            }
        }
        return "";
    }

}
