package com.github.xzwj87.mineflea.data.remote;

import android.util.Log;
import android.widget.TabHost;

import com.github.xzwj87.mineflea.data.DataSource;
import com.github.xzwj87.mineflea.model.GoodsModel;
import com.github.xzwj87.mineflea.model.PublisherModel;
import com.github.xzwj87.mineflea.net.NetDataApi;
import com.github.xzwj87.mineflea.net.NetDataApiImpl;

import java.util.List;

import rx.Observable;

/**
 * Created by JasonWang on 2016/9/20.
 */
public class MineFleaCloudSource implements DataSource{
    public static final String TAG = MineFleaCloudSource.class.getSimpleName();

    private NetDataApi mNetApi;

    public MineFleaCloudSource(NetDataApi netApi){
        mNetApi = netApi;
    }

    /**
     * release goods
     *
     * @param goods
     * @source: local and remote
     */
    @Override
    public void publishGoods(GoodsModel goods) {
        Log.v(TAG,"publishGoods(): goods = " + goods);

        mNetApi.publishGoods(goods);
    }

    /**
     * query the info of a publisher
     *
     * @param id
     * @source: remote
     */
    @Override
    public Observable<PublisherModel> queryPublisherDetail(long id) {
        Log.v(TAG,"queryPublisherDetail(): id = " + id);

        return mNetApi.queryPublisherDetail(id);
    }

    /**
     * query latest goods list
     *
     * @source: remote
     */
    @Override
    public Observable<List<GoodsModel>> queryLatestGoodsList() {
        Log.v(TAG,"queryLatestGoodsList()");

        return mNetApi.queryLatestGoodsList();
    }

    /**
     * follow some publisher
     *
     * @param publisher
     * @source: remote
     */
    @Override
    public void followPublisher(PublisherModel publisher) {
        Log.v(TAG,"followPublisher(): publisher = " + publisher);

        mNetApi.followPublisher(publisher);
    }

    /**
     * query the detail of a goods
     *
     * @param id
     * @source: local
     */
    @Override
    public Observable<GoodsModel> queryPublishedGoodsDetail(long id) {
        throw new UnsupportedOperationException(
                "queryPublishedGoodsDetail() should be called in local data base");
    }

    @Override
    public Observable<List<GoodsModel>> queryPublishedGoodsList() {
        throw new UnsupportedOperationException(
                "queryPublishedGoodsList() should be called in local data base");
    }

    /**
     * add goods to a favorite one
     *
     * @param goods
     * @source: local
     */
    @Override
    public void favorGoods(GoodsModel goods) {
        throw new UnsupportedOperationException("favorGoods() " +
                "shouldb be called in local data base");
    }

    /**
     * query the detail info of a favored goods
     *
     * @param id
     * @source: local
     */
    @Override
    public Observable<GoodsModel> queryFavorGoodsDetail(long id) {
        throw new UnsupportedOperationException(
                "queryFavorGoodsDetail() should be called in local data base");
    }

    @Override
    public Observable<List<GoodsModel>> queryFavorGoodsList() {
        throw new UnsupportedOperationException(
                "queryFavorGoodsList() should be called in local data base");
    }
}
