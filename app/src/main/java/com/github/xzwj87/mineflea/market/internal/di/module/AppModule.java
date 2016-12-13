package com.github.xzwj87.mineflea.market.internal.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jason on 9/29/16.
 */

@Module
public class AppModule {

    private Application mApp;

    public AppModule(Application application){
        mApp = application;
    }

    @Singleton
    @Provides
    Application provideApplication(){
        return mApp;
    }

    @Singleton
    @Provides
    Context provideApplicationContext(){
        return mApp.getApplicationContext();
    }
}
