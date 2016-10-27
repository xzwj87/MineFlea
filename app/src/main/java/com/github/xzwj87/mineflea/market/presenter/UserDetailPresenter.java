package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;

import java.util.List;

/**
 * Created by jason on 10/22/16.
 */

public abstract class UserDetailPresenter implements BasePresenter,PresenterCallback{
    public abstract void getUserInfoById(String id);

    public abstract void updateFollowers();

    public abstract void getGoodsList(String userId);

    public abstract String getCurrentUserId();

    public abstract List<PublishGoodsInfo> getGoodsList();

    public abstract int getGoodsNumber();

    public abstract boolean isMe(String userId);

    @Override
    public void onRegisterComplete(Message message) {
        throw new UnsupportedOperationException("not supported operation");
    }

    @Override
    public void onImgUploadComplete(Message message) {
        throw new UnsupportedOperationException("not supported operation");
    }

    public void loginComplete(Message message){
        throw new UnsupportedOperationException("not supported operation");
    }

    public void onPublishComplete(Message message){
        throw new UnsupportedOperationException("not supported operation");
    }

    public void updateUploadProcess(int count){
        throw new UnsupportedOperationException("not supported operation");
    }
}
