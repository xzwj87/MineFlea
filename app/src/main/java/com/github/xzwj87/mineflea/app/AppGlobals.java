package com.github.xzwj87.mineflea.app;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVOSCloud;
import com.github.xzwj87.mineflea.market.internal.di.HasComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.AppComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.DaggerAppComponent;
import com.github.xzwj87.mineflea.market.internal.di.module.AppModule;

import dagger.Component;

/**
 * Created by JasonWang on 2016/9/19.
 */
public class AppGlobals extends Application
        implements HasComponent<AppComponent>{

    private static Context sContext;
    private static final String LEAN_CLOUD_APP_ID = "4OaElXuRPCVDqxMLyXIRk4Ai-gzGzoHsz";
    private static final String LEAN_CLOUD_APP_KEY = "gEtmxOIxF0LrCT0jJaHSL9uU";

    private AppComponent mAppComponent;

    @Override
    public void onCreate(){
        super.onCreate();

        sContext = getApplicationContext();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        initLeanCloudService();
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
}
