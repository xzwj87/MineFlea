package com.github.xzwj87.mineflea.market.data.repository;

/**
 * Created by jason on 10/29/16.
 */

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.data.cache.CacheManager;
import com.github.xzwj87.mineflea.market.data.cache.FileCacheImpl;
import com.github.xzwj87.mineflea.market.data.remote.MineFleaRemoteSource;
import com.github.xzwj87.mineflea.market.executor.JobExecutor;
import com.github.xzwj87.mineflea.market.model.UserInfo;

import junit.framework.TestCase;

import org.junit.After;
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
        mRepo = new MineFleaRepository(new FileCacheImpl(AppGlobals.getAppContext(),new CacheManager(),new JobExecutor()),
                new MineFleaRemoteSource());
        mRepo.init();
        mIconUrl = Environment.getExternalStorageDirectory().getAbsolutePath() +
                Environment.DIRECTORY_DCIM + "/Camera/20160311_130939.jpg";
    }

    @Test
    public void register_test(){
        UserInfo user = new UserInfo("妮子","qaz123@163.com","qaz123@163.com",mIconUrl);
        user.setUserTelNumber("18923233233");
        mRepo.register(user);
    }

    public void login_test(){

    }

    public void publishGoods_test(){

    }

    @After
    @Override
    public void tearDown(){
        mRepo = null;
        mIconUrl = null;
    }
}

