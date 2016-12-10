package com.github.xzwj87.mineflea.market.ui;

import android.os.Message;

/**
 * Created by jason on 10/14/16.
 */

public interface LoginView extends BaseView{
    void onLoginSuccess();

    void onLoginFail();

    void showAccountInvalidMsg();

    void showPwdInvalidMsg();

    void showProgress(boolean show);

    void resetPwdSuccess();

    void showEmailResetPwdFailMsg();

    void showSmsResetPwdFailMsg();

    void showNoNetConnectionMsg();
}
