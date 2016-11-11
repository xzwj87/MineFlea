package com.github.xzwj87.mineflea.market.presenter;

import android.support.annotation.StringDef;

import com.github.xzwj87.mineflea.market.ui.BaseView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by jason on 9/27/16.
 */

public interface BasePresenter {

    @Retention(SOURCE)
    @StringDef({PRESENTER_EDIT,PRESENTER_LOGIN,PRESENTER_LOGOUT,
            PRESENTER_PUBLISH,PRESENTER_REGISTER,PRESENTER_CENTER,
            PRESENTER_USER_DETAIL,PRESENTER_FAVOR,PRESENTER_FOLLOWEE,
            PRESENTER_FOLLOWER,PRESENTER_GOODS})
    @interface PRESENTER_TYPE{}

    String PRESENTER_EDIT = "edit";
    String PRESENTER_LOGIN = "login";
    String PRESENTER_LOGOUT = "logout";
    String PRESENTER_PUBLISH = "publish";
    String PRESENTER_REGISTER = "register";
    String PRESENTER_CENTER = "center";
    String PRESENTER_USER_DETAIL = "userDetail";
    String PRESENTER_FAVOR = "favor";
    String PRESENTER_FOLLOWEE = "followee";
    String PRESENTER_FOLLOWER = "follower";
    String PRESENTER_GOODS = "goods";


    void init();

    void onPause();

    void onDestroy();

    void setView(BaseView view);
}
