package com.github.xzwj87.mineflea.market.data.cache;

import android.support.annotation.CheckResult;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by jason on 11/5/16.
 */

public interface FileCache {

    @Retention(SOURCE)
    @StringDef({CACHE_TYPE_USER,CACHE_TYPE_GOODS})
    public @interface CacheType{};

    String CACHE_TYPE_USER = "user";
    String CACHE_TYPE_GOODS = "goods";

    /*
     * whether the data is cached based on its id
     * @param type - userInfo or goodsInfo
     */
    boolean isCached(String id,@CacheType String type);

    /*
     * whether an image is cached
     */
    boolean isImageCached(String imgName,@CacheType String type);

    /*
     * save a userInfo to cache
     */
    void saveToFile(UserInfo user);

    /*
     * update user cache
     */
    void updateFile(UserInfo info);

    /*
     * save a goods info to cache
     */
    void saveToFile(PublishGoodsInfo goods);

    /*
     * save image file to user data
     * @return cache dir of the image
     */
    String saveImgToFile(String imgUri,@CacheType String type);

    /*
     * get user cache or goods cache data by id
     */
    UserInfo getUserCache(String id);

    /*
     * get goods cache data by id
     */
    PublishGoodsInfo getGoodsCache(String id);

    /*
     * get image cache dir
     */
    String getImageCachePath(String name,@CacheType String type);

    /*
     * is expired for time duration too long
     */
    boolean isExpired(String id, @CacheType String type);


    /*
     * delete a file
     */
    void delete(String id,@CacheType String type);

    /*
     * clean the cache data by type
     */
    void clear(@CacheType String type);

    /*
     * clean all
     */
    void clearAll();



}
