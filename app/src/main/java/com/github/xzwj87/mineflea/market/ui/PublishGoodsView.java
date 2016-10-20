package com.github.xzwj87.mineflea.market.ui;

/**
 * Created by jason on 9/28/16.
 */

public interface PublishGoodsView extends BaseView{
    void publishGoods();
    void onPublishComplete(boolean success);
    void updateUploadProcess(int count);
}
