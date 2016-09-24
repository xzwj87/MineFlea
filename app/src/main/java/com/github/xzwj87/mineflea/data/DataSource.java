package com.github.xzwj87.mineflea.data;

import com.github.xzwj87.mineflea.model.PublisherModel;
import com.github.xzwj87.mineflea.model.GoodsModel;

import java.util.List;

import rx.Observable;

/**
 * Created by JasonWang on 2016/9/20.
 */
public interface DataSource {

    /*
     * release goods
     */
    void publishGoods(GoodsModel goods);

    /*
     * query the detail of a goods
     */
    Observable<GoodsModel> queryPublishedGoodsDetail(long id);

    Observable<List<GoodsModel>> queryPublishedGoodsList();

    /*
     * add goods to a favorite one
     */
    void favorGoods(GoodsModel goods);

    /*
     * query the detail info of a favored goods
     */
    Observable<GoodsModel> queryFavorGoodsDetail(long id);

    Observable<List<GoodsModel>> queryFavorGoodsList();


    /*
     * query the info of a publisher
     */
    Observable<PublisherModel> queryPublisherDetail(long id);

    /*
     * query latest goods list
     */
    Observable<List<GoodsModel>> queryGoodsList();

    /*
     * follow some publisher
     */
    void followPublisher(PublisherModel publisher);
}
