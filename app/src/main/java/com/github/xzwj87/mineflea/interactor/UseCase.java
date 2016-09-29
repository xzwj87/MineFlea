package com.github.xzwj87.mineflea.interactor;

import com.github.xzwj87.mineflea.executor.JobExecutor;

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

    private final JobExecutor mExecutor;

    private Subscription mSubscription = Subscriptions.empty();

    public UseCase(JobExecutor executor){
        mExecutor = executor;
    }

    public abstract Observable buildUseCaseObservable();

    //@SuppressWarnings("unchecked")
    public void execute(Subscriber useCaseSubscriber){
        mSubscription = buildUseCaseObservable()
                .subscribeOn(Schedulers.from(mExecutor))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void unSubscribe(){
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

}
