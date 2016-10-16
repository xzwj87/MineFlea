package com.github.xzwj87.mineflea.market.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 10/13/16.
 */

public class UserInfo {
    public static final int MIN_PWD_LEN = 6;
    public static final int TEL_NUMBER_LEN = 11;

    public static final List<String> VALID_EMAIL_DOMAIN_NAME = new ArrayList<>();

    static{
        VALID_EMAIL_DOMAIN_NAME.add("163.com");
        VALID_EMAIL_DOMAIN_NAME.add("126.com");
        VALID_EMAIL_DOMAIN_NAME.add("qq.com");
        VALID_EMAIL_DOMAIN_NAME.add("sina.com");
    }

    public static final String VALIDATE_EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@" +
            "(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public static final String UER_EMAIL = "user_email";

    public static final String USER_PWD = "user_password";
    // registered user name
    public static final String USER_NAME = "user_name";

    public static final String USER_TEL = "user_tel";

    private String mUserName;
    private String mUserEmail;
    private String mTelNumber;
    private String mUserPwd;

    public void setUerEmail(String email){
        mUserEmail = email;
    }

    public String getUserEmail(){
        return mUserEmail;
    }

    public void setUserName(String name){
        mUserName = name;
    }

    public String getUserName(){
        return mUserName;
    }

    public void setUserTelNumber(String tel){
        mTelNumber = tel;
    }

    public String getUserTelNumber(){
        return mTelNumber;
    }

    public void setUserPwd(String pwd){
        mUserPwd = pwd;
    }

    public String getUserPwd(){
        return mUserPwd;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder(3);

        builder.append("User Name = " + mUserName + "\n")
               .append("User Email = " + mUserEmail + "\n")
               .append("User Telephone number " + mTelNumber + "\n");

        return builder.toString();
    }
}