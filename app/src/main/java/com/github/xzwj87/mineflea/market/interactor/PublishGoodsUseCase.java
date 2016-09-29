package com.github.xzwj87.mineflea.market.interactor;

import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.executor.JobExecutor;
import com.github.xzwj87.mineflea.market.model.GoodsModel;


import rx.Observable;

/**
 * Created by jason on 9/28/16.
 */

public class PublishGoodsUseCase extends UseCase {

    private MineFleaRepository mRepository;
    private GoodsModel mData;

    public PublishGoodsUseCase(MineFleaRepository repository,
                               JobExecutor executor){
        super(executor);

        mRepository = repository;
    }

    @Override
    public Observable buildUseCaseObservable() {
        return mRepository.publishGoods(mData);
    }

    public void setData(GoodsModel data){
        mData = data;
    }
}
