package com.github.xzwj87.mineflea.market.presenter;

import com.github.xzwj87.mineflea.market.model.UserInfo;

/**
 * Created by jason on 10/24/16.
 */

public abstract class UserCenterPresenter implements BasePresenter{
    public abstract void loadUserInfo();
    public abstract String getUserId();
}
