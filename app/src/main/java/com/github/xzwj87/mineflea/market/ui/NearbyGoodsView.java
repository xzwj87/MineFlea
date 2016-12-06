package com.github.xzwj87.mineflea.market.ui;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;

import java.util.List;

/**
 * Created by jason on 9/28/16.
 */

public interface NearbyGoodsView extends BaseView{
    void updateMarkerDisplay(List<PublishGoodsInfo> list);
}
