package com.github.xzwj87.mineflea.market.ui.protocol;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.ui.bean.NearbyGoogsInfo;
import com.github.xzwj87.mineflea.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhai on 2016/10/16.
 */

public class NearbyProtocol {

    public NearbyProtocol() {
    }

    public List<NearbyGoogsInfo> loadNearbyData() {
        List<NearbyGoogsInfo> list = new ArrayList<>();
        NearbyGoogsInfo info =new NearbyGoogsInfo(Constants.BEIJING, R.mipmap.ic_launcher, "英伦贵族小旅馆",
                "距离209米", 1456);
        list.add(info);
        info = new NearbyGoogsInfo(Constants.CHENGDU, R.mipmap.ic_launcher, "沙井国际洗浴会所",
                "距离897米", 456);
        list.add(info);
        info = new NearbyGoogsInfo(Constants.ZHONGGUANCUN, R.mipmap.ic_launcher, "老米家泡馍小炒",
                "距离679米", 1456);
        list.add(info);
        info = new NearbyGoogsInfo(Constants.ZHENGZHOU, R.mipmap.ic_launcher, "老米家泡馍小炒",
                "距离679米", 1456);
        list.add(info);
        info = new NearbyGoogsInfo(Constants.SHANGHAI, R.mipmap.ic_launcher, "老米家泡馍小炒",
                "距离679米", 1456);
        list.add(info);
        return list;
    }

}
