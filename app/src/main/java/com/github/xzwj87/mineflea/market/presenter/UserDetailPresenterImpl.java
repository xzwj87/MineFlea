package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.UserDetailView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by jason on 10/22/16.
 */

@PerActivity
public class UserDetailPresenterImpl extends UserDetailPresenter{
    private static final String TAG = UserDetailPresenterImpl.class.getSimpleName();

    private MineFleaRepository mDataRepo;
    private UserDetailView mView;

    @Inject
    public UserDetailPresenterImpl(@Named("dataRepository")MineFleaRepository repository){
        mDataRepo = repository;
    }

    @Override
    public void getUserInfoById(String id) {
        ;
    }

    @Override
    public void updateFollowers() {

    }

    @Override
    public List<PublishGoodsInfo> getGoodsList(String userId) {
        return null;
    }

    @Override
    public String getCurrentUserId() {
        return mDataRepo.getCurrentUserId();
    }

    @Override
    public void init() {
        mDataRepo.init();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void setView(BaseView view) {
        mView = (UserDetailView)view;
    }

    @Override
    public void onGetUserInfoComplete(Message message) {
        Log.v(TAG,"onGetUserInfoComplete()");
        if(message.obj != null){
            mView.onGetUserInfoComplete((UserInfo)message.obj);
        }else{
            mView.showGetUserInfoFail();
            mView.finishView();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onGetGoodsListDone(Message message) {
        Log.v(TAG,"onGetGoodsListDone()");
        if(message != null){
            mView.onGetGoodsListDone((List<PublishGoodsInfo>)message.obj);
        }else{
            mView.showGetGoodsListFail();
            mView.showGetGoodsListFail();
        }
    }
}
