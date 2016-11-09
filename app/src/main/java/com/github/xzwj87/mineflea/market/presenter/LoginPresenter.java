package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.ui.BaseView;

/**
 * Created by jason on 10/13/16.
 */

public abstract  class LoginPresenter implements BasePresenter{

    public abstract void setView(BaseView view);

    public abstract void login();

    public abstract boolean validLoginInfo();

    public abstract void setUserAccount(String account);

    public abstract void setUserPwd(String pwd);
}
