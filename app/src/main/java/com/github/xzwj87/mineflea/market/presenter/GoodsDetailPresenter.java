package com.github.xzwj87.mineflea.market.presenter;

/**
 * Created by jason on 12/1/16.
 */

public interface GoodsDetailPresenter extends BasePresenter{

    void getGoodsInfo(String goodsId);

    void addToFavorites();

    String getPublisherId();
}
