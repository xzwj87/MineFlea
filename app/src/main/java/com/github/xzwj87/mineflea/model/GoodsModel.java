package com.github.xzwj87.mineflea.model;

import android.support.v7.widget.LinearLayoutCompat;

import java.io.IOException;
import java.util.Date;

/**
 * Created by JasonWang on 2016/9/22.
 */
public class GoodsModel {

    private long mId;

    private String mName;

    private long mPublisherId;

    private double mDepreciationRate;

    private double mHighPrice;

    private double mLowPrice;

    private Date mReleasedDate;

    private int mStars;

    private String mImageUri;

    public GoodsModel(){
    }

    public long getId(){
        return mId;
    }

    public void setId(long id){
        mId = id;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
        mName = name;
    }

    public double getDepreciationRate(){
        return mDepreciationRate;
    }

    public void setDepreciationRate(double rate){
        mDepreciationRate = rate;
    }

    public long getPublisherId(){
        return mPublisherId;
    }

    public void setPublisherId(long publisherId){
        mPublisherId = publisherId;
    }

    public double getHighPrice(){
        return mHighPrice;
    }

    public void setHighPrice(double price){
        mHighPrice = price;
    }

    public double getLowerPrice(){
        return mLowPrice;
    }

    public void setLowerPrice(double price){
        mLowPrice = price;
    }

    public Date getReleasedDate(){
        return mReleasedDate;
    }

    public void setReleasedDate(Date date){
        mReleasedDate = date;
    }

    public int getStars(){
        return mStars;
    }

    public void setStars(int stars){
        mStars = stars;
    }

    public String getImageUri(){
        return mImageUri;
    }

    public void setImageUri(String uri){
        mImageUri = uri;
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("-------- Goods Detail --------\n");
        sb.append("id = " + mId + "\n");
        sb.append("name = " + mName + "\n");
        sb.append("high price = " + mHighPrice + "\n");
        sb.append("lower price = " + mLowPrice + "\n");
        sb.append("released date = " + mReleasedDate + "\n");
        sb.append("stars = " + mStars + "\n");
        sb.append("-------------------------------\n");

        return sb.toString();
    }
}
