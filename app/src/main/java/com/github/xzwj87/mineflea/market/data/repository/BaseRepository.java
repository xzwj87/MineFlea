package com.github.xzwj87.mineflea.market.data.repository;

import android.os.Message;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.callback.LoginCallback;
import com.github.xzwj87.mineflea.market.presenter.callback.PublishCallBack;
import com.github.xzwj87.mineflea.market.presenter.callback.RegisterCallBack;

import java.util.List;

import rx.Observable;

/**
 * Created by jason on 10/11/16.
 */

public interface BaseRepository {
    /**
     * publish a goods
     */
    void publishGoods(PublishGoodsInfo goods);


    /*
     * register user
     */
    void register(UserInfo userInfo);

    /*
     * login
     */
    void login(UserInfo info);

    void setPublishCallback(PublishCallBack callback);
    void setRegisterCallback(RegisterCallBack callback);
    void setLoginCallback(LoginCallback callback);
}
