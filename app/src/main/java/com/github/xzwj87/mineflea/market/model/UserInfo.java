package com.github.xzwj87.mineflea.market.model;

import com.tencent.qc.stat.common.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jason on 10/13/16.
 */

public class UserInfo {
    public static final String USER_ID = "user_id";

    public static final String IS_LOGIN = "is_login";

    public static final String IS_PHONE_VERIFIED = "mobilePhoneVerified";

    public static final String IS_EMAIL_VERIFIED = "emailVerified";

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

    public static final String USER_LOC_DETAIL = "location_detail";

    public static final String CURRENT_USER = "current_user";

    public static final String USER_INTRO = "introduction";

    public static final String PUBLISHED_GOODS = "published_goods";

    public static final String FAVOR_GOODS = "favor_goods";

    private String mId;
    private String mNickName;
    private String mUserName;
    private String mUserEmail;
    private boolean mIsEmailVerified;
    private String mTelNumber;
    private boolean mIsTelVerified;
    private String mUserPwd;
    private String mHeadIconUrl;
    private List<String> mGoodsList;
    private List<String> mFollowerList;
    private List<String> mFolloweeList;
    private List<String> mFavorGoodsList;
    private String mLoc;
    private String mIntro;

    public UserInfo(){
        this(null);
    }

    public UserInfo(String id){
       this(id,null);
    }

    public UserInfo(String id,String nickName){
        this(id,nickName,null);
    }

    public UserInfo(String id,String nickName,String userName){
        this(id,nickName,userName,null);
    }

    public UserInfo(String id,String nickName,String userName,String email){
        mId = id;
        mNickName = nickName;
        mUserEmail = email;
        mUserName = userName;

        mFolloweeList = new ArrayList<>();
        mFollowerList = new ArrayList<>();
        // published goods list
        mGoodsList = new ArrayList<>();
        mFavorGoodsList = new ArrayList<>();

        mIsEmailVerified = false;
        mIsTelVerified = false;
    }

    public void setUserId(String id){
        mId = id;
    }

    public String getUserId(){ return mId;}

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

    public void setEmailVerified(boolean state){
        mIsEmailVerified = state;
    }

    public boolean getEmailVerified(){
        return mIsEmailVerified;
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

    public void setTelVerified(boolean state){
        mIsTelVerified = state;
    }

    public boolean getTelVerified(){
        return mIsTelVerified;
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

    public int getFollowersCount(){
        if(mFollowerList == null) return 0;

        return mFollowerList.size();
    }

    public void setFollowerList(List<String> follower){
        mFollowerList = follower;
    }

    public List<String> getFollowerList(){
        return mFollowerList;
    }

    public void addFollower(String userId){
        if(mFollowerList == null) return;

        mFollowerList.add(userId);
    }

    public void removeFollower(String userId){
        if(mFollowerList == null) return;

        mFollowerList.remove(userId);
    }

    public void setFolloweeList(List<String> followee){
        mFolloweeList = followee;
    }

    public List<String> getFolloweeList(){
        return mFolloweeList;
    }

    public void addFollowee(String userId){
        if(mFolloweeList == null) return;

        mFolloweeList.add(userId);
    }

    public void removeFollowee(String userId){
        if(mFolloweeList == null) return;

        mFolloweeList.remove(userId);
    }

    public void setGoodsList(List<String> goodsList){
        mGoodsList = goodsList;
    }

    public List<String> getGoodsList(){
        return mGoodsList;
    }

    public int getGoodsCount(){
        if(mGoodsList == null) return 0;

        return mGoodsList.size();
    }

    public void addGoods(String goodsId){
        if(mGoodsList == null) return;

        mGoodsList.add(goodsId);
    }

    public void removeGoods(String goodsId){
        if(mGoodsList == null) return;

        mGoodsList.remove(goodsId);
    }

    public void setFavorGoodsList(List<String> favorList){
        mFavorGoodsList = favorList;
    }

    public List<String> getFavorGoodsList(){
        return mFavorGoodsList;
    }

    public int getFavorCount(){
        if(mFavorGoodsList == null){
            return 0;
        }

        return mFavorGoodsList.size();
    }

    public void addFavorGoods(String goodsId){
        mFavorGoodsList.add(goodsId);
    }

    public void removeFavorGoods(String goodsId){
        mFavorGoodsList.remove(goodsId);
    }

    public void setLocation(String location){
        mLoc = location;
    }

    public String getLocation(){
        return mLoc;
    }

    public void setIntro(String intro){
        mIntro = intro;
    }

    public String getIntro(){
        return mIntro;
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
}
