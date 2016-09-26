package com.github.xzwj87.mineflea.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.github.xzwj87.mineflea.App.AppGlobals;

/**
 * Created by jason on 9/25/16.
 */

public class NetConnectionUtils {

    public static boolean isNetworkConnected(){
        boolean isConnected = false;

        Context context = AppGlobals.getAppContext();

        ConnectivityManager netMgr = (ConnectivityManager)context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = netMgr.getActiveNetworkInfo();

        if(netInfo.isConnected()){
            isConnected = true;
        }

        return isConnected;
    }
}
