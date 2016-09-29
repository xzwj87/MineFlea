package com.github.xzwj87.mineflea.market.internal.di.module;

import android.app.Activity;
import android.content.Context;

import com.github.xzwj87.mineflea.market.internal.di.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jason on 9/29/16.
 */

@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(Activity activity){
        mActivity = activity;
    }

    @PerActivity
    @Provides
    public Activity provideActivity(){
        return mActivity;
    }

    @PerActivity
    @Provides
    Context provideActivityContext(){
        return mActivity.getApplicationContext();
    }
}
