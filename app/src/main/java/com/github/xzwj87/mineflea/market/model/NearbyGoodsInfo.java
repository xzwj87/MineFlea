package com.github.xzwj87.mineflea.market.model;

import com.amap.api.maps2d.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhai on 2016/10/15.
 */

public class NearbyGoodsInfo {
    private LatLng latlng;
    private int imgId;
    private String name;
    private String distance;
    private int zan;

    public static List<NearbyGoodsInfo> infos = new ArrayList<>();

    public NearbyGoodsInfo(LatLng latlng, int imgId, String name, String distance, int zan) {
        this.latlng = latlng;
        this.imgId = imgId;
        this.name = name;
        this.distance = distance;
        this.zan = zan;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    @Override
    public String toString() {
        return "NearbyGoodsInfo{" +
                "latlng=" + latlng +
                ", imgId=" + imgId +
                ", name='" + name + '\'' +
                ", distance='" + distance + '\'' +
                ", zan=" + zan +
                '}';
    }
}
