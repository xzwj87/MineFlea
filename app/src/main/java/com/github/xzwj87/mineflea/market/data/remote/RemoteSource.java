package com.github.xzwj87.mineflea.market.data.remote;

import com.github.xzwj87.mineflea.market.data.DataSource;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;

/**
 * Created by jason on 10/24/16.
 */

public interface RemoteSource extends DataSource {
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
}
