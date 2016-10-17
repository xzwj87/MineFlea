package com.github.xzwj87.mineflea.market.ui.bean;

/**
 * Created by linhai on 2016/10/16.
 */

public class DiscoverInfo {

    private String des;
    private String date;
    private String imageUrl;
    private String distance;


    public DiscoverInfo(String des, String date, String imageUrl, String distance) {
        this.des = des;
        this.date = date;
        this.imageUrl = imageUrl;
        this.distance = distance;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "DiscoverInfo{" +
                "des='" + des + '\'' +
                ", date='" + date + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }
}
