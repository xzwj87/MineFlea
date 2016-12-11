package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;
import android.util.Log;

import com.github.xzwj87.mineflea.market.data.ResponseCode;
import com.github.xzwj87.mineflea.market.data.repository.DataRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.NearbyGoodsView;

import static com.github.xzwj87.mineflea.market.data.ResponseCode.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by jason on 9/27/16.
 */

@PerActivity
public class NearbyGoodsPresenterImpl extends NearbyGoodsPresenter {
    private static final String TAG = NearbyGoodsPresenter.class.getSimpleName();

    @Inject
    DataRepository mRepository;
    //private NearbyProtocol protocol;
    private NearbyGoodsView mView;

    private List<PublishGoodsInfo> mGoodsList;
    private HashSet<String> mGoodsSet;
    private List<UserInfo> mPublisherList;

    @Inject
    public NearbyGoodsPresenterImpl(DataRepository repository){
        mRepository = repository;
    }

    @Override
    public void loadDataFromServer() {
        mRepository.getAllGoods();
        Log.e(TAG, "getAllGoods()");
    }

    @Override
    public void init() {
        //protocol = new NearbyProtocol();
        mRepository.init();
        mGoodsList = new ArrayList<>();
        mPublisherList = new ArrayList<>();
        mGoodsSet = new HashSet<>();
        mRepository.registerCallBack(PRESENTER_GOODS_LIST, new UserGoodsPresenterCallback());
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
            Log.v(TAG,"onComplete()");
            int resp = message.what;
            switch (resp) {
                case ResponseCode.RESP_GET_GOODS_LIST_SUCCESS:
                    // sync cache/cloud callback
                    synchronized (this) {
                        List<PublishGoodsInfo> goods = (List<PublishGoodsInfo>) message.obj;
                        if (goods != null) {
                            for (int i = 0; i < goods.size(); ++i) {
                                PublishGoodsInfo goodsInfo = goods.get(i);
                                if (!mGoodsSet.contains(goodsInfo.getId())) {
                                    mGoodsSet.add(goodsInfo.getId());
                                    mGoodsList.add(goodsInfo);
                                }
                            }
                        }
                    }
                    mView.updateMarkerDisplay(mGoodsList);
                    break;
                case ResponseCode.RESP_GET_GOODS_LIST_ERROR:
                    //mView.onGetGoodsListDone(false);
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onNext(Message message) {}
        @Override
        public void onError(Throwable e) {}
    }
}
