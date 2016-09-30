package com.github.xzwj87.mineflea.app;

import android.app.Application;
import android.content.Context;

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

    private AppComponent mAppComponent;

    @Override
    public void onCreate(){
        super.onCreate();

        sContext = getApplicationContext();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
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
}
