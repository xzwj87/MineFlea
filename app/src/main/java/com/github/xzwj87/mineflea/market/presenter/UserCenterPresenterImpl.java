package com.github.xzwj87.mineflea.market.presenter;

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

    private MineFleaRepository mRepository;
    private UserInfo mUserInfo;
    private UserCenterView mView;

    @Inject
    public UserCenterPresenterImpl(@Named("dataRepository") MineFleaRepository repository){
        mRepository = repository;
    }

    @Override
    public void loadUserInfo() {
        mUserInfo = mRepository.getCurrentUser();

        if(mUserInfo != null){
            mView.updateUserNickName(mUserInfo.getNickName());
            mView.updateUserEmail(mUserInfo.getUserEmail());
            mView.updateHeadIcon(mUserInfo.getHeadIconUrl());
        }
    }

    @Override
    public String getUserId() {
        if(mUserInfo == null){
            return mRepository.getCurrentUserId();
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
}