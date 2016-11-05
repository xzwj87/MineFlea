package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;
import android.util.Log;

import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.UserFollowInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.UserFollowView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by jason on 10/31/16.
 */

@PerActivity
public class UserFollowerPresenterImpl extends UserFollowerPresenter {

    private static final String TAG = UserFollowerPresenterImpl.class.getSimpleName();

    private UserFollowView mView;
    @Inject MineFleaRepository mRepo;
    private List<UserFollowInfo> mUserFollowList;

    @Inject
    public UserFollowerPresenterImpl(MineFleaRepository repository){
        mRepo = repository;
    }

    @Override
    public void init() {
        mRepo.init();
        mRepo.setPresenterCallback(this);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        mUserFollowList = null;
    }

    @Override
    public void setView(BaseView view) {
        mView = (UserFollowView)view;
    }

    @Override
    public int getUserFollowCount() {
        return mUserFollowList.size();
    }

    @Override
    public UserFollowInfo getUserFollowAtPos(int pos) {
        return mUserFollowList.get(pos);
    }

    @Override
    public void getUserFollowList(String userId) {
        mRepo.queryUserFolloweeListByUserId(userId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onGetUserFollowListDone(Message message) {
        Log.v(TAG,"onGetUserFollowListDone()");

        if(message.obj == null){
            mView.showBlankPage();
        }else{
            mUserFollowList = (List<UserFollowInfo>)message.obj;
            mView.renderView();
        }

        mView.showProgress(false);
    }

    @Override
    public void onGetUserFollowerDone(Message message) {

    }
}
