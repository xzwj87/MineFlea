package com.github.xzwj87.mineflea.market.model.mapper;

import android.content.ContentValues;
import android.database.Cursor;

import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;

/**
 * Created by JasonWang on 2016/9/24.
 */
public class GoodsModelMapper {

    public static ContentValues map(PublishGoodsInfo data){
        ContentValues cv = new ContentValues();

        return cv;
    }

    public static PublishGoodsInfo transform(Cursor c){
        PublishGoodsInfo data = new PublishGoodsInfo();

        return data;
    }
}
