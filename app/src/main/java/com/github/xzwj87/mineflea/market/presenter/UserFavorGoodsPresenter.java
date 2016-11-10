package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.model.UserGoodsInfo;

/**
 * Created by jason on 10/30/16.
 */

public abstract class UserFavorGoodsPresenter implements BasePresenter{
    public abstract void getFavorGoodsList(String id);

    public abstract int getGoodsCount();

    public abstract UserGoodsInfo getGoodsAtPos(int pos);
}
