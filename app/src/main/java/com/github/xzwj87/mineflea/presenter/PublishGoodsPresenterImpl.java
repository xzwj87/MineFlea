package com.github.xzwj87.mineflea.presenter;

import com.github.xzwj87.mineflea.data.RepoResponseCode;
import com.github.xzwj87.mineflea.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.executor.JobExecutor;
import com.github.xzwj87.mineflea.interactor.DefaultSubscriber;
import com.github.xzwj87.mineflea.interactor.PublishGoodsUseCase;
import com.github.xzwj87.mineflea.interactor.UseCase;
import com.github.xzwj87.mineflea.model.GoodsModel;
import com.github.xzwj87.mineflea.model.PublisherModel;
import com.github.xzwj87.mineflea.ui.PublishGoodsView;

/**
 * Created by jason on 9/27/16.
 */

public class PublishGoodsPresenterImpl implements PublishGoodsPresenter {
    public static final String TAG = PublishGoodsPresenterImpl.class.getSimpleName();

    private PublishGoodsUseCase mUseCase;
    private PublishGoodsView mView;

    public PublishGoodsPresenterImpl(PublishGoodsView view){
        mView = view;
    }

    @Override
    public void publishGoods(GoodsModel goods) {

        mUseCase.setData(goods);
        mUseCase.execute(new PublishGoodsSubscriber());
    }

    @Override
    public void onCreate() {
        mUseCase = new PublishGoodsUseCase(new MineFleaRepository(),
                new JobExecutor());
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }


    private class PublishGoodsSubscriber extends DefaultSubscriber<RepoResponseCode>{

        @Override
        public void onCompleted(){}

        @Override
        public void onNext(RepoResponseCode responseCode){

        }

        @Override
        public void onError(Throwable e){

        }
    }

}
