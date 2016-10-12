package com.github.xzwj87.mineflea.market.data.repository;

import android.os.Message;
import android.util.Log;

import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.data.RepoResponseCode;
import com.github.xzwj87.mineflea.market.data.local.MineFleaLocalSource;
import com.github.xzwj87.mineflea.market.data.remote.MineFleaCloudSource;
import com.github.xzwj87.mineflea.market.interactor.PublishCallBack;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.PublisherInfo;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by JasonWang on 2016/9/20.
 */
public class MineFleaRepository implements BaseRepository,MineFleaCloudSource.CloudSourceCallback{
    public static final String TAG = MineFleaRepository.class.getSimpleName();

    private MineFleaLocalSource mLocalSrc;
    private MineFleaCloudSource mCloudSrc;
    private PublishCallBack mCb;

    @Inject
    public MineFleaRepository(){
        mLocalSrc = MineFleaLocalSource.getInstance(AppGlobals.getAppContext());
        mCloudSrc = MineFleaCloudSource.getInstance();
        mCloudSrc.setCloudCallback(this);
    }

    public void setPublishCallback(PublishCallBack callBack){
        mCb = callBack;
    }

    @Override
    public void publishGoods(PublishGoodsInfo goods) {
        Log.v(TAG,"publishGoods(): goods = " + goods);

        //mLocalSrc.publishGoods(goods);

        mCloudSrc.publishGoods(goods);
    }

    @Override
    public Observable<PublishGoodsInfo> getPublishedGoodsDetail(long id) {
        Log.v(TAG,"getPublishedGoodsDetail(): id = " + id);

        return mLocalSrc.queryPublishedGoodsDetail(id);
    }

    @Override
    public Observable<List<PublishGoodsInfo>> getPublishedGoodsList() {
        Log.v(TAG,"getPublishedGoodsList()");

        return mLocalSrc.queryPublishedGoodsList();
    }

    @Override
    public Observable<List<PublishGoodsInfo>> getLatestGoodsList() {
        Log.v(TAG,"getLatestGoodsList()");

        return mCloudSrc.queryLatestGoodsList();
    }

    @Override
    public Observable<RepoResponseCode> favorGoods(PublishGoodsInfo goods) {
        Log.v(TAG,"favorGoods(): goods = " + goods);

        //TODO:consider both response
        mLocalSrc.favorGoods(goods);
        return mCloudSrc.favorGoods(goods);
    }

    @Override
    public Observable<PublishGoodsInfo> getFavorGoodsDetail(long id) {
        Log.v(TAG,"getFavorGoodsDetail(): id = " + id);

        return mLocalSrc.queryFavorGoodsDetail(id);
    }

    @Override
    public Observable<List<PublishGoodsInfo>> getFavorGoodsList() {
        Log.v(TAG,"getFavorGoodsList()");

        return mLocalSrc.queryFavorGoodsList();
    }


    @Override
    public Observable<PublisherInfo> getPublisherDetail(long id) {
        Log.v(TAG,"getPublisherDetail(): id = " + id);

        return mCloudSrc.queryPublisherDetail(id);
    }

    @Override
    public Observable<PublisherInfo> getFavorPublisherDetail(long id) {
        Log.v(TAG,"getFavorPublisherDetail() id = " + id);

        return mLocalSrc.queryPublisherDetail(id);
    }

    @Override
    public Observable<List<PublisherInfo>> getFavorPublisherList() {
        Log.v(TAG,"getFavorPublisherList()");

        return mLocalSrc.queryPublisherList();
    }

    @Override
    public Observable<RepoResponseCode> followPublisher(PublisherInfo publisher) {
        return null;
    }

    @Override
    public void publishComplete(Message message) {
        Log.v(TAG,"publishComplete()");
        mCb.onPublishComplete(message);
    }
}
