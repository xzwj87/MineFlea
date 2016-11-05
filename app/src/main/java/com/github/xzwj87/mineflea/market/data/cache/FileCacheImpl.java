package com.github.xzwj87.mineflea.market.data.cache;

import android.net.Uri;
import android.os.Environment;
import android.support.annotation.IntDef;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVUser;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.data.cache.serializer.JsonSerializer;
import com.github.xzwj87.mineflea.market.executor.JobExecutor;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.tencent.qc.stat.common.Env;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by jason on 11/5/16.
 */

@Singleton
public class FileCacheImpl implements FileCache{

    private static final long MAX_EXPIRATION_TIME = 12*60*60*1000;
    private static final int MAX_CACHE_SIZE = 50; //50MB

    private static String FILE_DIR_PARENT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MinFlea";
    // for saving current user info in case of being removed
    private static final String DIR_USER_DATA = "userdata";
    // for saving head icon etc
    private static final String DIR_IMAGE = "image";

    private static final String USER_CACHE_NAME = "_user";
    private static final String GOODS_CACHE_NAME = "_goods";
    private static final String IMAGE_CACHE_NAME = "_image";

    private File mCacheDir;
    @Inject CacheManager mCacheMgr;
    @Inject JobExecutor mExecutor;

    @Inject
    public FileCacheImpl(CacheManager cacheMgr, JobExecutor executor){
        mCacheMgr = cacheMgr;
        mExecutor = executor;

        if(isExternalStorageWritable()) {
            mCacheDir = AppGlobals.getAppContext().getExternalCacheDir();
        }else{
            mCacheDir = AppGlobals.getAppContext().getCacheDir();
            FILE_DIR_PARENT = AppGlobals.getAppContext().getFilesDir().getPath();
        }
    }

    @Override
    public boolean isCached(String id, @CacheType String type) {
        File file;
        if(type.equals(CACHE_TYPE_USER) && isCurrentUser(id)){
            file = buildUserDataFile(id);
        }else{
            file = buildFile(id,type);
        }

        return mCacheMgr.exist(file);
    }

    @Override
    public void saveToFile(UserInfo user) {
        String json = JsonSerializer.toJson(user);
        File file = buildUserFile(user.getUserId());

        executeAsync(new CacheWriter(mCacheMgr,file,json));
    }

    @Override
    public void saveToFile(PublishGoodsInfo goods) {
        String json = JsonSerializer.toJson(goods);
        File file = buildGoodsFile(goods.getId());

        executeAsync(new CacheWriter(mCacheMgr,file,json));
    }

    @Override
    public void saveImgToFile(String imgUri,@CacheType String type) {
        File in = new File(imgUri);
        if(in.exists()) {

            File out = buildImageFile(in.getName(),type);

            mCacheMgr.writeImgToFile(in,out);
        }
    }

    @Override
    public UserInfo getUserCache(String id) {
        File file = buildUserFile(id);
        String json = mCacheMgr.readFromFile(file);

        return JsonSerializer.fromUserJson(json);
    }

    @Override
    public PublishGoodsInfo getGoodsCache(String id) {
        File file = buildGoodsFile(id);
        String json = mCacheMgr.readFromFile(file);

        return JsonSerializer.fromGoodsJson(json);
    }

    @Override
    public boolean isExpired(String id, @CacheType String type) {
        File file;
        if (type.equals(CACHE_TYPE_USER)) {
            file = buildUserFile(id);
        }else{
            file = buildGoodsFile(id);
        }

        long lastUpdateTime = mCacheMgr.getCachedTime(file);
        long current = System.currentTimeMillis();


        return ((current - lastUpdateTime) >= MAX_EXPIRATION_TIME);
    }

    @Override
    public void delete(String id, @CacheType String type) {
        File file;
        if(CACHE_TYPE_USER.equals(type)){
            file = buildUserFile(id);
        }else{
            file = buildGoodsFile(id);
        }

        mCacheMgr.cleanDir(file);
    }

    @Override
    public void clear(@CacheType String type) {
        if(type.equals(CACHE_TYPE_USER)){
            clearUserDir();
        }else{
            clearGoodsDir();
        }
    }

    @Override
    public void clearAll() {
        clearUserDir();
        clearGoodsDir();
    }

    private void clearUserDir(){
        File file = new File(getUserCacheDir());

        executeAsync(new CacheClearer(mCacheMgr,file));
    }

    private void clearGoodsDir(){

        File file = new File(getGoodsCacheDir());

        executeAsync(new CacheClearer(mCacheMgr,file));
    }

    private File buildFile(String id, @CacheType String type){
        if(type.equals(CACHE_TYPE_USER)){
            return buildUserFile(id);
        }

        return buildGoodsFile(id);
    }

    private File buildUserFile(String id){
        String str = getUserCacheDir();
        str += (File.separator + id);

        return new File(str);
    }

    private File buildGoodsFile(String id){
        String str = getGoodsCacheDir();
        str += (File.separator + id);

        return new File(str);
    }

    private File buildUserDataFile(String id){
        String str = FILE_DIR_PARENT + File.separator +
                DIR_USER_DATA + File.separator + id;

        return new File(str);
    }

    /*
     * for images, we keep them on external storage
     */
    private File buildImageFile(String imgName,@CacheType String type){
        String str = FILE_DIR_PARENT + File.separator +
                DIR_IMAGE + File.separator +
                type + File.separator + imgName;

        return new File(str);
    }

    private boolean isCurrentUser(String id){
        AVUser user = AVUser.getCurrentUser();

        return user != null && user.getObjectId().equals(id);
    }

    private void executeAsync(Runnable runnable){
        mExecutor.execute(runnable);
    }

    private String getUserCacheDir(){
        return mCacheDir.getPath() +
                File.separator + USER_CACHE_NAME;
    }

    private String getGoodsCacheDir(){
        return mCacheDir.getPath() +
                File.separator + GOODS_CACHE_NAME;
    }

    private static class CacheWriter implements Runnable{
        private final CacheManager cacheMgr;
        private final File fileToWrite;
        private final String fileContent;

        public CacheWriter(CacheManager cacheManager,File writer,String content){
            cacheMgr = cacheManager;
            fileToWrite = writer;
            fileContent = content;
        }

        @Override
        public void run() {
            cacheMgr.writeToFile(fileToWrite,fileContent);
        }
    }

    private static class CacheClearer implements Runnable{

        private CacheManager cacheMgr;
        private File cleanDir;

        public CacheClearer(CacheManager cacheManager,File dir){
            cacheMgr = cacheManager;
            cleanDir = dir;
        }

        @Override
        public void run() {
            cacheMgr.cleanDir(cleanDir);
        }
    }

    private long getCachedSize(){
        long size = 0;

        File user = new File(getUserCacheDir());
        File goods = new File(getGoodsCacheDir());

        for(File file : user.listFiles()){
            size += file.length();
        }

        for(File file : goods.listFiles()){
            size += file.length();
        }

        return size/(1024*1000); //MB
    }

    private boolean exceedMaxSize(){
        return getCachedSize() >= MAX_CACHE_SIZE;
    }

    private boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
