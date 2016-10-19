package com.github.xzwj87.mineflea.market.data.repository;

import android.os.Message;
import android.util.Log;

import com.github.xzwj87.mineflea.market.data.local.MineFleaLocalSource;
import com.github.xzwj87.mineflea.market.data.remote.MineFleaCloudSource;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.PresenterCallback;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by JasonWang on 2016/9/20.
 */

// TODO: Create/Query/Update/Delete
@PerActivity
public class MineFleaRepository implements BaseRepository,MineFleaCloudSource.CloudSourceCallback{
    public static final String TAG = MineFleaRepository.class.getSimpleName();

    private MineFleaLocalSource mLocalSrc;
    @Inject MineFleaCloudSource mCloudSrc;
    //private HashMap<String,PresenterCallback> mPresenterCbs;
    private PresenterCallback mCb;

    private PublishGoodsInfo mGoodsInfo;

    @Inject
    public MineFleaRepository(@Named("localResource") MineFleaLocalSource localSource,
                              @Named("remoteResource") MineFleaCloudSource cloudSource){
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
        mCb = callback;
    }

    @Override
    public void uploadImage(String imgUri) {
        mCloudSrc.uploadImg(imgUri);
    }


    @Override
    public void publishComplete(Message message) {
        Log.v(TAG,"publishComplete(): goods id = " + message.obj);

        if(message.obj != null) {
            mGoodsInfo.setId((String) message.obj);
            mLocalSrc.publishGoods(mGoodsInfo);
        }

        mCb.onPublishComplete(message);
    }

    @Override
    public void registerComplete(Message message) {
        Log.v(TAG,"registerComplete(): message " + message.obj);

        mCb.onRegisterComplete(message);
    }

    @Override
    public void updateProcess(int count) {
        Log.v(TAG,"updateProcess(): count = " + count);
        mCb.updateUploadProcess(count);
    }


    @Override
    public void loginComplete(Message message) {
        mCb.loginComplete(message);
    }
}
