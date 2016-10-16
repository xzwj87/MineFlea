package com.github.xzwj87.mineflea.market.model;

import com.amap.api.maps2d.model.LatLng;
import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.utils.Constants;

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

    static {
        infos.add(new NearbyGoodsInfo(Constants.BEIJING, R.mipmap.ic_launcher, "英伦贵族小旅馆",
                "距离209米", 1456));
        infos.add(new NearbyGoodsInfo(Constants.CHENGDU, R.mipmap.ic_launcher, "沙井国际洗浴会所",
                "距离897米", 456));
        infos.add(new NearbyGoodsInfo(Constants.FANGHENG, R.mipmap.ic_launcher, "五环服装城",
                "距离249米", 1456));
        infos.add(new NearbyGoodsInfo(Constants.SHANGHAI, R.mipmap.ic_launcher, "老米家泡馍小炒",
                "距离679米", 1456));
        infos.add(new NearbyGoodsInfo(Constants.ZHENGZHOU, R.mipmap.ic_launcher, "老米家泡馍小炒",
                "距离679米", 1456));
        infos.add(new NearbyGoodsInfo(Constants.ZHONGGUANCUN, R.mipmap.ic_launcher, "老米家泡馍小炒",
                "距离679米", 1456));
    }

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
