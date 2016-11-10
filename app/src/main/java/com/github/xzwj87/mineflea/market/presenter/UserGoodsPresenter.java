package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.model.UserGoodsInfo;

/**
 * Created by jason on 10/27/16.
 */

public abstract class UserGoodsPresenter implements BasePresenter{
    public abstract void getGoodsListByUserId(String userId);

    public abstract int getGoodsCount();

    public abstract UserGoodsInfo getGoodsAtPos(int pos);
}
