package com.github.xzwj87.mineflea.market.ui;

import android.os.Message;

import butterknife.BindView;

/**
 * Created by jason on 10/14/16.
 */

public interface RegisterView extends BaseView{
    void onRegisterComplete(boolean success);
    void showNameInvalidMsg();
    void showEmailInvalidMsg();
    void showTelInvalidMsg();
    void showPwdInvalidMsg();
    void showProgress();
}
