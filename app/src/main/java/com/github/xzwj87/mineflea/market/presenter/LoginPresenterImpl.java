package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.data.repository.DataRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.LoginView;
import com.github.xzwj87.mineflea.utils.UserInfoUtils;
import com.github.xzwj87.mineflea.utils.UserPrefsUtil;

import javax.inject.Inject;

/**
 * Created by jason on 10/16/16.
 */
@PerActivity
public class LoginPresenterImpl extends LoginPresenter{

    public static final String TAG = LoginPresenterImpl.class.getSimpleName();

    @Inject
    DataRepository mDataRepo;
    private UserInfo mUserInfo;
    private LoginView mView;
    private boolean mIsEmail;

    @Inject
    public LoginPresenterImpl(DataRepository repository){
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
        }else{
            mView.showAccountInvalidMsg();
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
        mDataRepo.registerCallBack(PRESENTER_LOGIN,new LoginPresenterCallback());
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        mUserInfo = null;
        mDataRepo.unregisterCallback(PRESENTER_LOGIN);
    }

    @Override
    public void setView(BaseView view) {
        mView = (LoginView) view;
    }

    private class LoginPresenterCallback implements PresenterCallback {

        @Override
        public void onComplete(Message message) {
            if(message.obj != null){
                UserInfo user = (UserInfo) message.obj;
                UserPrefsUtil.saveUserLoginInfo(user);

                mView.updateUserEmail(user.getUserEmail());
                mView.updateUserNickName(user.getNickName());
                mView.updateUserHeadIcon(user.getHeadIconUrl());

                mView.onLoginSuccess();
                mView.showProgress(false);
            }else{
                mView.onLoginFail();
                UserPrefsUtil.updateUserInfoBoolean(UserInfo.IS_LOGIN,false);
            }
        }

        @Override
        public void onNext(Message message) {

        }

        @Override
        public void onError(Throwable e) {

        }
    }
}
