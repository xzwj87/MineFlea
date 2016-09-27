package com.github.xzwj87.mineflea.net;

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
    void publishGoods(GoodsModel goods) throws NetNoConnectionException;


    /**
     * query the info of a publisher
     *
     * @param: id - the id of the publisher
     */
    Observable<PublisherModel> queryPublisherDetail(long id);


    /**
     * query latest goods list
     */
    public Observable<List<GoodsModel>> queryLatestGoodsList();

    /**
     * follow some publisher
     *
     * @param : the publisher data to remote server
     */
    public void followPublisher(PublisherModel publisher) throws NetNoConnectionException;
}
