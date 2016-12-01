package com.github.xzwj87.mineflea.market.data.cache;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.executor.JobExecutor;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * Created by jason on 11/13/16.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class FileCacheTest extends TestCase{

    private CacheManager mCacheMgr;
    private FileCacheImpl mFileCache;

    private static final String USER_ID = "84efadasfadaedbd";
    private static final String GOODS_ID = "dsaasdadfa891234";



    @Before
    @Override
    public void setUp(){
        mCacheMgr = new CacheManager();
        mFileCache = new FileCacheImpl(AppGlobals.getAppContext(),
                mCacheMgr,new JobExecutor());
    }

    @Test
    public void filePathTest(){
        File user = new File(mFileCache.getUserCachePath());
        File goods = new File(mFileCache.getGoodsCachePath());
        File img = new File(mFileCache.getImageCachePath());

        assertEquals(true,user.exists());
        assertEquals(true,goods.exists());
        assertEquals(true,img.exists());
    }

    @Test
    public void saveFileTest(){
        UserInfo userInfo = new UserInfo(USER_ID,"Jack","1234@qq.com");
        PublishGoodsInfo goodsInfo = new PublishGoodsInfo(GOODS_ID,"brush",USER_ID);

        mFileCache.saveToFile(userInfo);
        mFileCache.saveToFile(goodsInfo);

        File user = new File(mFileCache.getUserCachePath(),userInfo.getUserId());
        File goods = new File(mFileCache.getGoodsCachePath(),goodsInfo.getId());
        assertEquals(true,user.exists() && user.isFile());
        assertEquals(true,goods.exists() && goods.isFile());

        UserInfo userInfo1 = mFileCache.getUserCache(USER_ID);
        PublishGoodsInfo goodsInfo1 = mFileCache.getGoodsCache(GOODS_ID);

        assertEquals(true,USER_ID.equals(userInfo1.getUserId()));
        assertEquals(true,GOODS_ID.equals(goodsInfo1.getId()));
    }

    @After
    @Override
    public void tearDown(){
        mFileCache = null;
        mCacheMgr = null;
    }
}
