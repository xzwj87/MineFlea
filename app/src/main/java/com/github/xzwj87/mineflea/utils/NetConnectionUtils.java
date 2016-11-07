package com.github.xzwj87.mineflea.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.github.xzwj87.mineflea.app.AppGlobals;

/**
 * Created by jason on 9/25/16.
 */

public class NetConnectionUtils {

    public static boolean isNetworkConnected(){
        Context context = AppGlobals.getAppContext();
        // only wifi connected, we are allowed to sync data
        if(SharePrefsHelper.getInstance(context).getDataSyncState()){
           return false;
        }

        boolean isConnected = false;

        ConnectivityManager netMgr = (ConnectivityManager)context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = netMgr.getActiveNetworkInfo();

        if(netInfo.isConnected()){
            isConnected = true;
        }

        return isConnected;
    }
}
