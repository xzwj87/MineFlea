package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.model.UserGoodsInfo;

/**
 * Created by jason on 10/30/16.
 */

public abstract class UserFavorGoodsPresenter implements BasePresenter,PresenterCallback{

    public abstract void getFavorGoodsList(String id);
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

    @Override
    public void onGetUserFollowListDone(Message message) {

    }

    @Override
    public void onGetUserFolloweeDone(Message message) {

    }

    @Override
    public void onGetUserFollowerDone(Message message) {

    }
}
