package com.github.xzwj87.mineflea.market.ui;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;

import java.util.List;

/**
 * Created by jason on 10/30/16.
 */

public interface UserFavoriteView extends BaseView{
    void showProgress(boolean show);
    void showBlankPage();
    void renderView();
}
