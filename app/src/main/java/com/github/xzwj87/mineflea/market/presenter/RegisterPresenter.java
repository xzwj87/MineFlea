package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

/**
 * Created by jason on 10/14/16.
 */

public abstract class RegisterPresenter implements BasePresenter,PresenterCallback{
    public abstract void register();
    public abstract void onImgUploadComplete(Message msg);
    public abstract void setUserNickName(String name);
    public abstract void setUserEmail(String email);
    public abstract void setUserTel(String tel);
    public abstract void setUserPwd(String pwd);
    public abstract void setUserIconUrl(String url);
    public abstract boolean validUserInfo();
    public abstract String getUserIconUrl();

    public abstract void onRegisterComplete(Message message);

    public void loginComplete(Message message){
        throw new UnsupportedOperationException("not supported operation");
    }

    public void onPublishComplete(Message message){
        throw new UnsupportedOperationException("not supported operation");
    }

    public void updateUploadProcess(int count){
        throw new UnsupportedOperationException("not supported operation");
    }

    public void onGetUserInfoComplete(Message message){
        throw new UnsupportedOperationException("not supported operation");
    }

    public void onGetGoodsListDone(Message message){
        throw new UnsupportedOperationException("not supported operation");
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
