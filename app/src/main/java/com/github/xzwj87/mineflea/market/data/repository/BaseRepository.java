package com.github.xzwj87.mineflea.market.data.repository;

import android.os.Message;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.PresenterCallback;

import java.util.List;

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

    /*
     * Callback of Presenter:this must be called in Presenter
     */
    void setPresenterCallback(PresenterCallback callback);

    /*
     * upload image
     */
    void uploadImage(String imgUri,boolean showProcess);

    /*
     * get current user id
     */
    String getCurrentUserId();

    /*
     * get current user information
     */
    UserInfo getCurrentUser();

    /*
     * get user info by id
     */
    void getUserInfoById(String id);

    /*
     * get goods list by user id
     */
    void getGoodsListByUserId(String id);

    /*
     * query favorite goods list by user id
     */
    void queryFavorGoodsListByUserId(String id);

    /*
     * query user follower by id
     */
    void queryUserFollowerListByUserId(String id);

    /*
     * query user followee by id
     */
    void queryUserFolloweeListByUserId(String id);
}
