package com.github.xzwj87.mineflea.utils;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.model.NearbyGoodsInfo;
import com.github.xzwj87.mineflea.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhai on 2016/10/16.
 */

public class NearbyProtocol {

    public NearbyProtocol() {
    }

    public List<NearbyGoodsInfo> loadNearbyData() {
        List<NearbyGoodsInfo> list = new ArrayList<>();
        NearbyGoodsInfo info =new NearbyGoodsInfo(Constants.BEIJING, R.mipmap.test_info_iv, "英伦贵族小旅馆",
                "距离209米", 1456);
        list.add(info);
        info = new NearbyGoodsInfo(Constants.CHENGDU, R.mipmap.test_info_iv, "沙井国际洗浴会所",
                "距离897米", 456);
        list.add(info);
        info = new NearbyGoodsInfo(Constants.ZHONGGUANCUN, R.mipmap.test_info_iv, "老米家泡馍小炒",
                "距离679米", 1456);
        list.add(info);
        info = new NearbyGoodsInfo(Constants.ZHENGZHOU, R.mipmap.test_info_iv, "老米家泡馍小炒",
                "距离679米", 1456);
        list.add(info);
        info = new NearbyGoodsInfo(Constants.SHANGHAI, R.mipmap.test_info_iv, "老米家泡馍小炒",
                "距离679米", 1456);
        list.add(info);
        return list;
    }

}
