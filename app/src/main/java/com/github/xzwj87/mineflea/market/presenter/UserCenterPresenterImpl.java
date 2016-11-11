package com.github.xzwj87.mineflea.market.presenter;

import android.text.TextUtils;

import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.UserCenterView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by jason on 10/24/16.
 */

@PerActivity
public class UserCenterPresenterImpl extends UserCenterPresenter {
    public static final String TAG = UserCenterPresenterImpl.class.getSimpleName();

    @Inject MineFleaRepository mRepository;
    private UserInfo mUserInfo;
    private UserCenterView mView;

    @Inject
    public UserCenterPresenterImpl(MineFleaRepository repository){
        mRepository = repository;
    }

    @Override
    public void loadUserInfo() {
        mUserInfo = mRepository.getCurrentUser();

        if(mUserInfo != null){
            renderView();
        }
    }

    @Override
    public String getUserId() {
        if(mUserInfo == null){
            mView.showNeedLoginHint();
            return null;
        }

        return mUserInfo.getUserId();
    }

    @Override
    public void init() {
        mRepository.init();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void setView(BaseView view) {
        mView = (UserCenterView)view;
    }

    private void renderView(){
        if(!TextUtils.isEmpty(mUserInfo.getNickName())){
            mView.updateUserNickName(mUserInfo.getNickName());
        }

        if (!TextUtils.isEmpty(mUserInfo.getUserEmail())) {
            mView.updateUserEmail(mUserInfo.getUserEmail());
        }

        if(!TextUtils.isEmpty(mUserInfo.getHeadIconUrl())) {
            mView.updateHeadIcon(mUserInfo.getHeadIconUrl());
        }
    }
}
