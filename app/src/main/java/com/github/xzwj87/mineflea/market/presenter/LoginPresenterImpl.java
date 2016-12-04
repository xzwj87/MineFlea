package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;
import android.util.Log;

import com.github.xzwj87.mineflea.market.data.ResponseCode;
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
public class LoginPresenterImpl implements LoginPresenter{

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
        if(mIsEmail) {
            mDataRepo.login(mUserInfo);
        }else{
            mDataRepo.loginBySms(mUserInfo.getUserTelNumber(),mUserInfo.getUserPwd());
        }
        mView.showProgress(true);
    }

    @Override
    public boolean validLoginInfo() {
        boolean isValid = false;
        if(mIsEmail) {
            isValid = UserInfoUtils.isEmailValid(mUserInfo.getUserEmail());
        }else {
            isValid = UserInfoUtils.isTelNumber(mUserInfo.getUserTelNumber());
        }

        if(!isValid){
            mView.showAccountInvalidMsg();
        }

        return isValid;
    }

    @Override
    public void setUserAccount(String account) {
        if(UserInfoUtils.isPossibleEmail(account)){
            mUserInfo.setUerEmail(account);
            mUserInfo.setUserName(account);
            mIsEmail = true;
        }else if(UserInfoUtils.isTelNumber(account)){
            mUserInfo.setUserTelNumber(account);
            mIsEmail = false;
        }

    }

    @Override
    public void setUserPwd(String pwd) {
        mUserInfo.setUserPwd(pwd);
    }

    @Override
    public String getUserNickName() {
        if(mUserInfo == null) return null;

        return mUserInfo.getNickName();
    }

    @Override
    public String getUserEmail() {
        if(mUserInfo == null) return null;

        return mUserInfo.getUserEmail();
    }

    @Override
    public String getHeadIconUrl() {
        if(mUserInfo == null) return null;

        return mUserInfo.getHeadIconUrl();
    }

    @Override
    public void sendAuthCodeByAccount(String account) {
        Log.v(TAG,"getAuthCodeByAccount()");
        mDataRepo.getAuthCodeByAccount(account);
    }

    @Override
    public void resetPwdBySms(String authCode, String newPwd) {
        Log.v(TAG,"resetPwdBySms()");
        mDataRepo.resetPwdBySms(authCode,newPwd);
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
            mView.showProgress(false);

            switch (message.what) {
                    case ResponseCode.RESP_LOGIN_SUCCESS:
                        UserInfo user = (UserInfo) message.obj;
                        UserPrefsUtil.saveUserLoginInfo(user);

                        mView.onLoginSuccess();
                        break;

                    case ResponseCode.RESP_LOGIN_FAIL:
                        mView.onLoginFail();
                        if(message.arg1 == ResponseCode.RESP_LOGIN_INVALID_PASSWORD){
                            mView.showPwdInvalidMsg();
                        }
                        UserPrefsUtil.updateUserInfoBoolean(UserInfo.IS_LOGIN,false);
                        break;

                    case ResponseCode.RESP_RESET_PWD_BY_SMS_SUCCESS:
                        mView.resetPwdSuccess();
                        break;
                    case ResponseCode.RESP_RESET_PWD_BY_EMAIL_FAIL:
                        mView.showEmailResetPwdFailMsg();
                        break;
                    case ResponseCode.RESP_RESET_PWD_BY_SMS_FAIL:
                        mView.showSmsResetPwdFailMsg();
                        break;
                    default:
                        break;
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
