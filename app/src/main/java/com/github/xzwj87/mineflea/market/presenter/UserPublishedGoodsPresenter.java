package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.model.UserGoodsInfo;

/**
 * Created by jason on 10/27/16.
 */

public abstract class UserPublishedGoodsPresenter
        implements BasePresenter, PresenterCallback{

    public abstract void getGoodsListByUserId(String userId);
    public abstract int getGoodsCount();
    public abstract UserGoodsInfo getGoodsAtPos(int pos);

    @Override
    public void loginComplete(Message message) {

    }

    @Override
    public void onPublishComplete(Message message) {

    }

    @Override
    public void onRegisterComplete(Message message) {

    }

    @Override
    public void updateUploadProcess(int count) {

    }

    @Override
    public void onImgUploadComplete(Message message) {

    }

    @Override
    public void onGetUserInfoComplete(Message message) {

    }

}
