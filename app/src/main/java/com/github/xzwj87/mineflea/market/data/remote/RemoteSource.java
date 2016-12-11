package com.github.xzwj87.mineflea.market.data.remote;

import com.github.xzwj87.mineflea.market.data.DataSource;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;

import java.util.List;

/**
 * Created by jason on 10/24/16.
 */

public interface RemoteSource extends DataSource {

    // only get 30 items a time
    int MAX_GOODS_TO_GET_ONE_TIME = 30;

    void getUserInfoById(String id);

    void getGoodsListByUserId(String id);

    void getGoodsById(String id);

    String getCurrentUserId();

    UserInfo getCurrentUser();

    void updateCurrentUserInfo(String key, String val);

    void follow(String userId);

    void unFollow(String userId);

    void getMyFollowee(String userId);

    void getFolloweeList(String userId);

    void getFollowerList(String userId);

    void getAllGoods();

    void loginBySms(String telNumber,String smsCode);

    void registerBySms(String telNumber, String smsCode);

    void sendAuthCode(String number);

    void sendResetPwdEmail(String emailAddress);

    void sendResetPwdBySms(String telNumber);

    void resetPwdBySms(String authCode,String newPwd);

    void uploadImg(String imgUrl, boolean showProcess);

    void uploadImg(List<String> imgList, boolean showProcess);
}
