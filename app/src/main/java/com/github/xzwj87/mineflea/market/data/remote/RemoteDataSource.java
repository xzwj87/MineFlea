package com.github.xzwj87.mineflea.market.data.remote;

import android.os.Message;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPowerfulUtils;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.SaveCallback;
import com.github.xzwj87.mineflea.market.data.ResponseCode;
import com.github.xzwj87.mineflea.market.model.AvCloudConstants;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.utils.NetConnectionUtils;
import com.github.xzwj87.mineflea.utils.PublishGoodsUtils;
import com.github.xzwj87.mineflea.utils.UserInfoUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by JasonWang on 2016/9/20.
 */

@Singleton
public class RemoteDataSource implements RemoteSource{
    public static final String TAG = RemoteDataSource.class.getSimpleName();

    private static RemoteDataSource sInstance;
    private RemoteSourceCallBack mCloudCallback;

    @Inject
    public RemoteDataSource(){
    }

    public void setCloudCallback(RemoteSourceCallBack callback){
        mCloudCallback = callback;
    }

    @Override
    public void getUserInfoById(String id) {
        UserInfo user = getCurrentUser();
        if(user != null && user.getUserId().equals(id)){
            final  Message msg = new Message();
            msg.obj = user;
            msg.what = ResponseCode.RESP_GET_USER_INFO_SUCCESS;
            mCloudCallback.onGetUserInfoDone(msg);
        }else{
            AVQuery<AVUser> query = new AVQuery<>(AvCloudConstants.AV_OBJ_USER);

            query.getInBackground(id, new GetCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    final Message msg = new Message();
                    if (e == null) {
                        msg.obj = UserInfoUtils.fromAvUser(avUser);
                        msg.what = ResponseCode.RESP_GET_USER_INFO_SUCCESS;
                    } else {
                        msg.obj = null;
                        msg.what = ResponseCode.RESP_GET_USER_INFO_ERROR;
                    }

                    mCloudCallback.onGetUserInfoDone(msg);
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getGoodsListByUserId(String id) {
        Log.v(TAG,"getGoodsListByUserId()");

        AVQuery query = new AVQuery(AvCloudConstants.AV_OBJ_GOODS);
        query.whereEqualTo(PublishGoodsInfo.GOODS_PUBLISHER,id);

        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                final Message msg = new Message();
                if(e != null){
                    msg.what = ResponseCode.RESP_GET_GOODS_LIST_SUCCESS;
                    List<PublishGoodsInfo> goodsList = new ArrayList<PublishGoodsInfo>();
                    for(int i = 0; i < list.size(); ++i){
                        goodsList.add(PublishGoodsUtils.fromAvObject(list.get(i)));
                    }

                    msg.obj = goodsList;
                }else{
                    msg.what = ResponseCode.RESP_GET_GOODS_LIST_ERROR;
                    msg.obj = null;
                }

                mCloudCallback.onGetGoodsListDone(msg);
            }
        });
    }

    @Override
    public void getGoodsById(String id) {
        Log.v(TAG,"getGoodsById()");
        AVQuery query = new AVQuery(AvCloudConstants.AV_OBJ_GOODS);
        query.whereEqualTo(AvCloudConstants.AV_OBJ_ID,id);

        query.getInBackground(id, new GetCallback() {
            @Override
            public void done(AVObject object, AVException e) {
                PublishGoodsInfo goodsInfo = PublishGoodsUtils.fromAvObject(object);

                final Message message = new Message();
                message.obj = goodsInfo;
                if(goodsInfo != null){
                    message.what = ResponseCode.RESP_GET_GOODS_SUCCESS;
                }else{
                    message.what = ResponseCode.RESP_GET_GOODS_ERROR;
                }

                mCloudCallback.onGetUserInfoDone(message);
            }
        });
    }

    @Override
    public String getCurrentUserId() {
        AVUser user = AVUser.getCurrentUser();
        if(user == null){
            return null;
        }

        return user.getObjectId();
    }

    @Override
    public UserInfo getCurrentUser() {
        AVUser user = AVUser.getCurrentUser();
        if(user == null){
            return null;
        }

        return UserInfoUtils.fromAvUser(user);
    }

    @Override
    public void updateCurrentUserInfo(String key, String val) {
        AVUser user = AVUser.getCurrentUser();
        user.put(key,val);
        user.saveInBackground();
    }

    @Override
    public void follow(String userId) {
        AVUser current = AVUser.getCurrentUser();
        current.followInBackground(userId, new FollowCallback() {
            @Override
            public void done(AVObject object, AVException e) {
                if(e == null){
                    Log.v(TAG,"follow successfully");
                }else if(e.getCode() == AVException.DUPLICATE_VALUE){
                    Log.v(TAG,"already followed");
                }else{
                    // followed error
                }
            }
        });
    }

    @Override
    public void unFollow(String userId) {
        AVUser user = AVUser.getCurrentUser();

        user.unfollowInBackground(userId, new FollowCallback() {
            @Override
            public void done(AVObject object, AVException e) {
                if(e == null){
                    Log.v(TAG,"unfollow successfully");
                }else{
                    Log.v(TAG,"fail to unfollow");
                }
            }
        });
    }

    @Override
    public void getMyFollowee(String userId) {
        if(userId == null) return;

        AVUser user = AVUser.getCurrentUser();
        AVQuery<AVUser> query = AVUser.followeeQuery(user.getObjectId(),AVUser.class);
        query.whereEqualTo(AvCloudConstants.AV_OBJ_ID,userId);

        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                final  Message msg = new Message();
                if(e == null){
                    msg.obj = list;
                    msg.what = ResponseCode.RESP_QUERY_FOLLOWEES_SUCCESS;
                }else{
                    msg.obj = null;
                    msg.what = ResponseCode.RESP_QUERY_FOLLOWEES_ERROR;
                }

                mCloudCallback.onGetUserFolloweeDone(msg);
            }
        });
    }

    @Override
    public void getFolloweeList(String userId) {
        AVQuery<AVUser> query = AVUser.followeeQuery(userId,AVUser.class);
        query.include(AvCloudConstants.AV_USER_FOLLOWEES);

        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                final Message message = new Message();
                if(e == null){
                    message.obj = list;
                    message.what = ResponseCode.RESP_QUERY_FOLLOWEES_SUCCESS;
                }else{
                    message.obj = null;
                    message.what = ResponseCode.RESP_QUERY_FOLLOWEES_ERROR;
                }

                mCloudCallback.onGetUserFolloweeDone(message);
            }
        });
    }

    @Override
    public void getFollowerList(String userId) {
        AVQuery<AVUser> query = AVUser.followerQuery(userId,AVUser.class);
        query.include(AvCloudConstants.AV_USER_FOLLOWERS);

        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                final Message message = new Message();
                if(e == null){
                    message.obj = list;
                    message.what = ResponseCode.RESP_QUERY_FOLLOWEES_SUCCESS;
                }else{
                    message.obj = null;
                    message.what = ResponseCode.RESP_QUERY_FOLLOWEES_ERROR;
                }

                mCloudCallback.onGetUserFollowerDone(message);
            }
        });

    }

    @Override
    public void getAllGoods() {
        AVQuery<AVObject> query = new AVQuery(AvCloudConstants.AV_OBJ_GOODS);

        query.setLimit(MAX_GOODS_TO_GET_ONE_TIME);
        query.orderByAscending(AvCloudConstants.AV_UPDATE_TIME);

        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                final Message message = new Message();
                if(e == null){
                    List<PublishGoodsInfo> goodsList = new ArrayList<PublishGoodsInfo>();
                    for(int i = 0; i < list.size(); ++i){
                        goodsList.add(PublishGoodsUtils.fromAvObject(list.get(i)));
                    }

                    message.what = ResponseCode.RESP_GET_GOODS_LIST_SUCCESS;
                    message.obj = goodsList;
                }else{
                    message.what = ResponseCode.RESP_GET_GOODS_LIST_ERROR;
                    message.arg1 = e.getCode();
                    message.obj = null;
                }

                mCloudCallback.onGetGoodsListDone(message);
            }
        });
    }

    @Override
    public void sendAuthCode(String number) {
        Log.v(TAG,"sendAuthCode()");

        AVOSCloud.requestSMSCodeInBackground(number, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                Log.v(TAG,"sendAuthCode(): done");
            }
        });
    }

    @Override
    public void sendResetPwdEmail(String emailAddress) {
        Log.v(TAG,"sendResetPwdEmail()");

        AVUser.requestPasswordResetInBackground(emailAddress, new RequestPasswordResetCallback() {
            @Override
            public void done(AVException e) {
                final Message msg = new Message();

                if(e != null){
                    e.printStackTrace();
                    // tell user to try again
                    msg.what = ResponseCode.RESP_RESET_PWD_BY_EMAIL_FAIL;
                    msg.obj = e.toString();
                }else{
                    msg.what = ResponseCode.RESP_RESET_PWD_BY_EMAIL_SUCCESS;
                    msg.obj = null;
                }

                mCloudCallback.onResetPwdByEmailDone(msg);
            }
        });
    }

    @Override
    public void sendResetPwdBySms(String telNumber) {
        Log.v(TAG,"sendResetPwdBySms()");

        AVUser.requestPasswordResetBySmsCodeInBackground(telNumber, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                final Message msg = new Message();
                if(e != null){
                    msg.what = ResponseCode.RESP_RESET_PWD_BY_SMS_FAIL;
                    msg.obj = e.toString();
                    e.printStackTrace();
                }else{
                    msg.what = ResponseCode.RESP_RESET_PWD_BY_SMS_SUCCESS;
                    msg.obj = null;
                }

                mCloudCallback.onResetPwdByTelDone(msg);
            }
        });
    }

    /**
     * release goods
     *
     * @param goods
     * @source: local and remote
     */
    @Override
    public void publishGoods(final PublishGoodsInfo goods) {

        if(NetConnectionUtils.isNetworkConnected()) {

            final AVObject avObject = PublishGoodsUtils.toAvObject(goods);

            avObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    Log.v(TAG,"saveInBackground(): done");

                    final Message msg = new Message();
                    if(e != null){
                        msg.obj = null;
                        msg.arg1 = ResponseCode.RESP_PUBLISH_GOODS_ERROR;
                        mCloudCallback.publishComplete(msg);
                        return;
                    }

                    String id = avObject.getObjectId();
                    msg.arg1 = ResponseCode.RESP_PUBLISH_GOODS_SUCCESS;
                    msg.obj = id;
                    Log.v(TAG,"publishGoods(): goods = " + id);
                    mCloudCallback.publishComplete(msg);
                }
            });

        }else{
            // broadcast to upper layer about network state
        }
    }

    @Override
    public void register(final UserInfo userInfo) {
        Log.v(TAG,"register(): user info " + userInfo);

        final AVUser avUser = UserInfoUtils.toAvUser(userInfo);

        avUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                final Message msg = new Message();
                if(e == null){
                    userInfo.setUserId(avUser.getObjectId());
                    msg.obj = userInfo;
                    msg.arg1 = ResponseCode.RESP_REGISTER_SUCCESS;
                }else{
                    Log.v(TAG,"avException = " + e.toString());
                    e.printStackTrace();
                    msg.obj = null;
                    msg.arg1 = ResponseCode.RESP_REGISTER_FAIL;
                }

                mCloudCallback.registerComplete(msg);
            }
        });
    }

    @Override
    public void login(UserInfo info) {
        Log.v(TAG,"user info detail = " + info);

        AVUser.logInInBackground(info.getUserName(),info.getUserPwd(), new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                Message message = new Message();
                if(e == null){
                    message.what = ResponseCode.RESP_LOGIN_SUCCESS;
                    message.obj = UserInfoUtils.fromAvUser(avUser);
                }else{
                    message.what = ResponseCode.RESP_LOGIN_FAIL;
                    if(e.getCode() == AVException.INVALID_EMAIL_ADDRESS) {
                        message.arg1 = ResponseCode.RESP_LOGIN_INVALID_EMAIL;
                    }else if(e.getCode() == AVException.INVALID_PHONE_NUMBER){
                        message.arg1 = ResponseCode.RESP_LOGIN_INVALID_PHONE_NUMBER;
                    }else if(e.getCode() == AVException.USERNAME_PASSWORD_MISMATCH){
                        message.arg1 = ResponseCode.RESP_LOGIN_INVALID_PASSWORD;
                    }
                    message.obj = null;
                }

                Log.v(TAG,"AVUser detail = " + avUser);

                mCloudCallback.loginComplete(message);
            }
        });
    }

    @Override
    public void favor(PublishGoodsInfo goodsInfo) {
        Log.v(TAG,"favor(): goods id = " + goodsInfo.getId());

        final AVUser user = AVUser.getCurrentUser();

        final AVObject object = PublishGoodsUtils.toAvObject(goodsInfo);
        object.put(AvCloudConstants.AV_GOODS_NAME,goodsInfo.getName());

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVRelation<AVObject> relation = user.getRelation(AvCloudConstants.AV_RELATION_USER_GOODS);
                relation.add(object);
                user.saveInBackground();
            }
        });
    }

    @Override
    public void queryFavoriteGoodsList(String userId) {
        Log.v(TAG,"queryFavorGoodsList() : user id = " + userId);

        AVObject user = AVUser.createWithoutData(AVPowerfulUtils.
                getAVClassName(AVUser.class.getSimpleName()),userId);
        AVRelation<AVObject> relation = user.getRelation(AvCloudConstants.AV_RELATION_USER_GOODS);

        AVQuery<AVObject> query = relation.getQuery();
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                final Message msg = new Message();
                if(e == null){
                    List<PublishGoodsInfo> goodsList = new ArrayList<PublishGoodsInfo>();
                    for(int i = 0; i < list.size(); ++i){
                        goodsList.add(PublishGoodsUtils.fromAvObject(list.get(i)));
                    }

                    msg.obj = goodsList;
                    msg.what = ResponseCode.RESP_QUERY_FAVORITE_GOODS_LIST_SUCCESS;
                }else{
                    msg.obj = null;
                    msg.what = ResponseCode.RESP_QUERY_FAVORITE_GOODS_LIST_ERROR;
                }

                mCloudCallback.onGetGoodsListDone(msg);
            }
        });
    }


    public void uploadImg(final String imgUri, boolean showProcess){
        Log.v(TAG,"uploadImg(): img = " + imgUri);

        if(showProcess) {
            try {
                final File file = new File(imgUri);
                AVFile avFile = AVFile.withAbsoluteLocalPath(file.getName(), file.getPath());

                avFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            Log.e(TAG, "fail to upload image: " + file.getPath());
                        }
                        Log.v(TAG, "saveInBackground(): done");
                    }
                }, new ProgressCallback() {
                    @Override
                    public void done(Integer integer) {
                        Log.v(TAG, "uploadImg(): current process = " + integer);
                        mCloudCallback.updateProcess(integer);
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                mCloudCallback.updateProcess(100);
            }
        }else{
            try {
                final File file = new File(imgUri);
                final AVFile avFile = AVFile.withAbsoluteLocalPath(file.getName(), file.getPath());

                avFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        Log.v(TAG, "saveInBackground(): done");
                        final Message msg = new Message();
                        if (e != null) {
                            msg.arg1 = ResponseCode.RESP_IMAGE_UPLOAD_ERROR;
                            msg.obj = e.getCode();
                            Log.e(TAG, "fail to upload image: " + file.getPath());
                        }else{
                            msg.arg1 = ResponseCode.RESP_IMAGE_UPLOAD_SUCCESS;
                            msg.obj = avFile.getUrl();
                        }
                        mCloudCallback.onImgUploadComplete(msg);
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.arg1 = ResponseCode.RESP_FILE_NOT_FOUND;
                msg.obj = null;
                mCloudCallback.onImgUploadComplete(msg);
            }
        }

    }

    public boolean isLogin(){
        AVUser current = AVUser.getCurrentUser();

        if(current != null){
            return true;
        }

        return false;
    }


    public void logOut(){
        AVUser.logOut();

        AVUser current = AVUser.getCurrentUser();

        if(current == null){
            Log.v(TAG,"log out successfully");
        }
    }
}
