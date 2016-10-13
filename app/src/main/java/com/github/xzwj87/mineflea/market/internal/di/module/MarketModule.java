package com.github.xzwj87.mineflea.market.internal.di.module;

import android.content.Context;

import com.github.xzwj87.mineflea.market.data.local.MineFleaLocalSource;
import com.github.xzwj87.mineflea.market.data.remote.MineFleaCloudSource;
import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.executor.JobExecutor;
import com.github.xzwj87.mineflea.market.interactor.PublishGoodsUseCase;
import com.github.xzwj87.mineflea.market.interactor.UseCase;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.net.NetDataApi;
import com.github.xzwj87.mineflea.market.net.NetDataApiImpl;

import javax.inject.Named;
import javax.inject.Singleton;

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
    MineFleaCloudSource provideRemoteResource(NetDataApiImpl netApi){
        return new MineFleaCloudSource(netApi);
    }

    @PerActivity @Provides
    @Named("dataRepository")
    MineFleaRepository provideRepository(MineFleaLocalSource localSource,
                                         MineFleaCloudSource cloudSource){
        return new MineFleaRepository(localSource,cloudSource);
    }

    @PerActivity @Provides
    @Named("publishGoods")
    UseCase providePublishUseCase(MineFleaRepository repository,JobExecutor executor){
        return new PublishGoodsUseCase(repository,executor);
    }


}
