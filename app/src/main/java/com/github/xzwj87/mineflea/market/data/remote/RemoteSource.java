package com.github.xzwj87.mineflea.market.data.remote;

import com.github.xzwj87.mineflea.market.data.DataSource;
import com.github.xzwj87.mineflea.market.model.UserInfo;

/**
 * Created by jason on 10/24/16.
 */

public interface RemoteSource extends DataSource {
    void getUserInfoById(String id);
    void getGoodsListByUserId(String id);
    String getCurrentUserId();
    UserInfo getCurrentUser();
}
