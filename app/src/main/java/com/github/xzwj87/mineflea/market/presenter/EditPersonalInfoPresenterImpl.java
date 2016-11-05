package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.EditPersonalInfoView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by jason on 11/2/16.
 */

@PerActivity
public class EditPersonalInfoPresenterImpl extends EditPersonalInfoPresenter{

    private EditPersonalInfoView mView;
    private MineFleaRepository mRepo;
    private UserInfo mCurrent;

    @Inject
    public EditPersonalInfoPresenterImpl(@Named("dataRepository")MineFleaRepository repository){
        mRepo = repository;
    }

    @Override
    public void init() {
        mRepo.init();
        mRepo.setPresenterCallback(this);

        mCurrent = mRepo.getCurrentUser();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

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
        mRepo.uploadImage(iconUrl,false);
        mView.updateHeadIcon(iconUrl);
    }

    // Todo: request to verify email
    @Override
    public void setEmail(String email) {
        mCurrent.setUerEmail(email);
        mView.updateEmail(email);
    }

    // Todo: request to verify telephony number
    @Override
    public void setTelNumber(String telNumber) {
        mCurrent.setUserTelNumber(telNumber);
        mView.updateTelNumber(telNumber);
    }

    @Override
    public void setIntro(String intro) {
        mCurrent.setIntro(intro);

        mRepo.updateCurrentUserInfo(UserInfo.USER_INTRO,intro);

        mView.updateIntro(intro);
    }

    @Override
    public void onImgUploadComplete(Message message) {
        if(message.obj != null){
            String url = (String)message.obj;
            mRepo.updateCurrentUserInfo(UserInfo.USER_HEAD_ICON,url);
        }
    }


    private void initView(){
        mView.updateHeadIcon(mCurrent.getHeadIconUrl());
        mView.updateNickName(mCurrent.getNickName());
        mView.updateEmail(mCurrent.getUserEmail());
        mView.updateTelNumber(mCurrent.getUserTelNumber());
        mView.updateIntro(mCurrent.getIntro());
    }
}
