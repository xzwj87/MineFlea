package com.github.xzwj87.mineflea.market.data.repository;

import android.os.Message;

import com.github.xzwj87.mineflea.market.data.RepoResponseCode;
import com.github.xzwj87.mineflea.market.interactor.UseCase;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.PublisherInfo;

import java.util.List;

import rx.Observable;

/**
 * Created by jason on 10/11/16.
 */

public interface BaseRepository {
    /**
     * publish a goods
     */
    void publishGoods(PublishGoodsInfo goods);

    /**
     * get a published goods detail by its id
     */
    Observable<PublishGoodsInfo> getPublishedGoodsDetail(long id);

    /**
     * get published goods list
     */
    Observable<List<PublishGoodsInfo>> getPublishedGoodsList();

    /**
     * get latest goods list
     */
    Observable<List<PublishGoodsInfo>> getLatestGoodsList();

    /**
     * favor a goods
     */
    Observable<RepoResponseCode> favorGoods(PublishGoodsInfo goods);

    /**
     * get favored goods detail
     */
    Observable<PublishGoodsInfo> getFavorGoodsDetail(long id);

    /**
     * get favored goods list
     */
    Observable<List<PublishGoodsInfo>> getFavorGoodsList();


    /* for publisher */
    /**
     * get the detail of a publisher
     */
    Observable<PublisherInfo> getPublisherDetail(long id);

    /**
     * get a favor publisher detail
     */
    Observable<PublisherInfo> getFavorPublisherDetail(long id);

    /**
     * get the favorite publisher list
     */
    Observable<List<PublisherInfo>> getFavorPublisherList();

    /**
     * follow a publisher
     */
    Observable<RepoResponseCode> followPublisher(PublisherInfo publisher);
}
