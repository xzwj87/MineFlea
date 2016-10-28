package com.github.xzwj87.mineflea.market.internal.di.module;

import com.github.xzwj87.mineflea.market.data.local.MineFleaLocalSource;
import com.github.xzwj87.mineflea.market.data.remote.MineFleaRemoteSource;
import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.executor.JobExecutor;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.net.NetDataApiImpl;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jason on 9/29/16.
 */

@Module
public class MarketModule {

    public MarketModule(){}

    @PerActivity @Provides
    @Named("jobExecutor")
    JobExecutor provideJobExecutor(){
        return new JobExecutor();
    }

    @PerActivity @Provides
    @Named("netApi")
    NetDataApiImpl provideNetApi(){
        return new NetDataApiImpl();
    }

    @PerActivity @Provides
    @Named("localResource")
    MineFleaLocalSource provideLocalResource(){
        return new MineFleaLocalSource();
    }

    @PerActivity @Provides
    @Named("remoteResource")
    MineFleaRemoteSource provideRemoteResource(NetDataApiImpl netApi){
        return new MineFleaRemoteSource(netApi);
    }

    @PerActivity @Provides
    @Named("dataRepository")
    MineFleaRepository provideRepository(MineFleaLocalSource localSource,
                                         MineFleaRemoteSource cloudSource){
        return new MineFleaRepository(localSource,cloudSource);
    }
}
