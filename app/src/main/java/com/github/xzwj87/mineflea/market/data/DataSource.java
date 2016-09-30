package com.github.xzwj87.mineflea.market.data;

import com.github.xzwj87.mineflea.market.model.PublisherModel;
import com.github.xzwj87.mineflea.market.model.GoodsModel;

import java.util.List;

import rx.Observable;

/**
 * Created by JasonWang on 2016/9/20.
 */
public interface DataSource {

    /**
     * release goods
     * @source: local and remote
     */
    Observable<RepoResponseCode> publishGoods(GoodsModel goods);

    /**
     * query the detail of a goods
     * @source: local
     */
    Observable<GoodsModel> queryPublishedGoodsDetail(long id);

    Observable<List<GoodsModel>> queryPublishedGoodsList();

    /**
     * add goods to a favorite one
     * @source: local
     */
    Observable<RepoResponseCode> favorGoods(GoodsModel goods);

    /**
     * query the detail info of a favored goods
     * @source: local
     */
    Observable<GoodsModel> queryFavorGoodsDetail(long id);

    Observable<List<GoodsModel>> queryFavorGoodsList();


    /**
     * query the info of a publisher
     * @source: remote
     */
    Observable<PublisherModel> queryPublisherDetail(long id);

    Observable<List<PublisherModel>> queryPublisherList();

    /**
     * query latest goods list
     * @source: remote
     */
    Observable<List<GoodsModel>> queryLatestGoodsList();

    /**
     * follow some publisher
     * @source: remote
     */
    Observable<RepoResponseCode> followPublisher(PublisherModel publisher);
}