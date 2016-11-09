package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

/**
 * Created by jason on 11/2/16.
 */

public abstract class EditPersonalInfoPresenter implements BasePresenter{

    public abstract void setNickName(String nickName);
    public abstract void setHeadIcon(String iconUrl);
    public abstract void setEmail(String email);
    public abstract void setTelNumber(String telNumber);
    public abstract void setIntro(String intro);
}
