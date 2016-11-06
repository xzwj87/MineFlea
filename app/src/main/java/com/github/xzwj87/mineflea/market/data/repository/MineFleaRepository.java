package com.github.xzwj87.mineflea.market.data.repository;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;

import com.github.xzwj87.mineflea.market.data.ResponseCode;
import com.github.xzwj87.mineflea.market.data.cache.FileCache;
import com.github.xzwj87.mineflea.market.data.cache.FileCacheImpl;
import com.github.xzwj87.mineflea.market.data.remote.MineFleaRemoteSource;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.PresenterCallback;
import com.squareup.picasso.Picasso;
import com.tencent.tauth.bean.Pic;

import java.util.ArrayList;
import java.util.List;

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
        mCloudSrc.logOut();

        String currentUserId = getCurrentUserId();
        if(mCache.isCached(currentUserId,FileCache.CACHE_TYPE_USER)){
            mCache.delete(currentUserId,FileCache.CACHE_TYPE_USER);
        }
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
        String id = getCurrentUserId();
        if(mCache.isCached(id,FileCache.CACHE_TYPE_USER)){
            return mCache.getUserCache(id);
        }

        return mCloudSrc.getCurrentUser();
    }

    @Override
    public void updateCurrentUserInfo(String key, String val) {
        mCloudSrc.updateCurrentUserInfo(key,val);

        if(mCache.isExpired(getCurrentUserId(),FileCache.CACHE_TYPE_USER)
            && mCache.isCached(getCurrentUserId(),FileCache.CACHE_TYPE_USER)){
            mCache.updateFile(getCurrentUser());
        }
    }

    @Override
    public void getUserInfoById(String id) {
        if (mCache.isCached(id, FileCache.CACHE_TYPE_USER) &&
                !mCache.isExpired(id, FileCache.CACHE_TYPE_USER)) {
            UserInfo userInfo = mCache.getUserCache(id);
            if(mPresenterCb != null){
                final Message message = new Message();
                message.obj = userInfo;
                message.what = ResponseCode.RESP_GET_USER_INFO_SUCCESS;

                mPresenterCb.onGetUserInfoComplete(message);
            }
        }else {
            mCloudSrc.getUserInfoById(id);
        }

    }

    @Override
    public void getGoodsListByUserId(String id) {
        if(!mCache.isExpired(id,FileCache.CACHE_TYPE_USER) &&
                mCache.isCached(id,FileCache.CACHE_TYPE_USER)){
            UserInfo userInfo = mCache.getUserCache(id);
            List<String> goodsIdList = userInfo.getGoodsList();
            List<PublishGoodsInfo> goodsList = new ArrayList<>();

            /*
             * we may want to update one by one instead of get it all together
             */
            for(int i = 0; i < goodsIdList.size(); ++i){
                String goodsId = goodsIdList.get(i);
                if(mCache.isCached(goodsId,FileCache.CACHE_TYPE_GOODS)
                        && !mCache.isExpired(goodsId,FileCache.CACHE_TYPE_GOODS)) {
                    goodsList.add(mCache.getGoodsCache(goodsId));
                //Todo: if cache miss, get it from cloud
                }else{
                    mCloudSrc.getGoodsById(goodsId);
                }
            }

            if(mPresenterCb != null){
                final Message message = new Message();
                message.obj = goodsIdList;
                message.what = ResponseCode.RESP_GET_GOODS_LIST_SUCCESS;
                mPresenterCb.onGetUserInfoComplete(message);
            }
        }else{
            mCloudSrc.getGoodsListByUserId(id);
        }
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
    public void onGetGoodsInfoDone(Message msg) {

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
        UserInfo user;

        if(message.obj != null) {
            user = (UserInfo) message.obj;
            String imgUrl = user.getHeadIconUrl();
            String imgName = URLUtil.guessFileName(imgUrl,null,null);
            String cacheImg;
            if(!mCache.isImageCached(imgName,FileCache.CACHE_TYPE_USER)){
                cacheImg  = mCache.saveImgToFile(imgUrl,FileCache.CACHE_TYPE_USER);
            }else{
                cacheImg = mCache.getImageCachePath(imgName,FileCache.CACHE_TYPE_USER);
            }

            if(!mCache.isCached(user.getUserId(),FileCache.CACHE_TYPE_USER) ||
                    mCache.isExpired(user.getUserId(),FileCache.CACHE_TYPE_USER)) {
                mCache.saveToFile(user);
            }

            Log.v(TAG,"cached image path = " + cacheImg);

            user.setHeadIconUrl(cacheImg);
        }

        if(mPresenterCb != null){
            mPresenterCb.loginComplete(message);
        }
    }
}
