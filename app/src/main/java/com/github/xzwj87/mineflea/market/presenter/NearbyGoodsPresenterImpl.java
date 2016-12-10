package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.data.repository.DataRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.NearbyGoodsView;
import com.github.xzwj87.mineflea.utils.NearbyProtocol;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by jason on 9/27/16.
 */

@PerActivity
public class NearbyGoodsPresenterImpl extends NearbyGoodsPresenter {

    @Inject
    DataRepository mRepository;
    private NearbyProtocol protocol;
    private NearbyGoodsView mView;

    private List<PublishGoodsInfo> mGoodsList;

    @Inject
    public NearbyGoodsPresenterImpl(DataRepository repository){
        mRepository = repository;
    }

    @Override
    public void loadDataFromServer() {
        mRepository.getAllGoods();
    }

    @Override
    public void init() {
        protocol = new NearbyProtocol();
        mRepository.init();
        mRepository.registerCallBack(PRESENTER_PUBLISH, new UserGoodsPresenterCallback());
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        mRepository.unregisterCallback(PRESENTER_GOODS_LIST);
        mGoodsList = null;
        mRepository = null;
    }

    @Override
    public void setView(BaseView view) {
        mView = (NearbyGoodsView) view;
    }


    private class UserGoodsPresenterCallback implements PresenterCallback {
        @SuppressWarnings("unchecked")
        @Override
        public void onComplete(Message message) {
            if(message.obj != null){
                mGoodsList = (List<PublishGoodsInfo>)message.obj;
                mView.updateMarkerDisplay(mGoodsList);
            }
        }

        @Override
        public void onNext(Message message) {}
        @Override
        public void onError(Throwable e) {}
    }
}
