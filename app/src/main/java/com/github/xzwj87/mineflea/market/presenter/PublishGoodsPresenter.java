package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;

import java.util.List;

/**
 * Created by jason on 9/27/16.
 */

public abstract class PublishGoodsPresenter implements BasePresenter,
                    PresenterCallback{
    public abstract void publishGoods();

    public abstract void setGoodsName(String name);
    public abstract void setGoodsPrice(String price);
    public abstract void setGoodsNote(String note);
    public abstract void setLocation(String loc);
    public abstract void setGoodsImgUrl(List<String> url);
    public abstract void setPublisherName(String name);
    public abstract boolean validGoodsInfo();

    public abstract void onPublishComplete(Message message);


    public void loginComplete(Message message){
        throw new UnsupportedOperationException("not supported operation");
    }

    public void onRegisterComplete(Message message){
        throw new UnsupportedOperationException("not supported operation");
    }

    public void onImgUploadComplete(Message message){
        throw new UnsupportedOperationException("not supported operation");
    }

    public void onGetUserInfoComplete(Message message){
        throw new UnsupportedOperationException("not supported operation");
    }

    public void onGetGoodsListDone(Message message){
        throw new UnsupportedOperationException("not supported operation");
    }
}
