package com.github.xzwj87.mineflea.market.internal.di.module;

import com.github.xzwj87.mineflea.market.data.cache.CacheManager;
import com.github.xzwj87.mineflea.market.data.cache.FileCache;
import com.github.xzwj87.mineflea.market.data.cache.FileCacheImpl;
import com.github.xzwj87.mineflea.market.data.remote.MineFleaRemoteSource;
import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.executor.JobExecutor;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jason on 9/29/16.
 */

@Module
public class MarketModule {

    public MarketModule() {
    }
}
