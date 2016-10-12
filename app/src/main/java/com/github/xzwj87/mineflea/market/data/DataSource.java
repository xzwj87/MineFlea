package com.github.xzwj87.mineflea.market.data;

import android.os.Message;

import com.github.xzwj87.mineflea.market.model.PublisherInfo;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;

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
    void publishGoods(PublishGoodsInfo goods);

    /**
     * query the detail of a goods
     * @source: local
     */
    Observable<PublishGoodsInfo> queryPublishedGoodsDetail(long id);

    Observable<List<PublishGoodsInfo>> queryPublishedGoodsList();

    /**
     * add goods to a favorite one
     * @source: local
     */
    Observable<RepoResponseCode> favorGoods(PublishGoodsInfo goods);

    /**
     * query the detail info of a favored goods
     * @source: local
     */
    Observable<PublishGoodsInfo> queryFavorGoodsDetail(long id);

    Observable<List<PublishGoodsInfo>> queryFavorGoodsList();


    /**
     * query the info of a publisher
     * @source: remote
     */
    Observable<PublisherInfo> queryPublisherDetail(long id);

    Observable<List<PublisherInfo>> queryPublisherList();

    /**
     * query latest goods list
     * @source: remote
     */
    Observable<List<PublishGoodsInfo>> queryLatestGoodsList();

    /**
     * follow some publisher
     * @source: remote
     */
    Observable<RepoResponseCode> followPublisher(PublisherInfo publisher);
}
