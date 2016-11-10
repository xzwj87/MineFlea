package com.github.xzwj87.mineflea.market.ui;

import android.support.annotation.IntDef;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by jason on 10/22/16.
 */

public interface UserDetailView extends BaseView{
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({IS_ME,ALREADY_FOLLOW,NOT_FOLLOW})
    @interface ACTION_STATE{}
    int IS_ME = 0x01;
    int ALREADY_FOLLOW = 0x02;
    int NOT_FOLLOW = 0x03;

    void showLoadProcess(boolean stop);

    void showGetInfoFailMsg();

    void renderHeadIcon(String iconUrl);

    void renderNickName(String name);

    void renderEmail(String email);

    void updateActionButton(@ACTION_STATE int state);
}
