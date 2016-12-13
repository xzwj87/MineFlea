package com.github.xzwj87.mineflea.market.presenter;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;

/**
 * Created by jason on 11/12/16.
 */

public interface DiscoverGoodsPresenter extends BasePresenter{
    /*
     * get all goods list from data repository
     */
    void getGoodsList();

    /*
     * get goods info at the given position
     */
    PublishGoodsInfo getItemAtPos(int pos);

    /*
     * get the count of goods
     */
    int getItemCount();

    /*
     * add this item to my favorites
     */
    void addToMyFavor(int pos);

    /*
     * get publisher nick name
     */
    String getPublisherNickName(int pos);
}
