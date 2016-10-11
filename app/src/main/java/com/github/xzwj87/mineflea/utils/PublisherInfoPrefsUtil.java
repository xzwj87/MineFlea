package com.github.xzwj87.mineflea.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.model.PublisherInfo;

/**
 * Created by jason on 10/6/16.
 */

public class PublisherInfoPrefsUtil {

    private static String USER_INFO = "user_info";

    private static SharedPreferences prefs = AppGlobals.getAppContext().
            getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);


    public static void saveUserInfo(PublisherInfo info){

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PublisherInfo.USER_NAME,info.getName());
        editor.putString(PublisherInfo.TOKEN,info.getToken());
        editor.apply();
    }

    public static PublisherInfo getPublisherInfo() {
        PublisherInfo user = new PublisherInfo();

        String name = prefs.getString(PublisherInfo.USER_NAME,"");
        String token = prefs.getString(PublisherInfo.TOKEN,"");
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(token)){
            return user;
        }

        return null;
    }
}
