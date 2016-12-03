package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

/**
 * Created by jason on 10/14/16.
 */

public interface  RegisterPresenter extends BasePresenter{

    void setUserNickName(String name);

    void setUserEmail(String email);

    void setUserPwd(String pwd);

    void setUserIconUrl(String url);

    void setSmsAuthCode(String authCode);

    boolean validUserInfo();

    String getUserIconUrl();

    void getSmsAuthCode(String telNumber);

    void signUpBySms();

    void updateUserInfo();
}
