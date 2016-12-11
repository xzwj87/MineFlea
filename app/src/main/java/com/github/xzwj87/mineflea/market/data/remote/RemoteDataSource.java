package com.github.xzwj87.mineflea.market.data.remote;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
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
import com.avos.avoscloud.UpdatePasswordCallback;
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
        Log.v(TAG,"getUserInfoById()");
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
                    Log.v(TAG,"getUserInfoById(): done");
                    final Message msg = new Message();
                    if (e == null) {
                        msg.obj = UserInfoUtils.fromAvUser(avUser);
                        msg.what = ResponseCode.RESP_GET_USER_INFO_SUCCESS;
                    } else {
                        msg.obj = null;
                        msg.what = ResponseCode.RESP_GET_USER_INFO_ERROR;
                        e.printStackTrace();
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

                mCloudCallback.onGetGoodsInfoDone(message);
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
        if(user != null) {
            user.put(key, val);
            user.saveInBackground();
        }
    }

    @Override
    public void updateGoodsInfo(String id,String key, List<String> val) {
        Log.v(TAG,"updateGoodsInfo()");

        AVObject object = AVObject.createWithoutData(AvCloudConstants.AV_OBJ_GOODS,id);
        object.put(key,val);

        object.saveInBackground();
    }

    @Override
    public void follow(String userId) {
        Log.v(TAG,"follow()");
        AVUser current = AVUser.getCurrentUser();
        // update list
        UserInfo userInfo = UserInfoUtils.fromAvUser(current);
        userInfo.addFollowee(userId);
        current.put(UserInfo.USER_FOLLOWEES,userInfo.getFolloweeList());
        current.saveInBackground();

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
        Log.v(TAG,"unFollow()");
        AVUser current = AVUser.getCurrentUser();

        // update list
        UserInfo userInfo = UserInfoUtils.fromAvUser(current);
        userInfo.removeFollowee(userId);
        current.put(UserInfo.USER_FOLLOWEES,userInfo.getFolloweeList());
        current.saveInBackground();

        current.unfollowInBackground(userId, new FollowCallback() {
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

    @SuppressWarnings("unchecked")
    @Override
    public void getAllGoods() {
        Log.v(TAG,"getAllGoods()");
        AVQuery<AVObject> query = new AVQuery(AvCloudConstants.AV_OBJ_GOODS);

        query.setLimit(MAX_GOODS_TO_GET_ONE_TIME);
        query.orderByAscending(PublishGoodsInfo.GOODS_UPDATED_TIME);

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
    public void loginBySms(String telNumber, String pwd) {
        Log.v(TAG,"loginBySms()");

        AVUser.loginByMobilePhoneNumberInBackground(telNumber, pwd, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                final Message msg = new Message();
                if(e != null){
                    msg.what = ResponseCode.RESP_LOGIN_FAIL;
                    msg.obj = null;
                    e.printStackTrace();
                }else{
                    msg.what = ResponseCode.RESP_LOGIN_SUCCESS;
                    msg.obj = UserInfoUtils.fromAvUser(avUser);
                }

                mCloudCallback.loginComplete(msg);
            }
        });
    }

    @Override
    public void registerBySms(String telNumber, String smsCode) {
        Log.v(TAG,"registerBySms()");

        AVUser.signUpOrLoginByMobilePhoneInBackground(telNumber, smsCode, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                final Message msg = new Message();
                if(e != null){
                    msg.what = ResponseCode.RESP_REGISTER_FAIL;
                    msg.obj = null;
                    e.printStackTrace();
                }else{
                    msg.what = ResponseCode.RESP_REGISTER_SUCCESS;
                    msg.obj = UserInfoUtils.fromAvUser(avUser);
                }

                mCloudCallback.registerComplete(msg);
            }
        });
    }

    @Override
    public void sendAuthCode(String number) {
        Log.v(TAG,"sendAuthCode()");

        AVOSCloud.requestSMSCodeInBackground(number, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if(e != null) {
                    Log.v(TAG,"sendAuthCode(): error" + e.getMessage());
                }
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
                    msg.what = ResponseCode.RESP_SEND_EMAIL_FAIL;
                    msg.obj = e.toString();
                }else{
                    msg.what = ResponseCode.RESP_SEND_EMAIL_SUCCESS;
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
                    msg.what = ResponseCode.RESP_SEND_SMS_CODE_FAIL;
                    e.printStackTrace();
                }else{
                    msg.what = ResponseCode.RESP_SEND_SMS_CODE_SUCCESS;
                }

                mCloudCallback.onResetPwdByTelDone(msg);
            }
        });
    }

    @Override
    public void resetPwdBySms(String authCode, String newPwd) {
        Log.v(TAG,"resetPwdBySms()");

        AVUser.resetPasswordBySmsCodeInBackground(authCode, newPwd, new UpdatePasswordCallback() {
            @Override
            public void done(AVException e) {
                final  Message msg = new Message();
                if(e != null){
                    e.printStackTrace();
                    msg.what = ResponseCode.RESP_RESET_PWD_BY_SMS_FAIL;
                }else{
                    msg.what = ResponseCode.RESP_SEND_SMS_CODE_SUCCESS;
                }

                mCloudCallback.onResetPwdBySms(msg);
            }
        });
    }

    @Override
    public void uploadImg(List<String> imgList, boolean showProcess) {
        if(imgList == null || imgList.size() == 0) return;

        Log.v(TAG,"uploadImg(): size = " + imgList.size());

        final List<String> imgUrls = new ArrayList<>(imgList.size());
        final Message msg = new Message();

        for(int i = 0; i < imgList.size(); ++i) {
            String imgUri = imgList.get(i);
            if (showProcess) {
                try {
                    final File file = new File(imgUri);
                    final AVFile avFile = AVFile.withAbsoluteLocalPath(file.getName(), file.getPath());
                    avFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e == null) {
                                imgUrls.add(avFile.getUrl());
                            }
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    mCloudCallback.updateProcess(100);
                }
            } else {
                try {
                    final File file = new File(imgUri);
                    final AVFile avFile = AVFile.withAbsoluteLocalPath(file.getName(), file.getPath());
                    avFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e == null) {
                                imgUrls.add(avFile.getUrl());
                            }
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    msg.what = ResponseCode.RESP_FILE_NOT_FOUND;
                    msg.obj = null;
                    mCloudCallback.onImgUploadComplete(msg);
                }
            }
        }
        // ok, done
        msg.what = ResponseCode.RESP_IMAGE_UPLOAD_SUCCESS;
        msg.obj = imgUrls;
        mCloudCallback.onImgUploadComplete(msg);
    }

    /**
     * release goods
     *
     * @param goods
     * @source: local and remote
     */
    @Override
    public void publishGoods(final PublishGoodsInfo goods) {

        final Message msg = new Message();
        if(NetConnectionUtils.isNetworkConnected()) {

            final AVObject avObject = PublishGoodsUtils.toAvObject(goods);

            avObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    Log.v(TAG,"saveInBackground(): done");
                    if(e != null){
                        msg.obj = null;
                        msg.what = ResponseCode.RESP_PUBLISH_GOODS_ERROR;
                        mCloudCallback.publishComplete(msg);
                        e.printStackTrace();
                    }else {
                        goods.setId(avObject.getObjectId());
                        msg.what = ResponseCode.RESP_PUBLISH_GOODS_SUCCESS;
                        msg.obj = goods;
                        Log.v(TAG, "publishGoods(): goods = " + goods);
                        mCloudCallback.publishComplete(msg);
                    }
                }
            });

        }else{
            // broadcast to upper layer about network state
            msg.what = ResponseCode.RESP_NETWORK_NOT_CONNECTED;
            mCloudCallback.publishComplete(msg);
        }
    }

    @Override
    public void register(final UserInfo userInfo, final String authCode) {
        Log.v(TAG,"register(): user info " + userInfo);

        final Message msg = new Message();

        if(NetConnectionUtils.isNetworkConnected()) {
            final AVUser avUser = UserInfoUtils.toAvUser(userInfo);

            avUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        userInfo.setUserId(avUser.getObjectId());
                        msg.obj = userInfo;
                        msg.what = ResponseCode.RESP_REGISTER_SUCCESS;
                        // verify SMS auth code
                        if (!TextUtils.isEmpty(authCode)) {
                            verifySmsCode(authCode);
                        }
                    } else {
                        Log.v(TAG, "avException = " + e.toString());
                        e.printStackTrace();
                        msg.obj = null;
                        msg.what = ResponseCode.RESP_REGISTER_FAIL;
                    }

                    mCloudCallback.registerComplete(msg);
                }
            });
        }else{
            msg.what = ResponseCode.RESP_NETWORK_NOT_CONNECTED;
            mCloudCallback.registerComplete(msg);
        }
    }

    @Override
    public void login(UserInfo info) {
        Log.v(TAG,"user info detail = " + info);

        final Message msg = new Message();

        if(NetConnectionUtils.isNetworkConnected()) {
            AVUser.logInInBackground(info.getUserName(), info.getUserPwd(), new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (e == null) {
                        msg.what = ResponseCode.RESP_LOGIN_SUCCESS;
                        msg.obj = UserInfoUtils.fromAvUser(avUser);
                    } else {
                        msg.what = ResponseCode.RESP_LOGIN_FAIL;
                        if (e.getCode() == AVException.INVALID_EMAIL_ADDRESS) {
                            msg.what = ResponseCode.RESP_LOGIN_INVALID_EMAIL;
                        } else if (e.getCode() == AVException.INVALID_PHONE_NUMBER) {
                            msg.what = ResponseCode.RESP_LOGIN_INVALID_PHONE_NUMBER;
                        } else if (e.getCode() == AVException.USERNAME_PASSWORD_MISMATCH) {
                            msg.what = ResponseCode.RESP_LOGIN_INVALID_PASSWORD;
                        }
                        msg.obj = null;
                    }

                    Log.v(TAG, "AVUser detail = " + avUser);

                    mCloudCallback.loginComplete(msg);
                }
            });
        }else{
            msg.what = ResponseCode.RESP_NETWORK_NOT_CONNECTED;
            mCloudCallback.loginComplete(msg);
        }
    }

    @Override
    public void favor(PublishGoodsInfo goodsInfo) {
        Log.v(TAG,"favor(): goods id = " + goodsInfo.getId());

        final AVUser user = AVUser.getCurrentUser();

        final AVObject object = AVObject.createWithoutData(
                AvCloudConstants.AV_OBJ_GOODS,goodsInfo.getId());
        object.put(PublishGoodsInfo.GOODS_FAVOR_USER,goodsInfo.getFavorUserList());
        //object.put(AvCloudConstants.AV_GOODS_NAME,goodsInfo.getName());

        if(user != null) {
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    AVRelation<AVObject> relation = user.getRelation(AvCloudConstants.AV_RELATION_USER_GOODS);
                    relation.add(object);
                    user.saveInBackground();
                    // Todo: we want to do callback to notify uppper layer
                }
            });
        }else{
            object.saveInBackground();
        }
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


    @Deprecated
    @Override
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
                            msg.what = ResponseCode.RESP_IMAGE_UPLOAD_ERROR;
                            msg.obj = e.getCode();
                            Log.e(TAG, "fail to upload image: " + file.getPath());
                        }else{
                            msg.what = ResponseCode.RESP_IMAGE_UPLOAD_SUCCESS;
                            msg.obj = avFile.getUrl();
                        }
                        mCloudCallback.onImgUploadComplete(msg);
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.what = ResponseCode.RESP_FILE_NOT_FOUND;
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

    private void verifySmsCode(String authCode){
        AVUser.verifyMobilePhoneInBackground(authCode, new AVMobilePhoneVerifyCallback() {
            @Override
            public void done(AVException e) {
                Log.v(TAG,"verifyMobilePhoneInBackground():done");
                final Message msg = new Message();
                if(e == null){
                    msg.what = ResponseCode.RESP_PHONE_NUMBER_VERIFIED_SUCCESS;
                }else{
                    msg.what = ResponseCode.RESP_PHONE_NUMBER_VERIFIED_ERROR;
                }
            }
        });
    }
}
