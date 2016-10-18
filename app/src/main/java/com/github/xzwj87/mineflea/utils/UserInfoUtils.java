package com.github.xzwj87.mineflea.utils;

import android.text.TextUtils;

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
}
