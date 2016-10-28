package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.model.UserInfo;

/**
 * Created by jason on 10/17/16.
 */

public interface PresenterCallback {
    void loginComplete(Message message);
    void onPublishComplete(Message message);
    void onRegisterComplete(Message message);
    void updateUploadProcess(int count);
    void onImgUploadComplete(Message message);
    void onGetUserInfoComplete(Message message);
    void onGetGoodsListDone(Message message);

}
