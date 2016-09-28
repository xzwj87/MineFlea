package com.github.xzwj87.mineflea.net;

import com.github.xzwj87.mineflea.data.RepoResponseCode;
import com.github.xzwj87.mineflea.exception.NetNoConnectionException;
import com.github.xzwj87.mineflea.model.GoodsModel;
import com.github.xzwj87.mineflea.model.PublisherModel;

import java.util.List;

import rx.Observable;

/**
 * Created by jason on 9/25/16.
 */

public interface NetDataApi {

    /**
     * publish goods info to remote server
     */
    Observable<RepoResponseCode> publishGoods(GoodsModel goods);


    /**
     * query the info of a publisher
     *
     * @param: id - the id of the publisher
     */
    Observable<PublisherModel> queryPublisherDetail(long id);


    /**
     * query latest goods list
     */
    Observable<List<GoodsModel>> queryLatestGoodsList();

    /**
     * follow some publisher
     *
     * @param : the publisher data to remote server
     */
    Observable<RepoResponseCode> followPublisher(PublisherModel publisher);
}
