package com.github.xzwj87.mineflea.market.ui;

import com.github.xzwj87.mineflea.market.model.UserInfo;

import java.util.List;

/**
 * Created by jason on 12/1/16.
 */

public interface GoodsDetailView extends BaseView{
    void onGetGoodsInfoDone(boolean success);

    void updateImageListPage(List<String> imgList);

    void updateGoodsName(String name);

    void updateGoodsPrice(double price);

    void updateGoodsLocation(String location);

    void updateLikes(int likes);

    void updateUserInfo(UserInfo userInfo);
}
