package com.github.xzwj87.mineflea.market.data.cache.serializer;

import android.text.TextUtils;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.google.gson.Gson;

/**
 * Created by jason on 11/5/16.
 */

public class JsonSerializer {

    private static final  Gson gson = new Gson();

    public static String toJson(UserInfo userInfo){

        return gson.toJson(userInfo);
    }

    public static String toJson(PublishGoodsInfo goodsInfo){
        return gson.toJson(goodsInfo);
    }

    public static UserInfo fromUserJson(String jsonString){
        if(TextUtils.isEmpty(jsonString)) return null;
        return (UserInfo)gson.fromJson(jsonString,UserInfo.class);
    }

    public static PublishGoodsInfo fromGoodsJson(String jsonString){
        if(TextUtils.isEmpty(jsonString)) return null;
        return (PublishGoodsInfo)gson.fromJson(jsonString,PublishGoodsInfo.class);
    }
}
