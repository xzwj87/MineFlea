package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

/**
 * Created by jason on 10/14/16.
 */

public interface  RegisterPresenter extends BasePresenter{

    void setUserNickName(String name);

    String getUserNickName();

    void setUserEmail(String email);

    String getUserEmail();

    void setUserPwd(String pwd);

    String getUserPwd();

    void setUserIconUrl(String url);

    String getUserIconUrl();

    void setSmsAuthCode(String authCode);

    String getTelNumber();

    boolean validUserInfo();

    void getSmsAuthCode(String telNumber);

    void signUpBySms();

    void updateUserInfo();
}
