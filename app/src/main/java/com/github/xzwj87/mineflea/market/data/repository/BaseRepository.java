package com.github.xzwj87.mineflea.market.data.repository;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.BasePresenter;
import com.github.xzwj87.mineflea.market.presenter.PresenterCallback;

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
     * login by SMS auth code
     */
    void loginBySms(String telNumber, String pwd);

    /*
     * register by SMS auth code
     */
    void registerBySms(String telNumber,String authCode);

    /*
     * send SMS auth code
     */
    void sendSmsAuthCode(String number);


    /*
     * reset password by SMS code
     */
    void resetPwdBySms(String authCode,String newPwd);


    /*
     * send SMS or email to reset password
     */
    void getAuthCodeByAccount(String account);

    /*
     * logout
     */
    void logout();

    /*
     * upload image by user id or goods id
     */
    void uploadImageById(String id,String imgUri,boolean isUser,boolean showProcess);

    /*
     * get current user id
     */
    String getCurrentUserId();

    /*
     * get current user information
     */
    UserInfo getCurrentUser();

    /*
     * update current user info
     */
    void updateCurrentUserInfo(String key, String val);

    /*
     * add a goods to my favorite list
     */
    void addToMyFavorites(PublishGoodsInfo goodsInfo);

    /*
     * get user info by id
     */
    void getUserInfoById(String id);

    /*
     * get all goods
     */
    void getAllGoods();


    /*
     * get goods info by id
     */
    void getGoodsInfoById(String goodsId);

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

    /*
     * register presenter callback
     */
    void registerCallBack(@BasePresenter.PRESENTER_TYPE String type,PresenterCallback callback);

    /*
     *  unregister presenter callback
     */
    void unregisterCallback(@BasePresenter.PRESENTER_TYPE String type);
}
