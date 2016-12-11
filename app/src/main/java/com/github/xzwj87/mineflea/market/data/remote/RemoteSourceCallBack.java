package com.github.xzwj87.mineflea.market.data.remote;

import android.os.Message;

/**
 * Created by jason on 11/20/16.
 */

public interface RemoteSourceCallBack {
    void publishComplete(Message message);

    void registerComplete(Message message);

    void updateProcess(int count);

    void loginComplete(Message message);

    void onImgUploadComplete(Message msg);

    void onGetUserInfoDone(Message msg);

    void onGetGoodsInfoDone(Message msg);

    void onGetGoodsListDone(Message msg);

    void onGetUserFolloweeDone(Message message);

    void onGetUserFollowerDone(Message message);

    void onResetPwdByEmailDone(Message message);

    void onResetPwdByTelDone(Message message);

    void onResetPwdBySms(Message message);

    void onTelNumberVerifiedComplete(Message msg);
}
