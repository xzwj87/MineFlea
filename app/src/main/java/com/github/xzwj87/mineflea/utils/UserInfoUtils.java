package com.github.xzwj87.mineflea.utils;

import android.text.TextUtils;

import com.avos.avoscloud.AVUser;
import com.github.xzwj87.mineflea.market.model.UserInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jason on 10/14/16.
 */

public class UserInfoUtils {

    public static boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile(UserInfo.VALIDATE_EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);

        return email.contains("@") || matcher.matches();
    }

    public static boolean isPasswordValid(String password) {
        if(TextUtils.isEmpty(password)) return false;

        return password.length() >= UserInfo.MIN_PWD_LEN;
    }

    public static boolean isNameValid(String name){
        return !TextUtils.isEmpty(name);
    }

    public static boolean isTelNumberValid(String tel){
        return !TextUtils.isEmpty(tel) && tel.length() == UserInfo.TEL_NUMBER_LEN;
    }

    public static UserInfo fromAvUser(AVUser avUser){
        UserInfo userInfo = new UserInfo();

        userInfo.setUserId(avUser.getObjectId());
        userInfo.setUserName(avUser.getUsername());
        userInfo.setUerEmail(avUser.getEmail());
        userInfo.setUserTelNumber(avUser.getMobilePhoneNumber());

        userInfo.setNickName((String)avUser.get(UserInfo.USER_NICK_NAME));
        userInfo.setHeadIconUrl((String)avUser.get(UserInfo.USER_HEAD_ICON));
        userInfo.setLocation((String)avUser.get(UserInfo.USER_LOCATION));
        return userInfo;
    }
}
