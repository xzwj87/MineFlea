package com.github.xzwj87.mineflea.utils;

import android.text.TextUtils;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.tencent.qc.stat.common.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jason on 10/14/16.
 */

public class UserInfoUtils {

    private static final int MIN_PWD_LEN = 6;
    private static final int TEL_NUMBER_LEN = 11;

    private static final String VALIDATE_EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@" +
            "(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static final String VALIDATE_TEL_REGEX = "^\\d[1-9]{1}\\d[0-9]{10}";

    public static boolean isEmailValid(String email) {

        return !TextUtils.isEmpty(email) && email.contains("@") && isEmail(email);
    }

    public static boolean isPossibleEmail(String account){
        return !TextUtils.isEmpty(account) && account.contains("@");
    }

    private static boolean isEmail(String account){
        Pattern pattern = Pattern.compile(VALIDATE_EMAIL_REGEX);
        Matcher matcher = pattern.matcher(account);

        return matcher.matches();
    }

    public static boolean isPasswordValid(String password) {
        if(TextUtils.isEmpty(password)) return false;

        return password.length() >= MIN_PWD_LEN;
    }

    public static boolean isNameValid(String name){
        return !TextUtils.isEmpty(name);
    }

    public static boolean isTelNumberValid(String tel){
        return !TextUtils.isEmpty(tel) && tel.length() == TEL_NUMBER_LEN
                && isTelNumber(tel);
    }

    public static boolean isTelNumber(String account){
        Pattern pattern = Pattern.compile(VALIDATE_TEL_REGEX);
        Matcher matcher = pattern.matcher(account);

        return matcher.matches();
    }

    @SuppressWarnings("unchecked")
    public static UserInfo fromAvUser(AVUser avUser){
        if(avUser == null) return null;

        UserInfo userInfo = new UserInfo();

        userInfo.setUserId(avUser.getObjectId());
        userInfo.setUserName(avUser.getUsername());
        userInfo.setUerEmail(avUser.getEmail());
        userInfo.setUserTelNumber(avUser.getMobilePhoneNumber());

        userInfo.setNickName((String)avUser.get(UserInfo.USER_NICK_NAME));
        userInfo.setHeadIconUrl((String)avUser.get(UserInfo.USER_HEAD_ICON));
        userInfo.setLocation((String)avUser.get(UserInfo.USER_LOCATION));
        userInfo.setIntro((String)avUser.get(UserInfo.USER_INTRO));
        userInfo.setGoodsList(avUser.getList(UserInfo.PUBLISHED_GOODS));
        userInfo.setFolloweeList(avUser.getList(UserInfo.USER_FOLLOWEES));
        userInfo.setFollowerList(avUser.getList(UserInfo.USER_FOLLOWERS));
        return userInfo;
    }

    public static AVUser toAvUser(UserInfo userInfo){
        if(userInfo == null) return null;

        AVUser avUser = new AVUser();

        if(!TextUtils.isEmpty(userInfo.getUserId())){
            avUser.setObjectId(userInfo.getUserId());
        }
        avUser.setUsername(userInfo.getUserEmail());
        avUser.setEmail(userInfo.getUserEmail());
        avUser.setMobilePhoneNumber(userInfo.getUserTelNumber());
        avUser.setPassword(userInfo.getUserPwd());

        avUser.put(UserInfo.USER_NICK_NAME,userInfo.getNickName());
        avUser.put(UserInfo.USER_HEAD_ICON,userInfo.getHeadIconUrl());
        avUser.put(UserInfo.USER_LOCATION,userInfo.getLocation());
        avUser.put(UserInfo.USER_FOLLOWERS,userInfo.getFollowerList());
        avUser.put(UserInfo.USER_FOLLOWEES,userInfo.getFolloweeList());
        avUser.put(UserInfo.USER_INTRO,userInfo.getIntro());
        avUser.put(UserInfo.PUBLISHED_GOODS,userInfo.getGoodsList());


        return avUser;
    }
}
