package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.ui.BaseView;

/**
 * presenter to login and reset password if user would like
 */

public interface  LoginPresenter extends BasePresenter{

    void setView(BaseView view);

    void login();

    boolean validLoginInfo();

    void setUserAccount(String account);

    void setUserPwd(String pwd);

    String getUserNickName();

    String getUserEmail();

    String getHeadIconUrl();

    void resetPwdByAccount(String account);
}
