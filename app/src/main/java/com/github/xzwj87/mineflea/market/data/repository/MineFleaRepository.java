package com.github.xzwj87.mineflea.market.data.repository;

import android.os.Message;
import android.util.Log;

import com.github.xzwj87.mineflea.market.data.local.MineFleaLocalSource;
import com.github.xzwj87.mineflea.market.data.remote.MineFleaRemoteSource;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.PresenterCallback;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by JasonWang on 2016/9/20.
 */

// TODO: Create/Query/Update/Delete
@Singleton
public class MineFleaRepository implements BaseRepository,MineFleaRemoteSource.CloudSourceCallback{
    public static final String TAG = MineFleaRepository.class.getSimpleName();

    private MineFleaLocalSource mLocalSrc;
    @Inject
    MineFleaRemoteSource mCloudSrc;
    private PresenterCallback mPresenterCb;

    private PublishGoodsInfo mGoodsInfo;

    @Inject
    public MineFleaRepository(@Named("localResource") MineFleaLocalSource localSource,
                              @Named("remoteResource") MineFleaRemoteSource cloudSource){
        Log.v(TAG,"Constructor()");
        mCloudSrc = cloudSource;
        mLocalSrc = localSource;
    }

    public void init(){
        mCloudSrc.setCloudCallback(this);
    }

    @Override
    public void publishGoods(PublishGoodsInfo goods) {
        Log.v(TAG,"publishGoods(): goods = " + goods);

        mGoodsInfo = goods;
        mCloudSrc.publishGoods(goods);
    }

    @Override
    public void register(UserInfo userInfo) {
        mCloudSrc.register(userInfo);
    }

    @Override
    public void login(UserInfo info) {
        mCloudSrc.login(info);
    }

    @Override
    public void setPresenterCallback(PresenterCallback callback) {
        mPresenterCb = callback;
    }

    @Override
    public void uploadImage(String imgUri,boolean showProcess) {
        mCloudSrc.uploadImg(imgUri,showProcess);
    }

    @Override
    public String getCurrentUserId() {
        return mCloudSrc.getCurrentUserId();
    }

    @Override
    public UserInfo getCurrentUser() {
        return mCloudSrc.getCurrentUser();
    }

    @Override
    public void getUserInfoById(String id) {
        mCloudSrc.getUserInfoById(id);
    }

    @Override
    public void getGoodsListByUserId(String id) {
        mCloudSrc.getGoodsListByUserId(id);
    }

    @Override
    public void queryGoodsListByUserId(String id) {
        mCloudSrc.queryFavoriteGoodsList(id);
    }

    public void onImgUploadComplete(Message msg) {
        if(mPresenterCb != null) {
            mPresenterCb.onImgUploadComplete(msg);
        }
    }

    @Override
    public void onGetUserInfoDone(Message msg) {
        if(mPresenterCb != null) {
            mPresenterCb.onGetUserInfoComplete(msg);
        }
    }

    @Override
    public void onGetGoodsListDone(Message msg) {
        if(mPresenterCb != null) {
            mPresenterCb.onGetGoodsListDone(msg);
        }
    }


    @Override
    public void publishComplete(Message message) {
        Log.v(TAG,"publishComplete(): goods id = " + message.obj);

        if(message.obj != null) {
            mGoodsInfo.setId((String) message.obj);
            mLocalSrc.publishGoods(mGoodsInfo);
        }

        if(mPresenterCb != null) {
            mPresenterCb.onPublishComplete(message);
        }
    }

    @Override
    public void registerComplete(Message message) {
        Log.v(TAG,"registerComplete(): message " + message.obj);

        if(mPresenterCb != null) {
            mPresenterCb.onRegisterComplete(message);
        }
    }

    @Override
    public void updateProcess(int count) {
        Log.v(TAG,"updateProcess(): count = " + count);

        if(mPresenterCb != null) {
            mPresenterCb.updateUploadProcess(count);
        }
    }

    @Override
    public void loginComplete(Message message) {
        if(mPresenterCb != null) {
            mPresenterCb.loginComplete(message);
        }
    }
}
