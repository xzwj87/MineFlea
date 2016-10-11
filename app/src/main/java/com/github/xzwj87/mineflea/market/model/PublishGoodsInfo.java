package com.github.xzwj87.mineflea.market.model;

import java.util.Date;
import java.util.List;

/**
 * Created by JasonWang on 2016/9/22.
 */
public class PublishGoodsInfo {

    public static final String GOODS_NAME = "name";
    public static final String GOODS_PUBLISHER = "goods_publisher";
    public static final String GOODS_LOW_PRICE = "low_Price";
    public static final String GOODS_HIGH_PRICE = "high_price";
    public static final String GOODS_RELEASE_DATE = "release_date";

    private String mId;

    private String mName;

    private String mPublisher;

    private double mHighPrice;

    private double mLowPrice;

    private String mNote;

    private long mReleasedDate;

    private List<String> mImageUri;

    public PublishGoodsInfo(){
        mReleasedDate = System.currentTimeMillis();
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


    public String getPublisher(){
        return mPublisher;
    }

    public void setPublisher(String publisher){
        mPublisher = publisher;
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

    public void setNote(String note){
        mNote = note;
    }

    public String getNote(){
        return mNote;
    }

    public long getReleasedDate(){
        return mReleasedDate;
    }

    public void setReleasedDate(long date){
        mReleasedDate = date;
    }

    public List<String> getImageUri(){
        return mImageUri;
    }

    public void setImageUri(List<String> uri){
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
        sb.append("released date = " + new Date(mReleasedDate) + "\n");
        sb.append("-------------------------------\n");

        return sb.toString();
    }
}
