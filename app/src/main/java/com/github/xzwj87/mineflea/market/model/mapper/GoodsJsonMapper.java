package com.github.xzwj87.mineflea.market.model.mapper;

import com.github.xzwj87.mineflea.market.model.GoodsModel;
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

    public static String map(GoodsModel goods){
        return sGson.toJson(goods);
    }

    public static String mapList(List<GoodsModel> goodsList){
        return sGson.toJson(goodsList);
    }

    public static GoodsModel transform(String json){

        return sGson.fromJson(json,GoodsModel.class);
    }

    public static List<GoodsModel> transformToList(String json){
        Type type = new TypeToken<List<GoodsModel>>(){}.getType();

        return sGson.fromJson(json,type);
    }
}
