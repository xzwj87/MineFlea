package com.github.xzwj87.mineflea.market.data.repository;

/**
 * Created by jason on 10/29/16.
 */

import android.os.Environment;
import android.os.Message;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.github.xzwj87.mineflea.market.data.local.MineFleaLocalSource;
import com.github.xzwj87.mineflea.market.data.remote.MineFleaRemoteSource;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.net.NetDataApiImpl;
import com.github.xzwj87.mineflea.market.presenter.PresenterCallback;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

/**
 * Created by jason on 10/29/16.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MineFleaRepositoryTest extends TestCase{

    private String mIconUrl;

    private MineFleaRepository mRepo;

    @Before
    public void setUp(){
        mRepo = new MineFleaRepository(MineFleaLocalSource.getInstance(),new MineFleaRemoteSource(new NetDataApiImpl()));
        mRepo.init();

        mRepo.setPresenterCallback(new RemoteSrcCallback());
        mIconUrl = Environment.getExternalStorageDirectory().getAbsolutePath() +
                Environment.DIRECTORY_DCIM + "/Camera/20160311_130939.jpg";
    }

    @Test
    public void register_test(){
        UserInfo user = new UserInfo("妮子","qaz123@163.com","qaz123@163.com",mIconUrl);
        user.setUserTelNumber("18923233233");
        user.setLoginDate(new Date());
        mRepo.uploadImage(mIconUrl,false);
        mRepo.register(user);
    }

    public void login_test(){

    }

    public void publishGoods_test(){

    }


    private class RemoteSrcCallback implements PresenterCallback {

        @Override
        public void loginComplete(Message message) {

        }

        @Override
        public void onPublishComplete(Message message) {

        }

        @Override
        public void onRegisterComplete(Message message) {
            assertEquals(message.obj,null);
        }

        @Override
        public void updateUploadProcess(int count) {

        }

        @Override
        public void onImgUploadComplete(Message message) {
            assertEquals(message.obj,null);
        }

        @Override
        public void onGetUserInfoComplete(Message message) {

        }

        @Override
        public void onGetGoodsListDone(Message message) {

        }

        @Override
        public void onGetUserFollowListDone(Message message) {

        }
    }
}

