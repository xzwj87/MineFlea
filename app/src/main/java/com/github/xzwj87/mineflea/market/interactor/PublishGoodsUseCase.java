package com.github.xzwj87.mineflea.market.interactor;

import android.os.Message;
import android.util.Log;

import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.executor.JobExecutor;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;


import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jason on 9/28/16.
 */

@PerActivity
public class PublishGoodsUseCase extends UseCase implements PublishCallBack{

    private static final String TAG = PublishGoodsUseCase.class.getSimpleName();

    private MineFleaRepository mRepository;
    private PublishGoodsInfo mData;
    private Subscriber mSubscriber;

    @Inject
    public PublishGoodsUseCase(@Named("dataRepository") MineFleaRepository repository,
                               @Named("jobExecutor") JobExecutor executor){
        super(executor);

        mRepository = repository;
        mRepository.setPublishCallback(this);
    }

    @Override
    public void execute(Subscriber useCaseSubscriber){
        Log.v(TAG,"execute()");
        mSubscriber = useCaseSubscriber;

        mRepository.publishGoods(mData);
    }

    public void setData(PublishGoodsInfo data){
        mData = data;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onPublishComplete(final Message message) {
        Log.v(TAG,"onPublishComplete(): " + message.obj);

        Observable observable = Observable.<Message>create(new Observable.OnSubscribe<Message>() {
            @Override
            public void call(Subscriber<? super Message> subscriber) {
                subscriber.onNext(message);
            }
        });

        observable.subscribeOn(Schedulers.from(mExecutor))
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(mSubscriber);
    }
}
