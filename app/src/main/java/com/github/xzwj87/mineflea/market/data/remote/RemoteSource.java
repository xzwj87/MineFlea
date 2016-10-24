package com.github.xzwj87.mineflea.market.data.remote;

import com.github.xzwj87.mineflea.market.data.DataSource;

/**
 * Created by jason on 10/24/16.
 */

public interface RemoteSource extends DataSource {
    void getUserInfo(String id);
    void getGoodsListByUserId(String id);
    String getCurrentUserId();
}
