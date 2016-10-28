package com.github.xzwj87.mineflea.market.model;

import java.util.ArrayList;
import java.util.Date;
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

    public static final String USER_ID = "user_id";

    public static final String IS_LOGIN = "is_login";

    public static final String USER_NICK_NAME = "nick_name";

    public static final String UER_EMAIL = "user_email";

    public static final String USER_PWD = "user_password";
    // registered user name
    public static final String USER_NAME = "user_name";

    public static final String USER_TEL = "user_tel";

    public static final String USER_HEAD_ICON = "head_icon";

    public static final String USER_FOLLOWERS = "followers";

    public static final String USER_FOLLOWEES = "followees";

    public static final String USER_LOCATION = "location";

    public static final String USER_LOGIN_DATE = "login_date";

    public static final String CURRENT_USER = "current_user";

    private String mId;
    private boolean mIsLogin;
    private String mNickName;
    private String mUserName;
    private String mUserEmail;
    private String mTelNumber;
    private String mUserPwd;
    private String mHeadIconUrl;
    private int mFollowers;
    private int mFollowees;
    private String mLoc;
    private Date mLoginDate;

    public void setUserId(String id){
        mId = id;
    }

    public String getUserId(){ return mId;}

    public void setLoginState(boolean isLogin){
        mIsLogin = isLogin;
    }

    public boolean getLoginState(){
        return mIsLogin;
    }

    public void setNickName(String nickName){
        mNickName = nickName;
    }

    public String getNickName(){
        return mNickName;
    }

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

    public void setHeadIconUrl(String url){
        mHeadIconUrl = url;
    }

    public String getHeadIconUrl(){
        return mHeadIconUrl;
    }

    public void setFollowers(int followers){
        mFollowers = followers;
    }

    public int getFollowers(){
        return mFollowers;
    }

    public void setFollowees(int followees){
        mFollowees = followees;
    }

    public int getFollowees(){
        return mFollowees;
    }

    public void setLocation(String location){
        mLoc = location;
    }

    public String getLocation(){
        return mLoc;
    }

    public void setLoginDate(Date date){
        mLoginDate = date;
    }

    public Date getLoginDate(){
        return mLoginDate;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder(4);

        builder.append("\nNick Name = " + mNickName + "\n")
               .append("Email = " + mUserEmail + "\n")
               .append("Telephone number " + mTelNumber + "\n")
               .append("Head icon url = " + mHeadIconUrl + "\n");

        return builder.toString();
    }

    private void setDefaultValue(){
        mIsLogin = false;
        mLoc = "xxxx";
    }
}
