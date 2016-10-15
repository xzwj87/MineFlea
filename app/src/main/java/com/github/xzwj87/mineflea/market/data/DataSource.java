package com.github.xzwj87.mineflea.market.data;

import android.os.Message;

import com.github.xzwj87.mineflea.market.model.PublisherInfo;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;

import java.util.List;

import rx.Observable;

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
    void register(UserInfo userInfo);
}
