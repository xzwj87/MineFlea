package com.github.xzwj87.mineflea.market.data.repository;

import android.os.Message;
import android.util.Log;

import com.github.xzwj87.mineflea.market.data.cache.FileCache;
import com.github.xzwj87.mineflea.market.data.cache.FileCacheImpl;
import com.github.xzwj87.mineflea.market.data.remote.MineFleaRemoteSource;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.PresenterCallback;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by JasonWang on 2016/9/20.
 */

@Singleton
public class MineFleaRepository implements BaseRepository,MineFleaRemoteSource.CloudSourceCallback{
    public static final String TAG = MineFleaRepository.class.getSimpleName();

    @Inject FileCacheImpl mCache;
    @Inject MineFleaRemoteSource mCloudSrc;
    private PresenterCallback mPresenterCb;

    private PublishGoodsInfo mGoodsInfo;

    @Inject
    public MineFleaRepository(FileCacheImpl cache, MineFleaRemoteSource cloudSource){
        mCloudSrc = cloudSource;
        mCache = cache;
    }

    public void init(){
        mCloudSrc.setCloudCallback(this);
    }

    @Override
    public void publishGoods(PublishGoodsInfo goods) {
        Log.v(TAG,"publishGoods(): goods = " + goods);

        mGoodsInfo = goods;

        if(!mCache.isCached(goods.getId(), FileCache.CACHE_TYPE_GOODS)){
            mCache.saveToFile(goods);
        }

        mCloudSrc.publishGoods(goods);
    }

    @Override
    public void register(UserInfo userInfo) {
        mCache.saveToFile(userInfo);
        mCloudSrc.register(userInfo);
    }

    @Override
    public void login(UserInfo info) {

        mCloudSrc.login(info);
    }

    @Override
    public void logout() {
        mCloudSrc.logOut();;
    }

    @Override
    public void setPresenterCallback(PresenterCallback callback) {
        mPresenterCb = callback;
    }

    @Override
    public void uploadImageById(String id, String imgUri, boolean isUser, boolean showProcess) {
        String type = FileCache.CACHE_TYPE_USER;
        if(!isUser){
          type = FileCache.CACHE_TYPE_GOODS;
        }

        if(!mCache.isCached(id,type)){
            mCache.saveImgToFile(imgUri,type);
        }

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
    public void updateCurrentUserInfo(String key, String val) {
        mCloudSrc.updateCurrentUserInfo(key,val);
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
    public void queryFavorGoodsListByUserId(String id) {
        mCloudSrc.queryFavoriteGoodsList(id);
    }

    @Override
    public void queryUserFollowerListByUserId(String id) {

    }

    @Override
    public void queryUserFolloweeListByUserId(String id) {

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
    public void onGetUserFolloweeDone(Message message) {
        mPresenterCb.onGetUserFolloweeDone(message);
    }

    @Override
    public void onGetUserFollowerDone(Message message) {
        mPresenterCb.onGetUserFollowerDone(message);
    }


    @Override
    public void publishComplete(Message message) {
        Log.v(TAG,"publishComplete(): goods id = " + message.obj);

        if(message.obj != null) {
            mGoodsInfo.setId((String) message.obj);
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
