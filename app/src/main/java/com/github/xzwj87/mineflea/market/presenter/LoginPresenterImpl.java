package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;
import android.util.Log;

import com.avos.avoscloud.AVUser;
import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.LoginView;
import com.github.xzwj87.mineflea.utils.UserInfoUtils;
import com.github.xzwj87.mineflea.utils.UserPrefsUtil;
import com.tencent.qc.stat.common.User;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by jason on 10/16/16.
 */
@PerActivity
public class LoginPresenterImpl extends LoginPresenter{

    public static final String TAG = LoginPresenterImpl.class.getSimpleName();

    @Inject MineFleaRepository mDataRepo;
    private UserInfo mUserInfo;
    private LoginView mView;
    private boolean mIsEmail;

    @Inject
    public LoginPresenterImpl(MineFleaRepository repository){
        mDataRepo = repository;
    }

    @Override
    public void login() {
        mDataRepo.login(mUserInfo);
    }

    @Override
    public boolean validLoginInfo() {

        return UserInfoUtils.isEmailValid(mUserInfo.getUserEmail());
    }

    @Override
    public void setUserAccount(String account) {
        mUserInfo.setUerEmail(account);
        mUserInfo.setUserName(account);

        if(UserInfoUtils.isEmailValid(account)){
            mIsEmail = true;
        }
    }

    @Override
    public void setUserPwd(String pwd) {
        mUserInfo.setUserPwd(pwd);
    }

    @Override
    public void init() {
        mUserInfo = new UserInfo();
        mDataRepo.init();
        mDataRepo.setPresenterCallback(this);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void setView(BaseView view) {
        mView = (LoginView) view;
    }

    @Override
    public void loginComplete(Message message) {
        Log.v(TAG,"loginComplete(): user detail = " + message.obj);

        if(message.obj != null){
            UserInfo user = (UserInfo) message.obj;
            user.setLoginState(true);
            UserPrefsUtil.saveUserLoginInfo(user);

            mView.updateUserEmail(user.getUserEmail());
            mView.updateUserNickName(user.getNickName());
            mView.updateUserHeadIcon(user.getHeadIconUrl());

            mView.onLoginSuccess();
        }else{
            mView.onLoginFail();
            UserPrefsUtil.updateUserInfoBoolean(UserInfo.IS_LOGIN,false);
        }
    }
}
