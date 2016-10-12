package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;
import android.util.Log;

import com.github.xzwj87.mineflea.market.interactor.DefaultSubscriber;
import com.github.xzwj87.mineflea.market.interactor.UseCase;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.ui.PublishGoodsView;

import java.util.List;

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
    private PublishGoodsInfo mGoodsInfo;

    @Inject
    public PublishGoodsPresenterImpl(@Named("publishGoods") UseCase useCase){
        mPublishGoodsUseCase = useCase;
    }

    public void setPublishGoodsView(PublishGoodsView view){
        mView = view;
    }

    @Override
    public void init() {
        mGoodsInfo = new PublishGoodsInfo();
    }

    @Override
    public void publishGoods() {

        mPublishGoodsUseCase.setData(mGoodsInfo);
        mPublishGoodsUseCase.execute(new PublishGoodsSubscriber());

        mView.finishView();
    }

    @Override
    public void setGoodsName(String name) {
        mGoodsInfo.setName(name);
    }

    @Override
    public void setGoodsLowPrice(double price) {
        mGoodsInfo.setLowerPrice(price);
    }

    @Override
    public void setGoodsHighPrice(double price) {
        mGoodsInfo.setHighPrice(price);
    }

    @Override
    public void setGoodsNote(String note) {
        mGoodsInfo.setNote(note);
    }

    @Override
    public void setGoodsImgUrl(List<String> urls) {
        mGoodsInfo.setImageUri(urls);
    }

    @Override
    public void onPause() {
        mPublishGoodsUseCase.unSubscribe();
    }

    @Override
    public void onDestroy() {
        mGoodsInfo = null;
    }


    private class PublishGoodsSubscriber extends DefaultSubscriber<Message>{

        @Override
        public void onCompleted(){}

        @Override
        public void onNext(Message message){
            Log.v(TAG,"onNext(): message =  " + message.arg1);

        }

        @Override
        public void onError(Throwable e){

        }
    }

}
