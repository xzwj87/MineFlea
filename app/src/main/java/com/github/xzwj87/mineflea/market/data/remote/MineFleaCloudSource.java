package com.github.xzwj87.mineflea.market.data.remote;

import android.util.Log;

import com.github.xzwj87.mineflea.market.data.DataSource;
import com.github.xzwj87.mineflea.market.data.RepoResponseCode;
import com.github.xzwj87.mineflea.market.model.GoodsModel;
import com.github.xzwj87.mineflea.market.model.PublisherModel;
import com.github.xzwj87.mineflea.market.net.NetDataApi;
import com.github.xzwj87.mineflea.market.net.NetDataApiImpl;

import java.util.List;

import rx.Observable;

/**
 * Created by JasonWang on 2016/9/20.
 */
public class MineFleaCloudSource implements DataSource{
    public static final String TAG = MineFleaCloudSource.class.getSimpleName();

    private NetDataApi mNetApi;
    private static MineFleaCloudSource sInstance;

    private MineFleaCloudSource(NetDataApi netApi){
        mNetApi = netApi;
    }

    public static MineFleaCloudSource getInstance(){
        if(sInstance == null){
            sInstance = new MineFleaCloudSource(new NetDataApiImpl());
        }

        return sInstance;
    }
    /**
     * release goods
     *
     * @param goods
     * @source: local and remote
     */
    @Override
    public Observable<RepoResponseCode> publishGoods(GoodsModel goods) {
        Log.v(TAG,"publishGoods(): goods = " + goods);

        return mNetApi.publishGoods(goods);
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

    @Override
    public Observable<List<PublisherModel>> queryPublisherList() {
        throw new UnsupportedOperationException("queryPublisherList() should not be" +
                " called in cloud resource");
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
    public Observable<RepoResponseCode> followPublisher(PublisherModel publisher) {
        Log.v(TAG,"followPublisher(): publisher = " + publisher);

            return mNetApi.followPublisher(publisher);
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

    // TODO: it need to notify the Server about the state of this goods
    @Override
    public Observable<RepoResponseCode> favorGoods(GoodsModel goods) {
        throw new UnsupportedOperationException("favorGoods() " +
                "should be called in local data base");
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
