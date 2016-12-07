package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

/**
 * Created by jason on 11/2/16.
 */

public interface EditPersonalInfoPresenter extends BasePresenter{

    void setNickName(String nickName);

    void setHeadIcon(String iconUrl);

    void setEmail(String email);

    void setTelNumber(String telNumber);

    void setIntro(String intro);

    boolean isEmailVerified();

    boolean isTelVerified();
}
