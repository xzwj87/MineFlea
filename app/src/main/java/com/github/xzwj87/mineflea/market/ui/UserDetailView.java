package com.github.xzwj87.mineflea.market.ui;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;

import java.util.List;

/**
 * Created by jason on 10/22/16.
 */

public interface UserDetailView extends BaseView{
    void showLoadProcess(boolean stop);
    void onGetUserInfoDone(boolean success);

    void renderHeadIcon(String iconUrl);

    void renderNickName(String name);

    void renderEmail(String email);

    void updateActionButton(boolean isFollowee);
}
