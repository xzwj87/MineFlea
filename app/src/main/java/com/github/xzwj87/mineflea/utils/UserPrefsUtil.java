package com.github.xzwj87.mineflea.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.model.PublisherInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;

/**
 * Created by jason on 10/6/16.
 */

public class UserPrefsUtil {

    private static final String USER_INFO = "user_info";


    private static SharedPreferences prefs = AppGlobals.getAppContext().
            getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);


    public static void savePublisherInfo(PublisherInfo info){

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PublisherInfo.USER_NAME,info.getName());
        editor.putString(PublisherInfo.TOKEN,info.getToken());
        editor.apply();
    }

    public static PublisherInfo getPublisherInfo() {
        PublisherInfo user = new PublisherInfo();

        String name = prefs.getString(PublisherInfo.USER_NAME,"dummy");
        String token = prefs.getString(PublisherInfo.TOKEN,"dummy");
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(token)){
            user.setName(name);
            user.setToken(token);

            return user;
        }

        return null;
    }

    public static void saveUserLoginInfo(UserInfo info){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(UserInfo.USER_NAME,info.getUserName());
        editor.putString(UserInfo.UER_EMAIL,info.getUserEmail());
        editor.putString(UserInfo.USER_PWD,info.getUserPwd());
        editor.putString(UserInfo.USER_HEAD_ICON,info.getHeadIconUrl());

        editor.apply();
    }

    public static UserInfo getUserLoginInfo(){
        UserInfo loginInfo = new UserInfo();

        String name = prefs.getString(UserInfo.USER_NAME,"null");
        String email = prefs.getString(UserInfo.UER_EMAIL,"null");
        String pwd = prefs.getString(UserInfo.USER_PWD,"null");

        loginInfo.setUserName(name);
        loginInfo.setUerEmail(email);
        loginInfo.setUserPwd(pwd);

        return loginInfo;
    }
}
