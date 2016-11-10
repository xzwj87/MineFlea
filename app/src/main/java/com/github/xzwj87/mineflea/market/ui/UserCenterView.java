package com.github.xzwj87.mineflea.market.ui;

/**
 * Created by jason on 10/24/16.
 */

public interface UserCenterView extends BaseView{
    void updateUserNickName(String nickName);
    void updateUserEmail(String email);
    void updateHeadIcon(String iconUrl);
    void showNeedLoginHint();
}
