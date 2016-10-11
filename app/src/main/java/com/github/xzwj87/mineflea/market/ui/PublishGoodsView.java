package com.github.xzwj87.mineflea.market.ui;

import com.github.xzwj87.mineflea.market.data.RepoResponseCode;

/**
 * Created by jason on 9/28/16.
 */

public interface PublishGoodsView {

    void publishGoods();
    void onPublishComplete(RepoResponseCode responseCode);
    void finishView();
}
