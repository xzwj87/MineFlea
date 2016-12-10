package com.github.xzwj87.mineflea.market.data.repository;

/**
 * Created by jason on 10/29/16.
 */

import android.os.Environment;
import android.os.Message;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.data.cache.CacheManager;
import com.github.xzwj87.mineflea.market.data.cache.FileCacheImpl;
import com.github.xzwj87.mineflea.market.data.remote.RemoteDataSource;
import com.github.xzwj87.mineflea.market.executor.JobExecutor;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.BasePresenter;
import com.github.xzwj87.mineflea.market.presenter.PresenterCallback;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * Created by jason on 10/29/16.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class DataRepositoryTest extends TestCase{

    private String mIconUrl;

    private DataRepository mRepo;

    @Before
    public void setUp(){
        mRepo = new DataRepository(new FileCacheImpl(AppGlobals.getAppContext(),new CacheManager(),new JobExecutor()),
                new RemoteDataSource());
        mRepo.init();
        mIconUrl = Environment.getExternalStorageDirectory().getAbsolutePath() +
                Environment.DIRECTORY_DCIM + "/Camera/20160311_130939.jpg";
    }

    @Test
    public void register_test(){
        UserInfo user = new UserInfo("妮子","qaz123@163.com","qaz123@163.com",mIconUrl);
        user.setUserTelNumber("18923233233");
        //mRepo.register(user);
    }

    @Test
    public void login_test(){
        UserInfo user = new UserInfo();
        user.setUserName("1234@qq.com");
        user.setUserPwd("123456");

        mRepo.login(user);

        mRepo.registerCallBack(BasePresenter.PRESENTER_LOGIN, new PresenterCallback() {
            @Override
            public void onComplete(Message message) {

            }

            @Override
            public void onNext(Message message) {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void publishGoods_test(){

    }

    @After
    @Override
    public void tearDown(){
        mRepo = null;
        mIconUrl = null;
    }
}

