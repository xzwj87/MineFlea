package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.github.xzwj87.mineflea.market.data.ResponseCode;
import com.github.xzwj87.mineflea.market.data.repository.DataRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.DiscoverGoodsView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
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
    private HashMap<String,UserInfo> mPublisherList;
    // mark an item to be liked
    private Hashtable<String,Boolean> mIsLiked;

    @Inject
    public DiscoverGoodsPresenterImpl(DataRepository repository){
        mRepo = repository;
        mGoodsList = new ArrayList<>();
        mPublisherList = new HashMap<>();
        mGoodsSet = new HashSet<>();
        mIsLiked = new Hashtable<>();
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
    public void addToMyFavor(int pos) {
        Log.v(TAG,"addToMyFavor()");
        PublishGoodsInfo goodsInfo = mGoodsList.get(pos);
        String id = goodsInfo.getId();
        if(mIsLiked.get(id) == null || !mIsLiked.get(id)) {
            goodsInfo.addFavorUser("unknown");
            mRepo.addToMyFavorites(goodsInfo);
            mIsLiked.put(id,true);
            mView.updateLikesView(pos,mGoodsList.get(pos).getStars());
        }
    }

    @Override
    public String getPublisherNickName(int pos) {
        if(mPublisherList.size() == 0) return null;

        String userId = mGoodsList.get(pos).getUserId();

        return (mPublisherList.get(userId)).getNickName();
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
        mGoodsSet = null;
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
                                // get user info
                                mRepo.getUserInfoById(goodsInfo.getUserId());
                            }
                        }
                    }
                    mView.onGetGoodsListDone(true);
                    break;
                case ResponseCode.RESP_GET_GOODS_LIST_ERROR:
                    mView.onGetGoodsListDone(false);
                    break;
                case ResponseCode.RESP_GET_USER_INFO_SUCCESS:
                    Log.v(TAG,"onComplete(): get user info success");
                    UserInfo user = (UserInfo)message.obj;
                    if(!mPublisherList.containsKey(user.getUserId())){
                        mPublisherList.put(user.getUserId(),user);
                    }
                    // we want to set refresh to false
                    mView.onGetGoodsListDone(true);
                    break;
                case ResponseCode.RESP_GET_USER_INFO_ERROR:
                    break;
                default:
                    break;
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
