package com.github.xzwj87.mineflea.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.amap.api.maps.model.LatLng;
import com.avos.avoscloud.AVUser;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.model.UserInfo;

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

    public static void updateCurrentLocation(LatLng loc){
        String current = loc.latitude + "-" + loc.longitude;

        updateUserInfoString(UserInfo.USER_LOCATION,current);
    }

    public static LatLng getCurrentLocation(){
        String current = getString(UserInfo.USER_LOCATION,"");

        if(!TextUtils.isEmpty(current)){
            String[] strs = current.split("-");
            LatLng loc = new LatLng(Double.parseDouble(strs[0]),
                    Double.parseDouble(strs[1]));

            return loc;
        }

        return null;
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

        loginInfo.setUserName(name);
        loginInfo.setUerEmail(email);
        loginInfo.setUserPwd(pwd);

        return loginInfo;
    }

    private static final String SP_CONFIG = "config";

    public static void setBooleanPref(Context ctx, String key, boolean flag) {
        SharedPreferences sp = ctx.getSharedPreferences(SP_CONFIG, ctx.MODE_PRIVATE);
        sp.edit().putBoolean(key, flag).commit();
    }

    public static boolean getBooleanPref(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(SP_CONFIG, ctx.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void setBooleanPref(String key, boolean flag) {
        SharedPreferences sp = AppGlobals.getAppContext().getSharedPreferences(SP_CONFIG, AppGlobals.getAppContext().MODE_PRIVATE);
        sp.edit().putBoolean(key, flag).commit();
    }

    public static boolean getBooleanPref(String key) {
        SharedPreferences sp = AppGlobals.getAppContext().getSharedPreferences(SP_CONFIG, AppGlobals.getAppContext().MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void setStringPref(Context ctx, String key, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(SP_CONFIG, ctx.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getStringPref(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(SP_CONFIG, ctx.MODE_PRIVATE);
        return sp.getString(key, null);
    }

    public static void setStringPref(String key, String value) {
        SharedPreferences sp = AppGlobals.getAppContext().getSharedPreferences(SP_CONFIG, AppGlobals.getAppContext().MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getStringPref(String key) {
        SharedPreferences sp = AppGlobals.getAppContext().getSharedPreferences(SP_CONFIG, AppGlobals.getAppContext().MODE_PRIVATE);
        return sp.getString(key, null);
    }

    public static void setIntPref(Context ctx, String key, int value) {
        SharedPreferences sp = ctx.getSharedPreferences(SP_CONFIG, ctx.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getIntPref(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(SP_CONFIG, ctx.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    public static void setIntPref(String key, int value) {
        SharedPreferences sp = AppGlobals.getAppContext().getSharedPreferences(SP_CONFIG, AppGlobals.getAppContext().MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getIntPref(String key) {
        SharedPreferences sp = AppGlobals.getAppContext().getSharedPreferences(SP_CONFIG, AppGlobals.getAppContext().MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    public static void removePref(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(SP_CONFIG, ctx.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    public static void removePref(String key) {
        SharedPreferences sp = AppGlobals.getAppContext().getSharedPreferences(SP_CONFIG, AppGlobals.getAppContext().MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }
}
