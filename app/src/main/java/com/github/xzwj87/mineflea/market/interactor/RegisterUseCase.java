package com.github.xzwj87.mineflea.market.interactor;

import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.executor.JobExecutor;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.UserInfo;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Provides;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jason on 10/15/16.
 */

@PerActivity
public class RegisterUseCase extends UseCase implements RegisterCallBack{
    private static final String TAG = RegisterUseCase.class.getSimpleName();

    private MineFleaRepository mRepository;
    private Subscriber mSubscriber;

    @Inject
    public RegisterUseCase(@Named("dataRepository") MineFleaRepository repository,
            @Named("jobExecutor")JobExecutor jobExecutor){
        super(jobExecutor);

        mRepository = repository;
    }

    @Override
    public void execute(Subscriber subscriber) {
        mSubscriber = subscriber;

        init();
    }

    public void register(UserInfo userInfo){
        mRepository.register(userInfo);
    }

    private void init(){
        mRepository.setRegisterCallback(this);
        mRepository.init();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onRegisterComplete(final Message message) {
        Log.v(TAG,"onRegisterComplete()");

        Observable observable = Observable.<Message>create(new Observable.OnSubscribe() {
            @Override
            public void call(Object o) {
                mSubscriber.onNext(message);
            }
        });

        observable.subscribeOn(Schedulers.from(mExecutor))
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(mSubscriber);
    }
}
