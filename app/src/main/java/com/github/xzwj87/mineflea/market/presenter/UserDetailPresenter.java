package com.github.xzwj87.mineflea.market.presenter;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;

import java.util.List;

/**
 * Created by jason on 10/22/16.
 */

public abstract class UserDetailPresenter implements BasePresenter{
    public abstract UserInfo getUserInfoById(String id);

    public abstract void updateFollowers();

    public abstract List<PublishGoodsInfo> getGoodsList(String userId);
}
