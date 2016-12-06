package com.github.xzwj87.mineflea.market.ui;

import android.os.Message;

import butterknife.BindView;

/**
 * Created by jason on 10/14/16.
 */

public abstract class RegisterView implements BaseView{
    public void onLoginBySmsComplete(boolean success){}

    public void onRegisterComplete(boolean success){}

    public void showNameInvalidMsg(){}

    public void showEmailInvalidMsg(){}

    //void showTelInvalidMsg();

    public void showPwdInvalidMsg(){}

    public void showHeadIconNullMsg(){}
}
