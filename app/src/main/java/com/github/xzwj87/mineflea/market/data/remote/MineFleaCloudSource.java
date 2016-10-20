package com.github.xzwj87.mineflea.market.data.remote;

import android.os.Message;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.github.xzwj87.mineflea.market.data.DataSource;
import com.github.xzwj87.mineflea.market.data.ResponseCode;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.ModelConstants;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.net.NetDataApiImpl;
import com.github.xzwj87.mineflea.utils.NetConnectionUtils;
import com.tencent.qc.stat.common.User;

import java.io.File;
import java.io.FileNotFoundException;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by JasonWang on 2016/9/20.
 */

@PerActivity
public class MineFleaCloudSource implements DataSource{
    public static final String TAG = MineFleaCloudSource.class.getSimpleName();

    private NetDataApiImpl mNetApi;
    private static MineFleaCloudSource sInstance;
    private CloudSourceCallback mCloudCallback;

    @Inject
    public MineFleaCloudSource(@Named("netApi") NetDataApiImpl netApi){
        mNetApi = netApi;
    }

    public void setCloudCallback(CloudSourceCallback callback){
        mCloudCallback = callback;
    }

    public static MineFleaCloudSource getInstance(){
        if(sInstance == null){
            sInstance = new MineFleaCloudSource(new NetDataApiImpl());
        }

        return sInstance;
    }

    public interface CloudSourceCallback{
        void publishComplete(Message message);
        void registerComplete(Message message);
        void updateProcess(int count);
        void loginComplete(Message message);
        void onImgUploadComplete(Message msg);
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

            final AVObject avObject = new AVObject(ModelConstants.AV_OBJ_GOODS);
            avObject.put(PublishGoodsInfo.GOODS_NAME,goods.getName());
            avObject.put(PublishGoodsInfo.GOODS_PUBLISHER,goods.getPublisher());
            avObject.put(PublishGoodsInfo.GOODS_LOW_PRICE,goods.getLowerPrice());
            avObject.put(PublishGoodsInfo.GOODS_HIGH_PRICE,goods.getHighPrice());
            avObject.put(PublishGoodsInfo.GOODS_RELEASE_DATE,goods.getReleasedDate());

            avObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    Log.v(TAG,"saveInBackground(): done");

                    final Message msg = new Message();
                    if(e != null){
                        msg.obj = null;
                        msg.arg1 = ResponseCode.RESP_AV_SAVED_FAILURE;
                        mCloudCallback.publishComplete(msg);
                        return;
                    }

                    String id = avObject.getObjectId();
                    msg.arg1 = ResponseCode.RESP_AV_SAVED_SUCCESS;
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
    public void register(UserInfo userInfo) {
        Log.v(TAG,"register(): user info " + userInfo);

        final AVUser avUser = new AVUser();

        avUser.put(UserInfo.USER_NICK_NAME,userInfo.getNickName());
        avUser.put(UserInfo.USER_HEAD_ICON,userInfo.getHeadIconUrl());
        avUser.setUsername(userInfo.getUserEmail());
        avUser.setEmail(userInfo.getUserEmail());
        avUser.setMobilePhoneNumber(userInfo.getUserTelNumber());
        avUser.setPassword(userInfo.getUserPwd());

        avUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                final Message msg = new Message();
                if(e == null){
                    msg.obj = avUser.getObjectId();
                    msg.arg1 = ResponseCode.RESP_REGISTER_SUCCESS;
                }else{
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
                    message.arg1 = ResponseCode.RESP_LOGIN_SUCCESS;
                    message.obj = avUser;
                }else{
                    message.arg1 = ResponseCode.RESP_LOGIN_FAIL;
                    message.obj = null;
                }

                Log.v(TAG,"AVUser detail = " + avUser);

                mCloudCallback.loginComplete(message);
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
                            msg.arg1 = ResponseCode.RESP_AV_SAVED_FAILURE;
                            msg.obj = null;
                            Log.e(TAG, "fail to upload image: " + file.getPath());
                        }else{
                            msg.arg1 = ResponseCode.RESP_AV_SAVED_SUCCESS;
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
}
