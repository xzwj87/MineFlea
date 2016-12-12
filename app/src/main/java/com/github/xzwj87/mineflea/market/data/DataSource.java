package com.github.xzwj87.mineflea.market.data;


import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;

/**
 * Created by JasonWang on 2016/9/20.
 */
public interface DataSource {

    /**
     * publish goods
     * @source: local and remote
     */
    void publishGoods(PublishGoodsInfo goods);

    /*
     * register account
     */
    void register(UserInfo userInfo,String authCode);

    /*
     * login
     */
    void login(UserInfo info);

    /*
     * favorite a goods
     */
    void favor(PublishGoodsInfo goodsInfo);

    /*
     * query user favorite goods list
     */
    void queryFavoriteGoodsList(String userId);
}
