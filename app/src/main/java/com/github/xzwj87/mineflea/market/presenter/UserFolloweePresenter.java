package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.model.UserFollowInfo;

/**
 * Created by jason on 10/31/16.
 */

public abstract class UserFolloweePresenter implements BasePresenter,PresenterCallback{

    public abstract int getUserFollowCount();
    public abstract UserFollowInfo getUserFollowAtPos(int pos);
    public abstract void getUserFollowList(String userId);

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
    public void onGetGoodsListDone(Message message) {

    }

    @Override
    public void onGetUserFolloweeDone(Message message) {

    }

    @Override
    public void onGetUserFollowerDone(Message message) {

    }
}
