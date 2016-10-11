package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;
import android.util.Log;

import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.executor.JobExecutor;
import com.github.xzwj87.mineflea.market.interactor.DefaultSubscriber;
import com.github.xzwj87.mineflea.market.interactor.PublishGoodsUseCase;
import com.github.xzwj87.mineflea.market.interactor.UseCase;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.ui.PublishGoodsView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by jason on 9/27/16.
 */

@PerActivity
public class PublishGoodsPresenterImpl implements PublishGoodsPresenter {
    public static final String TAG = PublishGoodsPresenterImpl.class.getSimpleName();

    private UseCase mPublishGoodsUseCase;
    private PublishGoodsView mView;

    @Inject
    public PublishGoodsPresenterImpl(@Named("publishGoods") UseCase useCase){
        mPublishGoodsUseCase = useCase;
    }

    public void setPublishGoodsView(PublishGoodsView view){
        mView = view;
    }

    @Override
    public void init() {

    }

    @Override
    public void publishGoods(PublishGoodsInfo goods) {

        mPublishGoodsUseCase.setData(goods);
        mPublishGoodsUseCase.execute(new PublishGoodsSubscriber());

        mView.finishView();
    }

    @Override
    public void onCreate() {
/*        mPublishGoodsUseCase = new PublishGoodsUseCase(new MineFleaRepository(),
                new JobExecutor());*/
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }


    private class PublishGoodsSubscriber extends DefaultSubscriber<Message>{

        @Override
        public void onCompleted(){}

        @Override
        public void onNext(Message message){
            Log.v(TAG,"onNext(): message =  " + message);
            //mView.onPublishComplete(responseCode);
        }

        @Override
        public void onError(Throwable e){

        }
    }

}
