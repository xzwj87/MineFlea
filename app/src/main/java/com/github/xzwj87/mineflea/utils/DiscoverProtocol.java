package com.github.xzwj87.mineflea.utils;

import com.github.xzwj87.mineflea.market.model.DiscoverInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhai on 2016/10/16.
 */

public class DiscoverProtocol {

    public DiscoverProtocol() {
    }

    public List<DiscoverInfo> loadDiscoverData() {
        List<DiscoverInfo> list = new ArrayList<>();
        DiscoverInfo info = new DiscoverInfo("第一个物品", "2016年10月12日", "image url", "1000米");
        list.add(info);
        info = new DiscoverInfo("第一个物品", "2016年10月12日", "image url", "1000米");
        list.add(info);
        info = new DiscoverInfo("第2个物品", "2016年10月12日", "image url", "1000米");
        list.add(info);
        info = new DiscoverInfo("第3个物品", "2016年10月12日", "image url", "1000米");
        list.add(info);
        info = new DiscoverInfo("第4个物品", "2016年10月12日", "image url", "1000米");
        list.add(info);
        info = new DiscoverInfo("第5物品", "2016年10月12日", "image url", "1000米");
        list.add(info);
        info = new DiscoverInfo("第6个物品", "2016年10月12日", "image url", "1000米");
        list.add(info);
        info = new DiscoverInfo("第7个物品", "2016年10月12日", "image url", "1000米");
        list.add(info);
        info = new DiscoverInfo("第8个物品", "2016年10月12日", "image url", "1000米");
        list.add(info);
        info = new DiscoverInfo("第9个物品", "2016年10月12日", "image url", "1000米");
        list.add(info);
        return list;
    }

}
