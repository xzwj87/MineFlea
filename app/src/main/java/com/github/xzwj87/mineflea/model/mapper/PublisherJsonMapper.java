package com.github.xzwj87.mineflea.model.mapper;

import com.github.xzwj87.mineflea.model.PublisherModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by jason on 9/26/16.
 */

public class PublisherJsonMapper {

    private static Gson sGson = new Gson();

    public static String map(PublisherModel data){
        return sGson.toJson(data);
    }

    public static String mapList(List<PublisherModel> publisherList){
        return sGson.toJson(publisherList);
    }

    public static PublisherModel transform(String json){
        return sGson.fromJson(json,PublisherModel.class);
    }

    public static List<PublisherModel> transformToList(String json){
        Type type = new TypeToken<List<PublisherModel>>(){}.getType();

        return sGson.fromJson(json,type);
    }
}
