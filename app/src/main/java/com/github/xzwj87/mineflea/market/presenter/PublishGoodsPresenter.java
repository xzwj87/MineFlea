package com.github.xzwj87.mineflea.market.presenter;


import com.amap.api.maps2d.model.LatLng;

import java.util.List;

/**
 * Created by jason on 9/27/16.
 */

interface PublishGoodsPresenter extends BasePresenter{
    void publishGoods();

    void setGoodsName(String name);

    void setGoodsPrice(String price);

    void setGoodsNote(String note);

    void setLocation(LatLng loc);

    void setLocationDetail(String detail);

    void setGoodsImgUrl(List<String> url);

    void setPublisherId(String name);

    boolean validGoodsInfo();

    String getCurrentUserId();
}
