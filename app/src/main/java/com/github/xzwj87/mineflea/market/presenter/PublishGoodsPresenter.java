package com.github.xzwj87.mineflea.market.presenter;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;

import java.util.List;

/**
 * Created by jason on 9/27/16.
 */

public interface PublishGoodsPresenter extends BasePresenter{
    void publishGoods();

    void setGoodsName(String name);
    void setGoodsLowPrice(double price);
    void setGoodsHighPrice(double price);
    void setGoodsNote(String note);
    void setGoodsImgUrl(List<String> url);
}
