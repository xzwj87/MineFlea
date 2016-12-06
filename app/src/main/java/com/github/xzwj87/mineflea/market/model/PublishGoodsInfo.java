package com.github.xzwj87.mineflea.market.model;

import com.amap.api.maps.model.LatLng;
import com.avos.avoscloud.AVObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JasonWang on 2016/9/22.
 */
public class PublishGoodsInfo {

    public static final int MAX_NAME_SIZE = 30;
    public static final String GOODS_ID = "id";
    public static final String GOODS_NAME = "name";
    public static final String GOODS_PUBLISHER = "publisher_id";
    public static final String GOODS_PRICE = "Price";
    public static final String GOODS_UPDATED_TIME = "updated_time";
    public static final String GOODS_RELEASE_DATE = "release_date";
    public static final String GOODS_LOC = "location";
    public static final String GOODS_LIKES = "likes";
    public static final String GOODS_IMAGES = "images";
    public static final String GOODS_NOTE = "note";
    public static final String GOODS_FAVOR_USER = "favor_user";

    public static final String GOODS_IMG_STRING_SEP = "-";

    private String mId;

    private String mName;

    private String mUserId;

    private double mPrice;

    private String mNote;

    private Date mReleasedDate;

    private Date mUpdateTime;

    private LatLng mLoc;

    private List<String> mImageUri;
    //users who likes me
    private List<String> mFavorUserList;

    public PublishGoodsInfo(){
        this(null);
    }

    public PublishGoodsInfo(String id){
        this(id,null);
    }

    public PublishGoodsInfo(String id,String name){
        this(id,name,null);
    }

    public PublishGoodsInfo(String id,String name,String userId){
        this(id,name,userId,0);
    }

    public PublishGoodsInfo(String id,String name,String userId,double price){
        mId = id;
        mName = name;
        mUserId = userId;
        mPrice = price;

        mReleasedDate = new Date();
        mUpdateTime = new Date();
        mImageUri = new ArrayList<>();
        mFavorUserList = new ArrayList<>();
    }

    public String getId(){
        return mId;
    }

    public void setId(String id){
        mId = id;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
        mName = name;
    }


    public String getUserId(){
        return mUserId;
    }

    public void setUserId(String publisher){
        mUserId = publisher;
    }

    public double getPrice(){
        return mPrice;
    }

    public void setPrice(double price){
        mPrice = price;
    }

    public void setNote(String note){
        mNote = note;
    }

    public String getNote(){
        return mNote;
    }

    public Date getReleasedDate(){
        return mReleasedDate;
    }

    public void setReleasedDate(Date date){
        mReleasedDate = date;
    }

    public Date getUpdateTime(){
        return mUpdateTime;
    }

    public void setUpdateTime(Date time){
        mUpdateTime = time;
    }

    public void setLocation(LatLng location){
        mLoc = location;
    }

    public LatLng getLocation(){
        return mLoc;
    }

    public List<String> getImageUri(){
        return mImageUri;
    }

    public void setImageUri(List<String> uri){
        mImageUri = uri;
    }

    public void setGoodsFavorUser(List<String> userList){
        mFavorUserList = userList;
    }

    public List<String> getFavorUserList(){
        return mFavorUserList;
    }

    public int getStars(){
        if(mFavorUserList == null) return 0;

        return mFavorUserList.size();
    }

    public void addFavorUser(String id){
        if(mFavorUserList == null) return;

        mFavorUserList.add(id);
    }

    public void removeFavorUser(String id){
        if(mFavorUserList == null) return;

        mFavorUserList.remove(id);
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("-------- Goods Detail --------\n");
        sb.append("name = " + mName + "\n");
        sb.append("price = " + mPrice + "\n");
        sb.append("released date = " + mReleasedDate + "\n");
        sb.append("-------------------------------\n");

        return sb.toString();
    }
}
