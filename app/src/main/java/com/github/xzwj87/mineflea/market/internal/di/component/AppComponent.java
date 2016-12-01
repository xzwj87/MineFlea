package com.github.xzwj87.mineflea.market.internal.di.component;

import android.app.Application;
import android.content.Context;

import com.github.xzwj87.mineflea.market.data.cache.FileCacheImpl;
import com.github.xzwj87.mineflea.market.data.remote.RemoteDataSource;
import com.github.xzwj87.mineflea.market.data.repository.DataRepository;
import com.github.xzwj87.mineflea.market.executor.JobExecutor;
import com.github.xzwj87.mineflea.market.internal.di.module.AppModule;
import com.github.xzwj87.mineflea.market.ui.activity.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by jason on 9/29/16.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(BaseActivity activity);

    Application application();
    Context context();
    DataRepository mineFleaRepository();
    JobExecutor jobExecutor();
    RemoteDataSource mineFleaRemoteSource();
    FileCacheImpl fileCacheImpl();
}
