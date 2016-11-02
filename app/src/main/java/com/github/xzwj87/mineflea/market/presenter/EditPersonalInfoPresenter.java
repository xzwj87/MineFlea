package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

/**
 * Created by jason on 11/2/16.
 */

public abstract class EditPersonalInfoPresenter implements BasePresenter,PresenterCallback{

    public abstract void setNickName(String nickName);
    public abstract void setHeadIcon(String iconUrl);
    public abstract void setEmail(String email);
    public abstract void setTelNumber(String telNumber);
    public abstract void setIntro(String intro);

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
    public void onGetUserFollowListDone(Message message) {

    }

    @Override
    public void onGetUserFolloweeDone(Message message) {

    }

    @Override
    public void onGetUserFollowerDone(Message message) {

    }
}
