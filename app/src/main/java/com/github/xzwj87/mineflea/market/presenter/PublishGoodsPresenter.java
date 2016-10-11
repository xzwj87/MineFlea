package com.github.xzwj87.mineflea.market.presenter;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;

/**
 * Created by jason on 9/27/16.
 */

public interface PublishGoodsPresenter extends BasePresenter{

    void init();
    void publishGoods(PublishGoodsInfo goods);
}
