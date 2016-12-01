package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;
import android.util.Log;

import com.github.xzwj87.mineflea.market.data.ResponseCode;
import com.github.xzwj87.mineflea.market.data.repository.DataRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.DiscoverGoodsView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by jason on 11/12/16.
 */

@PerActivity
public class DiscoverGoodsPresenterImpl implements DiscoverGoodsPresenter{
    private static final String TAG = DiscoverGoodsPresenter.class.getSimpleName();

    @Inject
    DataRepository mRepo;
    private DiscoverGoodsView mView;
    // a id set to check whether a goods has been retrieved already
    private HashSet<String> mGoodsSet;
    private List<PublishGoodsInfo> mGoodsList;
    private List<UserInfo> mPublisherList;
    private final Object mCompleteLock;

    @Inject
    public DiscoverGoodsPresenterImpl(DataRepository repository){
        mRepo = repository;
        mGoodsList = new ArrayList<>();
        mPublisherList = new ArrayList<>();
        mGoodsSet = new HashSet<>();
        mCompleteLock = new Object();
    }

    @Override
    public void getGoodsList() {
        mRepo.getAllGoods();
    }

    @Override
    public PublishGoodsInfo getItemAtPos(int pos) {
        if(mGoodsList.size() == 0) return  null;

        return mGoodsList.get(pos);
    }

    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }

    @Override
    public String getPublisherHeadIcon(int pos) {
        if(mPublisherList.size() == 0) return null;

        return mPublisherList.get(pos).getHeadIconUrl();
    }

    @Override
    public String getPublisherNickName(int pos) {
        if(mPublisherList.size() == 0) return null;

        return mPublisherList.get(pos).getNickName();
    }

    @Override
    public void init() {
        mRepo.init();
        mRepo.registerCallBack(PRESENTER_GOODS_LIST,new DiscoverPresenterCallback());
        mRepo.getAllGoods();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        mRepo.unregisterCallback(PRESENTER_GOODS_LIST);
        mGoodsList = null;
        mPublisherList = null;
    }

    @Override
    public void setView(BaseView view) {
        mView = (DiscoverGoodsView)view;
    }

    private class DiscoverPresenterCallback implements PresenterCallback{
        @SuppressWarnings("unchecked")
        @Override
        public void onComplete(Message message) {
            Log.v(TAG,"onComplete()");
            // sync cache/cloud callback
            synchronized (mCompleteLock){
                List<PublishGoodsInfo> goods = (List<PublishGoodsInfo>)message.obj;
                if(goods != null) {
                    for (int i = 0; i < goods.size(); ++i) {
                        PublishGoodsInfo goodsInfo = goods.get(i);
                        if(!mGoodsSet.contains(goodsInfo.getId())){
                            mGoodsSet.add(goodsInfo.getId());
                            mGoodsList.add(goodsInfo);
                        }
                    }
                }
            }

            if(message.what == ResponseCode.RESP_GET_GOODS_LIST_SUCCESS){
                mView.onGetGoodsListDone(true);
            }else{
                mView.onGetGoodsListDone(false);
            }
        }

        @Override
        public void onNext(Message message) {

        }

        @Override
        public void onError(Throwable e) {

        }
    }
}
