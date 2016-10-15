package com.github.xzwj87.mineflea.market.presenter;

/**
 * Created by jason on 10/14/16.
 */

public interface RegisterPresenter extends BasePresenter{
    void register();
    void setUserName(String name);
    void setUserEmail(String email);
    void setUserTel(String tel);
    void setUserPwd(String pwd);
    boolean validUserInfo();
}
