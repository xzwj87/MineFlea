package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.data.ResponseCode;
import com.github.xzwj87.mineflea.market.data.repository.DataRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.EditPersonalInfoView;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

/**
 * Created by jason on 11/2/16.
 */

@PerActivity
public class EditPersonalInfoPresenterImpl implements EditPersonalInfoPresenter{

    private EditPersonalInfoView mView;
    @Inject
    DataRepository mRepo;
    private UserInfo mCurrent;

    @Inject
    public EditPersonalInfoPresenterImpl(DataRepository repository){
        mRepo = repository;
    }

    @Override
    public void init() {
        mRepo.init();
        mRepo.registerCallBack(PRESENTER_EDIT,new EditPresenterListener());

        mCurrent = mRepo.getCurrentUser();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        mCurrent = null;
        mRepo.unregisterCallback(PRESENTER_EDIT);
    }

    @Override
    public void setView(BaseView view) {
        mView = (EditPersonalInfoView)view;
        initView();
    }

    @Override
    public void setNickName(String nickName) {
        mCurrent.setNickName(nickName);
        mRepo.updateCurrentUserInfo(UserInfo.USER_NICK_NAME,nickName);
        mView.updateNickName(nickName);
    }

    @Override
    public void setHeadIcon(String iconUrl) {
        mCurrent.setHeadIconUrl(iconUrl);
        ArrayList<String> list = (ArrayList<String>) Arrays.asList(iconUrl);
        mRepo.uploadImages(list,false);
        //mRepo.uploadImage(iconUrl,false);
        mView.updateHeadIcon(iconUrl);
    }

    // Todo: request to verify email
    @Override
    public void setEmail(String email) {
        mCurrent.setUerEmail(email);
        mView.updateEmail(email);
    }

    @Override
    public String getEmail() {
        return mCurrent.getUserEmail();
    }

    // Todo: request to verify telephony number
    @Override
    public void setTelNumber(String telNumber) {
        mCurrent.setUserTelNumber(telNumber);
        mView.updateTelNumber(telNumber);
    }

    @Override
    public String getTelNumber() {
        return mCurrent.getUserTelNumber();
    }

    @Override
    public void setIntro(String intro) {
        mCurrent.setIntro(intro);

        mRepo.updateCurrentUserInfo(UserInfo.USER_INTRO,intro);

        mView.updateIntro(intro);
    }

    @Override
    public boolean isEmailVerified() {
        return mCurrent != null && mCurrent.getEmailVerified();
    }

    @Override
    public boolean isTelVerified() {
        return mCurrent != null && mCurrent.getTelVerified();
    }

    private void initView(){
        mView.updateHeadIcon(mCurrent.getHeadIconUrl());
        mView.updateNickName(mCurrent.getNickName());
        mView.updateEmail(mCurrent.getUserEmail());
        mView.updateTelNumber(mCurrent.getUserTelNumber());
        mView.updateIntro(mCurrent.getIntro());
    }


    private class EditPresenterListener implements PresenterCallback {

        @Override
        public void onComplete(Message message) {
            int what = message.what;
            switch (what) {
                case ResponseCode.RESP_IMAGE_UPLOAD_SUCCESS:
                    if (message.obj != null) {
                        String url = (String) message.obj;
                        mRepo.updateCurrentUserInfo(UserInfo.USER_HEAD_ICON, url);
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onNext(Message object) {

        }

        @Override
        public void onError(Throwable e) {

        }
    }
}
