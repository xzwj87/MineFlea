package com.github.xzwj87.mineflea.market.data.repository;

import android.os.Message;
import android.util.Log;

import com.github.xzwj87.mineflea.market.data.local.MineFleaLocalSource;
import com.github.xzwj87.mineflea.market.data.remote.MineFleaCloudSource;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.callback.LoginCallback;
import com.github.xzwj87.mineflea.market.presenter.callback.PublishCallBack;
import com.github.xzwj87.mineflea.market.presenter.callback.RegisterCallBack;

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

    private PublishCallBack mPublishCb;
    private RegisterCallBack mRegisterCb;
    private LoginCallback mLoginCb;

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
    public void setPublishCallback(PublishCallBack callback) {
        mPublishCb = callback;
    }

    @Override
    public void setRegisterCallback(RegisterCallBack callback) {
        mRegisterCb = callback;
    }

    @Override
    public void setLoginCallback(LoginCallback callback) {
        mLoginCb = callback;
    }


    @Override
    public void publishComplete(Message message) {
        Log.v(TAG,"publishComplete(): goods id = " + message.obj);

        if(message.obj != null) {
            mGoodsInfo.setId((String) message.obj);
            mLocalSrc.publishGoods(mGoodsInfo);
        }

        mPublishCb.onPublishComplete(message);
    }

    @Override
    public void registerComplete(Message message) {
        Log.v(TAG,"registerComplete(): message " + message.obj);

        mRegisterCb.onRegisterComplete(message);
    }

    @Override
    public void loginComplete(Message message) {
        mLoginCb.loginComplete(message);
    }
}
