package com.github.xzwj87.mineflea.market.presenter;


import com.amap.api.maps2d.model.LatLng;

import java.util.List;

/**
 * Created by jason on 9/27/16.
 */

public abstract class PublishGoodsPresenter implements BasePresenter{
    public abstract void publishGoods();

    public abstract void setGoodsName(String name);

    public abstract void setGoodsPrice(String price);

    public abstract void setGoodsNote(String note);

    public abstract void setLocation(LatLng loc);

    public abstract void setGoodsImgUrl(List<String> url);

    public abstract void setPublisherName(String name);

    public abstract boolean validGoodsInfo();
}
