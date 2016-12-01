package com.github.xzwj87.mineflea.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;

import com.github.xzwj87.mineflea.R;

/**
 * Created by JasonWang on 2016/4/26.
 */
public class SharePrefsHelper {
    public static final String TAG = "SharePrefsHelper";

    private static SharePrefsHelper mInstance;
    private SharedPreferences mSharePref;
    private Resources mResources;


    public static SharePrefsHelper getInstance(Context context){
        if(mInstance == null){
            mInstance = new SharePrefsHelper(context);
        }

        return mInstance;
    }

    private SharePrefsHelper(Context context){
        this.mResources = context.getResources();
        this.mSharePref = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public void updateValue(String key,String val){
        SharedPreferences.Editor editor = mSharePref.edit();
        editor.putString(key,val).apply();
    }

    public void updateLogState(boolean state){
        String key = mResources.getString(R.string.key_login_state);
        boolean current = Boolean.parseBoolean(mSharePref.getString(key,""));
        Log.v(TAG,"current log state: " + String.valueOf(current));
        if(current != state){
            SharedPreferences.Editor editor = mSharePref.edit();
            editor.putString(key,String.valueOf(state));
            editor.apply();
        }
    }

    public boolean getLoginState(){
        String key = mResources.getString(R.string.key_login_state);

        return Boolean.parseBoolean(mSharePref.getString(key,""));
    }

    public boolean getDataSyncState(){
        String key = mResources.getString(R.string.key_pref_wifi_only_sync);

        return mSharePref.getBoolean(key,false);
    }

    public String getThemeColor(){
        String key = mResources.getString(R.string.key_pref_theme_color);
        String blue = mResources.getString(R.string.color_indigo);

        return mSharePref.getString(key,blue);
    }


}
