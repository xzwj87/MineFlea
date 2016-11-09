package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;
import android.util.Log;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;

import java.util.List;

/**
 * Created by jason on 10/22/16.
 */

public abstract class UserDetailPresenter implements BasePresenter{
    public abstract void getUserInfoById(String id);

    public abstract void follow(String userId);

    public abstract void unFollow(String userId);

    public abstract boolean isMyFollowee(String userId);

    public abstract void getGoodsList(String userId);

    public abstract String getCurrentUserId();

    public abstract List<PublishGoodsInfo> getGoodsList();

    public abstract boolean isMe();

    public abstract int getGoodsNumber();
}
