package com.github.xzwj87.mineflea.market.model.mapper;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by jason on 9/25/16.
 */

public class GoodsJsonMapper {

    private static final Gson sGson = new Gson();
    // map model data to JSON

    public static String map(PublishGoodsInfo goods){
        return sGson.toJson(goods);
    }

    public static String mapList(List<PublishGoodsInfo> goodsList){
        return sGson.toJson(goodsList);
    }

    public static PublishGoodsInfo transform(String json){

        return sGson.fromJson(json,PublishGoodsInfo.class);
    }

    public static List<PublishGoodsInfo> transformToList(String json){
        Type type = new TypeToken<List<PublishGoodsInfo>>(){}.getType();

        return sGson.fromJson(json,type);
    }
}
