package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVUser;
import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.UserDetailView;
import com.github.xzwj87.mineflea.utils.UserPrefsUtil;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by jason on 10/22/16.
 */

@PerActivity
public class UserDetailPresenterImpl extends UserDetailPresenter{
    private static final String TAG = UserDetailPresenterImpl.class.getSimpleName();

    @Inject MineFleaRepository mDataRepo;
    private UserDetailView mView;
    private List<PublishGoodsInfo> mGoodsList;
    private List<AVUser> mFolloweeList;
    private UserInfo mUserInfo;

    @Inject
    public UserDetailPresenterImpl(MineFleaRepository repository){
        mDataRepo = repository;
    }

    @Override
    public void getUserInfoById(String id) {
        mDataRepo.getUserInfoById(id);
    }

    @Override
    public void follow(String userId) {

    }

    @Override
    public void unFollow(String userId) {

    }

    @Override
    public boolean isMyFollowee(String userId) {
        if(mFolloweeList != null){
            for(int i = 0; i < mFolloweeList.size(); ++i){
                if(mFolloweeList.get(i).getObjectId().equals(userId)){
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void getGoodsList(String userId) {
        Log.v(TAG,"getGoodsList(): id = " + userId);
        mDataRepo.getGoodsListByUserId(userId);
    }

    @Override
    public String getCurrentUserId() {
        return mDataRepo.getCurrentUserId();
    }

    @Override
    public List<PublishGoodsInfo> getGoodsList() {
        return mGoodsList;
    }

    @Override
    public int getGoodsNumber() {
        return mGoodsList.size();
    }

    @Override
    public boolean isMe() {
        String id = mUserInfo.getUserId();
        String currentId = getCurrentUserId();

        return id != null && id.equals(currentId);
    }

    @Override
    public void init() {
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
        mView = (UserDetailView)view;
    }

    @Override
    public void onGetUserInfoComplete(Message message) {
        Log.v(TAG,"onGetUserInfoDone(): user info = " + message.obj);
        if(message.obj != null){
            mUserInfo = (UserInfo)message.obj;

            mDataRepo.queryUserFolloweeListByUserId(mUserInfo.getUserId());

            renderView();
        }else{
            mView.showGetInfoFailMsg();
            mView.finishView();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onGetUserFolloweeDone(Message message) {
        if(message.obj != null){
            mFolloweeList = (List<AVUser>)message.obj;
            mView.updateActionButton(true);
        }else{
            mView.updateActionButton(false);
        }
    }

    private void renderView(){
        if(!TextUtils.isEmpty(mUserInfo.getHeadIconUrl())) {
            mView.renderHeadIcon(mUserInfo.getHeadIconUrl());
        }

        if(!TextUtils.isEmpty(mUserInfo.getNickName())){
            mView.renderNickName(mUserInfo.getNickName());
        }

        if(!TextUtils.isEmpty(mUserInfo.getUserEmail())){
            mView.renderEmail(mUserInfo.getUserEmail());
        }
    }
}
