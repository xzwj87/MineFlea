package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.model.UserFollowInfo;

/**
 * Created by jason on 10/31/16.
 */

public abstract class UserFolloweePresenter implements BasePresenter{
    public abstract int getUserFollowCount();

    public abstract UserFollowInfo getUserFollowAtPos(int pos);

    public abstract void getUserFollowList(String userId);
}
