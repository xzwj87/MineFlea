package com.github.xzwj87.mineflea.market.model;

import java.util.Date;
import java.util.List;

/**
 * a model for user published/favorite goods list
 */

public class UserGoodsInfo {

    private String mGoodsName;
    private double mGoodsPrice;
    private int mGoodsLikes;
    private List<String> mGoodsImgUrl;
    private String mReleaseDate;


    public UserGoodsInfo(){
        mReleaseDate = (new Date()).toString();
        mGoodsLikes = 0;
    }

    /**
     * getters
     */
    public String getName(){
        return mGoodsName;
    }

    public double getPrice(){
        return mGoodsPrice;
    }

    public int getLikes(){
        return mGoodsLikes;
    }

    public List<String> getUrlList(){
        return mGoodsImgUrl;
    }

    public String getReleaseDate(){
        return mReleaseDate;
    }

    /**
     * setters
     */

    public void setName(String name){
        mGoodsName = name;
    }

    public void setPrice(double price){
        mGoodsPrice = price;
    }

    public void setLikes(int likes){
        mGoodsLikes = likes;
    }

    public void setGoodsImgUrl(List<String> urls){
        mGoodsImgUrl = urls;
    }

    public void setReleaseDate(String date){
        mReleaseDate = date;
    }

    @Override
    public String toString(){
        return "goods name = " + mGoodsName + "\n" +
                "goods price = " + mGoodsPrice + "\n" +
                "goods likes = " + mGoodsLikes + "\n";
    }

}
