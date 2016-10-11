package com.github.xzwj87.mineflea.market.model.mapper;

import com.github.xzwj87.mineflea.market.model.PublisherInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by jason on 9/26/16.
 */

public class PublisherJsonMapper {

    private static Gson sGson = new Gson();

    public static String map(PublisherInfo data){
        return sGson.toJson(data);
    }

    public static String mapList(List<PublisherInfo> publisherList){
        return sGson.toJson(publisherList);
    }

    public static PublisherInfo transform(String json){
        return sGson.fromJson(json,PublisherInfo.class);
    }

    public static List<PublisherInfo> transformToList(String json){
        Type type = new TypeToken<List<PublisherInfo>>(){}.getType();

        return sGson.fromJson(json,type);
    }
}
