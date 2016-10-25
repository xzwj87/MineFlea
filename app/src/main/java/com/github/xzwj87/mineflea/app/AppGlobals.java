package com.github.xzwj87.mineflea.app;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;

import com.amap.api.maps2d.MapsInitializer;
import com.avos.avoscloud.AVOSCloud;
import com.github.xzwj87.mineflea.market.internal.di.HasComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.AppComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.DaggerAppComponent;
import com.github.xzwj87.mineflea.market.internal.di.module.AppModule;

import java.io.File;

/**
 * Created by JasonWang on 2016/9/19.
 */
public class AppGlobals extends Application
        implements HasComponent<AppComponent>{

    private static final String TAG = AppGlobals.class.getSimpleName();

    private static Context sContext;
    private static final String LEAN_CLOUD_APP_ID = "4OaElXuRPCVDqxMLyXIRk4Ai-gzGzoHsz";
    private static final String LEAN_CLOUD_APP_KEY = "gEtmxOIxF0LrCT0jJaHSL9uU";


    private AppComponent mAppComponent;

    public static final String FILE_DIR_PARENT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MinFlea";
    public static final String FILE_DIR_MISC = FILE_DIR_PARENT + "/misc";

    private static int mainThreadId;
    private static Handler handler;

    @Override
    public void onCreate(){
        super.onCreate();

        mainThreadId = android.os.Process.myTid();
        handler = new Handler();

        sContext = getApplicationContext();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        initLeanCloudService();
        initFileDir();
        initMap();
    }

    private void initMap() {
        try {
            MapsInitializer.initialize(sContext);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public AppGlobals(){
        super();
    }

    public static Context getAppContext(){
        return sContext;
    }

    @Override
    public AppComponent getComponent(){
        return mAppComponent;
    }

    private void initLeanCloudService(){
        AVOSCloud.initialize(this,LEAN_CLOUD_APP_ID,LEAN_CLOUD_APP_KEY);
    }

    private void initFileDir(){
        File file = new File(FILE_DIR_MISC);

        Boolean isSuccess = true;
        if(!file.exists()){
            isSuccess = file.mkdirs();
        }

        if(isSuccess){
            Log.v(TAG,"create directory:" + FILE_DIR_MISC + " successfully");
        }else {
            Log.v(TAG, "fail to create directory " + FILE_DIR_MISC);
        }

    }

    public static int getMainThreadId(){
        return mainThreadId;
    }

    public static Handler getHandler(){
        return handler;
    }
}
