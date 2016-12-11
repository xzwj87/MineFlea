package com.github.xzwj87.mineflea.market.data.cache;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.data.cache.serializer.JsonSerializer;
import com.github.xzwj87.mineflea.market.executor.JobExecutor;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.utils.FileManager;
import com.github.xzwj87.mineflea.utils.PicassoUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by jason on 11/5/16.
 */

@Singleton
public class FileCacheImpl implements FileCache{
    private static final String TAG = "FileCache";

    private static final long MAX_EXPIRATION_TIME = 12*60*60*1000;
    private static final int MAX_CACHE_SIZE = 50; //50MB

    private static String FILE_DIR_PARENT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MinFlea/cache";
    // for saving current user info in case of being removed
    private static final String DIR_USER_DATA = "userdata";

    private static final String USER_CACHE_NAME = "_user";
    private static final String GOODS_CACHE_NAME = "_goods";
    private static final String IMAGE_CACHE_NAME = "_image";

    private File mCacheDir;
    @Inject CacheManager mCacheMgr;
    @Inject JobExecutor mExecutor;

    // currently how many goods are retrieved from the cache
    private int mCurrentRetrieved = 0;

    @Inject
    public FileCacheImpl(Context context, CacheManager cacheMgr, JobExecutor executor){
        mCacheMgr = cacheMgr;
        mExecutor = executor;

        if(isExternalStorageWritable()) {
            mCacheDir = new File(FILE_DIR_PARENT);
        }else{
            FILE_DIR_PARENT = context.getFilesDir().getPath();
            mCacheDir = new File(FILE_DIR_PARENT);
        }

        // make dirs for cache
        File user = new File(getUserCacheDir());
        File goods = new File(getGoodsCacheDir());
        File userImg = new File((getImgCacheDir() + File.separator + CACHE_TYPE_USER));
        File goodsImg = new File((getImgCacheDir() + File.separator + CACHE_TYPE_GOODS));

        if(!user.exists()){
            user.mkdirs();
        }

        if(!goods.exists()){
            goods.mkdir();
        }

        if(!userImg.exists()){
            userImg.mkdirs();
        }

        if(!goodsImg.exists()){
            goodsImg.mkdir();
        }
    }

    @Override
    public boolean isCached(String id, @CacheType String type) {
        if(TextUtils.isEmpty(id)){
            return true;
        }

        File file;
        if(type.equals(CACHE_TYPE_USER) && isCurrentUser(id)){
            file = buildUserDataFile(id);
        }else{
            file = buildFile(id,type);
        }

        return mCacheMgr.exist(file);
    }

    @Override
    public boolean isImageCached(String imgName, @CacheType String type) {
        if(TextUtils.isEmpty(imgName)){
            return true;
        }

        File file = buildImageFile(imgName,type);

        return mCacheMgr.exist(file);
    }

    @Override
    public void saveToFile(UserInfo user) {
        String json = JsonSerializer.toJson(user);
        File file = buildUserFile(user.getUserId());

        executeAsync(new CacheWriter(mCacheMgr,file,json,false));
    }

    @Override
    public void updateFile(UserInfo info) {
        String json = JsonSerializer.toJson(info);
        File file = buildUserFile(info.getUserId());

        executeAsync(new CacheWriter(mCacheMgr,file,json,true));
    }

    @Override
    public void saveToFile(PublishGoodsInfo goods) {
        String json = JsonSerializer.toJson(goods);
        File file = buildGoodsFile(goods.getId());

        executeAsync(new CacheWriter(mCacheMgr,file,json,false));
    }

    @Override
    public void updateFile(PublishGoodsInfo goods) {
        String json = JsonSerializer.toJson(goods);
        File file = buildGoodsFile(goods.getId());

        executeAsync(new CacheWriter(mCacheMgr,file,json,true));
    }

    @Override
    public String saveImgToFile(String imgUri,@CacheType String type) {
        Log.v(TAG,"saveImgToFile()");

        String path;
        if(URLUtil.isNetworkUrl(imgUri)) {
            String imgName = URLUtil.guessFileName(imgUri,null,null);
            File out = buildImageFile(imgName,type);
            path = out.getAbsolutePath();
            executeAsync(new ImageDownloader(out,imgUri));
        }else{
            final File in = new File(imgUri);
            final File out = buildImageFile(in.getName(), type);
            path = out.getAbsolutePath();

            if (in.exists()) {
                executeAsync(new Runnable() {
                    @Override
                    public void run() {
                        mCacheMgr.writeImgToFile(in, out);
                    }
                });
            }
        }

        return path;
    }

    @Override
    public UserInfo getUserCache(String id) {
        if(TextUtils.isEmpty(id)) return null;

        File file = buildUserFile(id);
        String json = mCacheMgr.readFromFile(file);

        return JsonSerializer.fromUserJson(json);
    }

    @Override
    public PublishGoodsInfo getGoodsCache(String id) {
        if(TextUtils.isEmpty(id)) return null;

        File file = buildGoodsFile(id);
        String json = mCacheMgr.readFromFile(file);

        return JsonSerializer.fromGoodsJson(json);
    }

    @Override
    public List<PublishGoodsInfo> getAllGoodsCache() {
        Log.v(TAG,"getAllGoodsCache()");
        File goodsFile = new File(getGoodsCacheDir());
        File[] files = goodsFile.listFiles();

        if(goodsFile.exists() && files != null && files.length > 0){
            List<PublishGoodsInfo> goodsList = new ArrayList<>();
            mCurrentRetrieved = (mCurrentRetrieved >= files.length) ? 0 : mCurrentRetrieved;
            // only retrieve 30 items a time
            int i;
            for(i = mCurrentRetrieved; i < (mCurrentRetrieved + MAX_ITEMS_TO_GET_A_TIME) &&
                    i < files.length; ++i){
                goodsList.add(getGoodsCache(files[i].getName()));
            }

            mCurrentRetrieved = i + MAX_ITEMS_TO_GET_A_TIME;

            return goodsList;
        }

        return null;
    }

    @Override
    public String getUserCachePath() {
        return getUserCacheDir();
    }

    @Override
    public String getGoodsCachePath() {
        return getGoodsCacheDir();
    }

    public String getImageCachePath() {
        return getImgCacheDir();
    }

    @Override
    public String getImageFilePath(String name, @CacheType String type) {
        return buildImageFile(name,type).getPath();
    }

    @Override
    public boolean isExpired(String id, @CacheType String type) {
        if(TextUtils.isEmpty(id)){
            return true;
        }

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

        return new File(new File(str),id);
    }

    private File buildGoodsFile(String id){
        String str = getGoodsCacheDir();

        return new File(new File(str),id);
    }

    private File buildUserDataFile(String id){
        String str = FILE_DIR_PARENT + File.separator +
                DIR_USER_DATA;

        return new File(new File(str),"123");
    }

    /*
     * for images, we keep them on external storage
     */
    private File buildImageFile(String imgName,@CacheType String type){
        String str = getImgCacheDir() + File.separator + type;

        return new File(new File(str),imgName);
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

    private String getImgCacheDir(){
        return mCacheDir.getPath() +
                File.separator + IMAGE_CACHE_NAME;
    }

    private static class CacheWriter implements Runnable{
        private final CacheManager cacheMgr;
        private final File fileToWrite;
        private final String fileContent;
        private boolean isUpdate = false;

        public CacheWriter(CacheManager cacheManager,File writer,String content,
                           boolean update){
            cacheMgr = cacheManager;
            fileToWrite = writer;
            fileContent = content;
            isUpdate = update;
        }

        @Override
        public void run() {
            cacheMgr.writeToFile(fileToWrite,fileContent,isUpdate);
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

    protected static class ImageDownloader implements Runnable{
        private File file;
        private String url;

        public ImageDownloader(File dest, String imgUrl){
            this.file = dest;
            this.url = imgUrl;
        }

        @Override
        public void run() {
            downloadImg(file,url);
        }

        private void downloadImg(File dest, String imgUrl){
            if(dest == null || imgUrl == null) return;

            ImageView iv = new ImageView(AppGlobals.getAppContext(),null);
            iv.setDrawingCacheEnabled(true);
            PicassoUtils.loadImage(iv,imgUrl);
            FileManager.saveBitmapToFile(dest,iv.getDrawingCache());

            Log.v(TAG,"downloadImg(): url = " + imgUrl);

/*            try {
                URL url = new URL(imgUrl);

                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                FileOutputStream fos = new FileOutputStream(dest);
                InputStream fis =  urlConnection.getInputStream();

                int bufferLen = 0;
                byte[] buffer= new byte[1024];

                while((bufferLen = fis.read(buffer)) > 0){
                    fos.write(buffer,0,bufferLen);
                }

                fos.close();
                fis.close();

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }*/
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
