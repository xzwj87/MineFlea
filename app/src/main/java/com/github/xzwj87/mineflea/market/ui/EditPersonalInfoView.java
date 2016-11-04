package com.github.xzwj87.mineflea.market.ui;

/**
 * Created by jason on 11/2/16.
 */

public interface EditPersonalInfoView extends BaseView{
    void updateHeadIcon(String url);
    void updateNickName(String nickName);
    void updateEmail(String email);
    void updateTelNumber(String tel);
    void updateIntro(String intro);

}
