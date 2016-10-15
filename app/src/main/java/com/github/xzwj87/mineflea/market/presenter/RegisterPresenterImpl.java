package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.github.xzwj87.mineflea.market.interactor.DefaultSubscriber;
import com.github.xzwj87.mineflea.market.interactor.RegisterUseCase;
import com.github.xzwj87.mineflea.market.interactor.UseCase;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.RegisterView;
import com.github.xzwj87.mineflea.utils.UserInfoUtils;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by jason on 10/14/16.
 */

@PerActivity
public class RegisterPresenterImpl implements RegisterPresenter{
    public static final String TAG = RegisterPresenterImpl.class.getSimpleName();

    private RegisterView mView;
    private RegisterUseCase mUseCase;
    private UserInfo mUserInfo;

    @Inject
    public RegisterPresenterImpl(@Named("register")UseCase useCase){
        mUseCase = (RegisterUseCase)useCase;
    }

    @Override
    public void setView(BaseView view) {
        mView = (RegisterView)view;
    }

    @Override
    public void register() {
        Log.v(TAG,"register()");
        mUseCase.register(mUserInfo);
    }

    @Override
    public void setUserName(String name) {
        mUserInfo.setUserName(name);
    }

    @Override
    public void setUserEmail(String email) {
        mUserInfo.setUerEmail(email);
    }

    @Override
    public void setUserTel(String tel) {
        mUserInfo.setUserTelNumber(tel);
    }

    @Override
    public void setUserPwd(String pwd) {
        mUserInfo.setUserPwd(pwd);
    }

    @Override
    public boolean validUserInfo() {

        if(!UserInfoUtils.isNameValid(mUserInfo.getUserName())){
            mView.showNameInvalidMsg();
            return false;
        }

        if(!UserInfoUtils.isEmailValid(mUserInfo.getUserEmail())){
            mView.showEmailInvalidMsg();
            return false;
        }

        if(!UserInfoUtils.isTelNumberValid(mUserInfo.getUserTelNumber())){
            mView.showTelInvalidMsg();
            return false;
        }

        if(!UserInfoUtils.isPasswordValid(mUserInfo.getUserPwd())){
            mView.showPwdInvalidMsg();
            return false;
        }


        //mView.showProgress();

        return true;
    }

    @Override
    public void init() {
        mUserInfo = new UserInfo();
        mUseCase.execute(new RegisterSubscriber());
    }

    @Override
    public void onPause() {
        mUseCase.unSubscribe();
    }

    @Override
    public void onDestroy() {

    }

    private class RegisterSubscriber extends DefaultSubscriber<Message>{
        @Override
        public void onCompleted(){}

        @Override
        public void onNext(Message message){
            Log.v(TAG,"onNext(): message =  " + message.arg1);

            if(message.obj != null){
                mView.onRegisterComplete(true);
            }else {
                mView.onRegisterComplete(false);
            }
        }

        @Override
        public void onError(Throwable e){

        }
    }

}
