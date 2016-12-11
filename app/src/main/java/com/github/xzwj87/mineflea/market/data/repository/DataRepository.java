package com.github.xzwj87.mineflea.market.data.repository;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;

import com.github.xzwj87.mineflea.market.data.ResponseCode;
import com.github.xzwj87.mineflea.market.data.cache.FileCache;
import com.github.xzwj87.mineflea.market.data.cache.FileCacheImpl;
import com.github.xzwj87.mineflea.market.data.remote.RemoteDataSource;
import com.github.xzwj87.mineflea.market.data.remote.RemoteSourceCallBack;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.PresenterCallback;
import com.github.xzwj87.mineflea.utils.UserInfoUtils;

import static com.github.xzwj87.mineflea.market.presenter.BasePresenter.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by JasonWang on 2016/9/20.
 */

@Singleton
public class DataRepository implements BaseRepository,RemoteSourceCallBack{
    public static final String TAG = DataRepository.class.getSimpleName();

    @Inject FileCacheImpl mCache;
    @Inject RemoteDataSource mCloudSrc;

    private HashMap<String,PresenterCallback> mPresenterCbs;

    @Inject
    public DataRepository(FileCacheImpl cache, RemoteDataSource cloudSource){
        mCloudSrc = cloudSource;
        mCache = cache;
        mPresenterCbs = new HashMap<>();
    }

    public void init(){
        mCloudSrc.setCloudCallback(this);
    }

    @Override
    public void publishGoods(PublishGoodsInfo goods) {
        Log.v(TAG,"publishGoods(): goods = " + goods);

        mCloudSrc.publishGoods(goods);
    }

    @Override
    public void register(UserInfo userInfo,String authCode) {
        mCloudSrc.register(userInfo,authCode);
    }

    @Override
    public void login(UserInfo info) {
        mCloudSrc.login(info);
    }

    @Override
    public void loginBySms(String telNumber, String pwd) {
        mCloudSrc.loginBySms(telNumber,pwd);
    }

    @Override
    public void registerBySms(String telNumber, String authCode) {
        mCloudSrc.registerBySms(telNumber,authCode);
    }

    @Override
    public void sendSmsAuthCode(String number) {
        Log.v(TAG,"sendSmsAuthCode()");
        mCloudSrc.sendAuthCode(number);
    }

    @Override
    public void resetPwdBySms(String authCode, String newPwd) {
        Log.v(TAG,"resetPwdBySms()");
        mCloudSrc.resetPwdBySms(authCode,newPwd);
    }

    @Override
    public void getAuthCodeByAccount(String account) {
        if(UserInfoUtils.isTelNumberValid(account)){
            mCloudSrc.sendResetPwdBySms(account);
        }else{
            mCloudSrc.sendResetPwdEmail(account);
        }
    }

    @Override
    public void logout() {
        mCloudSrc.logOut();

        String currentUserId = getCurrentUserId();
        if(!TextUtils.isEmpty(currentUserId)
                && mCache.isCached(currentUserId,FileCache.CACHE_TYPE_USER)){
            mCache.delete(currentUserId,FileCache.CACHE_TYPE_USER);
        }
    }

    @Deprecated
    @Override
    public void uploadImageById(String imgUri, boolean isUser, boolean showProcess) {
        if(TextUtils.isEmpty(imgUri))  return;

        String type = FileCache.CACHE_TYPE_USER;
        if(!isUser){
          type = FileCache.CACHE_TYPE_GOODS;
        }

        if(!mCache.isImageCached(imgUri,type)){
            mCache.saveImgToFile(imgUri,type);
            mCloudSrc.uploadImg(imgUri,showProcess);
        }

    }

    @Override
    public void uploadImages(List<String> imgList, boolean showProgress) {
        Log.v(TAG,"uploadImages()");

        mCloudSrc.uploadImg(imgList,showProgress);
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

        if(mCache.isCached(getCurrentUserId(),FileCache.CACHE_TYPE_USER)){
            mCache.updateFile(getCurrentUser());
        }
    }

    @Override
    public void updateGoodsInfo(String id,String key, List<String> val) {
        Log.v(TAG,"updateGoodsInfo()");

        mCloudSrc.updateGoodsInfo(id, key, val);

        PublishGoodsInfo goodsInfo = mCache.getGoodsCache(id);
        List<String> localUrls = new ArrayList<>(val.size());
        for(String url: val){
            String img = mCache.saveImgToFile(url,FileCache.CACHE_TYPE_GOODS);
            localUrls.add(img);
            //Log.v(TAG,"updateGoodsInfo(): updated local urls = " + img);
        }

        goodsInfo.setImageUri(localUrls);
        mCache.updateFile(goodsInfo);

    }

    @Override
    public void addToMyFavorites(PublishGoodsInfo goods) {
        Log.v(TAG,"addToMyFavorites()");

        mCloudSrc.favor(goods);

        String myId = getCurrentUserId();
        if(!TextUtils.isEmpty(myId) &&
            mCache.isCached(myId,FileCache.CACHE_TYPE_USER)){
            UserInfo info = mCache.getUserCache(myId);
            info.addFavorGoods(goods.getId());
            mCache.updateFile(info);
        }

        if(mCache.isCached(goods.getId(),FileCache.CACHE_TYPE_GOODS)){
            mCache.updateFile(goods);
        }else{
            mCache.saveToFile(goods);
        }
    }

    @Override
    public void getUserInfoById(String id) {
        Log.v(TAG,"getUserInfoById()");
        if (mCache.isCached(id, FileCache.CACHE_TYPE_USER)) {
            UserInfo userInfo = mCache.getUserCache(id);
            PresenterCallback callback = mPresenterCbs.get(PRESENTER_USER_DETAIL);

            if(callback != null){
                final Message message = new Message();
                message.obj = userInfo;
                message.what = ResponseCode.RESP_GET_USER_INFO_SUCCESS;

                callback.onComplete(message);
            }else{
                callback = mPresenterCbs.get(PRESENTER_PUBLISH);
                if(callback != null){
                    final Message message = new Message();
                    message.obj = userInfo;
                    message.what = ResponseCode.RESP_GET_USER_INFO_SUCCESS;

                    callback.onComplete(message);
                }
            }
        }else {
            mCloudSrc.getUserInfoById(id);
        }

    }

    @Override
    public void getAllGoods() {
        Log.v(TAG,"getAllGoods()");

        List<PublishGoodsInfo> goodsList = mCache.getAllGoodsCache();

        if(goodsList != null && goodsList.size() > 0){
            final Message msg = new Message();
            msg.obj = goodsList;
            msg.what = ResponseCode.RESP_GET_GOODS_LIST_SUCCESS;

            PresenterCallback callback = mPresenterCbs.get(PRESENTER_GOODS_LIST);
            if(callback != null){
                callback.onComplete(msg);
            }
        }

        mCloudSrc.getAllGoods();
    }

    @Override
    public void follow(String userId) {
        mCloudSrc.follow(userId);

        UserInfo info = mCache.getUserCache(userId);
        if(info != null) {
            info.addFollowee(userId);
            mCache.updateFile(info);
        }
    }

    @Override
    public void unFollow(String userId) {
        mCloudSrc.unFollow(userId);

        UserInfo info = mCache.getUserCache(userId);
        if(info != null) {
            info.removeFollowee(userId);
            mCache.updateFile(info);
        }
    }

    @Override
    public void getGoodsInfoById(String goodsId) {
        if(TextUtils.isEmpty(goodsId)) return;
        PresenterCallback callback = mPresenterCbs.get(PRESENTER_GOODS_DETAIL);

        if(mCache.isCached(goodsId,FileCache.CACHE_TYPE_GOODS)){
            PublishGoodsInfo goodsInfo = mCache.getGoodsCache(goodsId);
            final Message msg = new Message();
            msg.obj = goodsInfo;
            msg.what = ResponseCode.RESP_GET_GOODS_SUCCESS;

            Log.v(TAG,"getGoodsInfoById()");
            if(callback != null){
                callback.onComplete(msg);
            }
        }else{
            mCloudSrc.getGoodsById(goodsId);
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

            PresenterCallback callback = mPresenterCbs.get(PRESENTER_GOODS);
            if(callback != null){
                final Message message = new Message();
                message.obj = goodsIdList;
                message.what = ResponseCode.RESP_GET_GOODS_LIST_SUCCESS;
                callback.onComplete(message);
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

    @Override
    public void registerCallBack(@PRESENTER_TYPE String type, PresenterCallback callback) {
        mPresenterCbs.put(type, callback);
        if(type.equals(PRESENTER_PUBLISH)){
            if(mPresenterCbs.get(PRESENTER_PUBLISH) != null){
                Log.v(TAG,"there is a publish callback");
            }
        }
        if(!mPresenterCbs.containsKey(type)) {
            mPresenterCbs.put(type, callback);
        }
    }

    @Override
    public void unregisterCallback(@PRESENTER_TYPE String type) {
        if(mPresenterCbs.containsKey(type)){
            mPresenterCbs.remove(type);
        }
    }

    public void onImgUploadComplete(Message msg) {

        Iterator iterator = mPresenterCbs.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            PresenterCallback callback = (PresenterCallback)entry.getValue();
            if(callback != null){
                callback.onComplete(msg);
            }
        }
    }

    @Override
    public void onGetUserInfoDone(Message msg) {
        Log.v(TAG,"onGetUserInfoDone()");
        Iterator iterator = mPresenterCbs.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            PresenterCallback callback = (PresenterCallback)entry.getValue();
            if(callback != null){
                callback.onComplete(msg);
            }
        }
    }

    @Override
    public void onGetGoodsInfoDone(Message msg) {
        Log.v(TAG,"onGetGoodsInfoDone()");

        //TODO: we may want to only do EXISTING CALLBACK
        Iterator iterator = mPresenterCbs.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            PresenterCallback callback = (PresenterCallback)entry.getValue();
            if(callback != null){
                callback.onComplete(msg);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onGetGoodsListDone(Message msg) {
        Log.v(TAG,"onGetGoodsListDone()");
        PresenterCallback callback = mPresenterCbs.get(PRESENTER_GOODS_LIST);
        if(callback != null) {
            callback.onComplete(msg);
        }

        // save it to cache
        if(msg.what == ResponseCode.RESP_GET_GOODS_LIST_SUCCESS) {
            List<PublishGoodsInfo> goodsList = (ArrayList<PublishGoodsInfo>) msg.obj;
            for(PublishGoodsInfo goods: goodsList){
                // save image to local
                List<String> imgList = goods.getImageUri();
                List<String> imgLocal = new ArrayList<>(imgList.size());
                for(String url: imgList) {
                    String name = URLUtil.guessFileName(url, null, null);
                    if (!mCache.isImageCached(name, FileCache.CACHE_TYPE_GOODS)) {
                        String path = mCache.saveImgToFile(url, FileCache.CACHE_TYPE_GOODS);
                        imgLocal.add(path);
                    }
                    Log.v(TAG, "onGetGoodsListDone(): url = " + url);
                }
                // ok,save it now
                if(imgLocal.size() > 0) {
                    goods.setImageUri(imgLocal);
                    if (!mCache.isCached(goods.getId(), FileCache.CACHE_TYPE_GOODS)) {
                        mCache.saveToFile(goods);
                    }
                }
            }
        }
    }

    @Override
    public void onGetUserFolloweeDone(Message message) {
        PresenterCallback callback = mPresenterCbs.get(PRESENTER_FOLLOWEE);
        if(callback != null) {
            callback.onComplete(message);
        }
    }

    @Override
    public void onGetUserFollowerDone(Message message) {
        PresenterCallback callback = mPresenterCbs.get(PRESENTER_FOLLOWER);
        if(callback != null) {
            callback.onComplete(message);
        }
    }

    @Override
    public void onResetPwdByEmailDone(Message message) {
        PresenterCallback callback = mPresenterCbs.get(PRESENTER_LOGIN);
        if(callback != null){
            callback.onComplete(message);
        }
    }

    @Override
    public void onResetPwdByTelDone(Message message) {
        PresenterCallback callback = mPresenterCbs.get(PRESENTER_LOGIN);
        if(callback != null){
            callback.onComplete(message);
        }
    }

    @Override
    public void onResetPwdBySms(Message msg) {
        Log.v(TAG,"onResetPwdBySms()");
        PresenterCallback callback = mPresenterCbs.get(PRESENTER_LOGIN);
        if(callback != null){
            callback.onComplete(msg);
        }
    }

    @Override
    public void onTelNumberVerifiedComplete(Message msg) {
        Log.v(TAG,"onTelNumberVerifiedComplete()");

    }

    @Override
    public void publishComplete(Message message) {
        Log.v(TAG,"publishComplete(): goods id = " + message.obj);

        if(message.obj != null) {
            PublishGoodsInfo goods = (PublishGoodsInfo)message.obj;
            if(!mCache.isCached(goods.getId(), FileCache.CACHE_TYPE_GOODS)){
                mCache.saveToFile(goods);
            }
        }

        PresenterCallback callback = mPresenterCbs.get(PRESENTER_PUBLISH);
        if(callback != null){
            callback.onComplete(message);
        }
    }

    @Override
    public void registerComplete(Message message) {
        Log.v(TAG,"registerComplete(): message " + message.obj);

        if(message.obj != null){
            mCache.saveToFile((UserInfo)message.obj);
        }

        PresenterCallback listener = mPresenterCbs.get(PRESENTER_REGISTER);
        if(listener != null){
            listener.onComplete(message);
        }
    }

    @Override
    public void updateProcess(int count) {
        Log.v(TAG,"updateProcess(): count = " + count);

        PresenterCallback listener = mPresenterCbs.get(PRESENTER_PUBLISH);
        if(listener != null){
            Log.v(TAG,"updateProcess(): count = " + count);
            final Message message = new Message();
            message.obj = count;
            listener.onNext(message);
        }
/*        if(mPresenterCb != null) {
            mPresenterCb.updateUploadProcess(count);
        }*/
    }

    @Override
    public void loginComplete(Message message) {
        UserInfo user;

        if(message.obj != null) {
            user = (UserInfo) message.obj;
            String imgUrl = user.getHeadIconUrl();
            String imgName = URLUtil.guessFileName(imgUrl,null,null);
            String cacheImg = null;
            if(imgUrl != null) {
                if (!mCache.isImageCached(imgName, FileCache.CACHE_TYPE_USER)) {
                    cacheImg = mCache.saveImgToFile(imgUrl, FileCache.CACHE_TYPE_USER);
                } else {
                    cacheImg = mCache.getImageFilePath(imgName, FileCache.CACHE_TYPE_USER);
                }
            }

            if(!mCache.isCached(user.getUserId(),FileCache.CACHE_TYPE_USER) ||
                    mCache.isExpired(user.getUserId(),FileCache.CACHE_TYPE_USER)) {
                mCache.saveToFile(user);
            }

            Log.v(TAG,"cached image path = " + cacheImg);

            user.setHeadIconUrl(cacheImg);
        }

        PresenterCallback listener = mPresenterCbs.get(PRESENTER_LOGIN);
        if(listener != null){
            listener.onComplete(message);
        }
    }
}
