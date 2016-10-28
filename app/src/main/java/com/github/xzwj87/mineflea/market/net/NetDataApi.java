package com.github.xzwj87.mineflea.market.net;

import com.github.xzwj87.mineflea.market.data.ResponseCode;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.PublisherInfo;

import java.util.List;

import rx.Observable;

/**
 * Created by jason on 9/25/16.
 */

public interface NetDataApi {

    /**
     * publish goods info to remote server
     */
    Observable<ResponseCode> publishGoods(PublishGoodsInfo goods);


    /**
     * query the info of a publisher
     *
     * @param: id - the id of the publisher
     */
    Observable<PublisherInfo> queryPublisherDetail(long id);


    /**
     * query latest goods list
     */
    Observable<List<PublishGoodsInfo>> queryLatestGoodsList();

    /**
     * follow some publisher
     *
     * @param : the publisher data to remote server
     */
    Observable<ResponseCode> followPublisher(PublisherInfo publisher);
}
