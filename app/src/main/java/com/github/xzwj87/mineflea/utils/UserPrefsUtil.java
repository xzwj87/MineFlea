package com.github.xzwj87.mineflea.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.avos.avoscloud.AVUser;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.model.PublisherInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.tencent.qc.stat.common.User;

/**
 * Created by jason on 10/6/16.
 */

public class UserPrefsUtil {

    private static final String USER_INFO = "user_info";


    private static SharedPreferences prefs = AppGlobals.getAppContext().
            getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);

    public static boolean isLogin(){
        return getBoolean(UserInfo.IS_LOGIN,false);
    }

    public static boolean getBoolean(String key,boolean defaultVal){
        return prefs.getBoolean(key,defaultVal);
    }

    public static void setLoginState(boolean state){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(UserInfo.IS_LOGIN,state);

        editor.apply();
    }

    public static String getString(String key, String defaultVal){

        return prefs.getString(key,defaultVal);
    }

    public static void updateUserInfoBoolean(String key, boolean val){
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(key,val);
        editor.apply();
    }

    public static void updateUserInfoString(String key, String val){
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(key,val);
        editor.apply();
    }

    public static void saveUserLoginInfo(UserInfo info){
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(UserInfo.IS_LOGIN,info.getLoginState());
        editor.putString(UserInfo.USER_NICK_NAME,info.getNickName());
        editor.putString(UserInfo.USER_NAME,info.getUserName());
        editor.putString(UserInfo.UER_EMAIL,info.getUserEmail());
        editor.putString(UserInfo.USER_PWD,info.getUserPwd());
        editor.putString(UserInfo.USER_HEAD_ICON,info.getHeadIconUrl());

        editor.apply();
    }

    public static void saveUserLoginInfo(AVUser avUser){
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(UserInfo.IS_LOGIN,true);
        editor.putString(UserInfo.USER_NICK_NAME,(String)avUser.get(UserInfo.USER_NICK_NAME));
        editor.putString(UserInfo.USER_NAME,avUser.getUsername());
        editor.putString(UserInfo.UER_EMAIL,avUser.getEmail());
        editor.putString(UserInfo.USER_HEAD_ICON,(String)avUser.get(UserInfo.USER_HEAD_ICON));

        editor.apply();
    }

    public static UserInfo getUserLoginInfo(){
        UserInfo loginInfo = new UserInfo();

        boolean loginState = prefs.getBoolean(UserInfo.IS_LOGIN,false);
        String name = prefs.getString(UserInfo.USER_NAME,"null");
        String email = prefs.getString(UserInfo.UER_EMAIL,"null");
        String pwd = prefs.getString(UserInfo.USER_PWD,"null");

        loginInfo.setLoginState(loginState);
        loginInfo.setUserName(name);
        loginInfo.setUerEmail(email);
        loginInfo.setUserPwd(pwd);

        return loginInfo;
    }
}
