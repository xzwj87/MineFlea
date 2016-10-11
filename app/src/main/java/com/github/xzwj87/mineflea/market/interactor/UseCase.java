package com.github.xzwj87.mineflea.market.interactor;

import com.github.xzwj87.mineflea.market.executor.JobExecutor;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by jason on 9/27/16.
 */

public abstract class UseCase {

    protected final JobExecutor mExecutor;

    protected Subscription mSubscription = Subscriptions.empty();

    public UseCase(JobExecutor executor){
        mExecutor = executor;
    }

    public abstract void setData(PublishGoodsInfo info);

    public abstract void execute(Subscriber subscriber);

    public void unSubscribe(){
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

}
