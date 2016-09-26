package com.github.xzwj87.mineflea.model.mapper;

import android.content.ContentValues;
import android.database.Cursor;

import com.github.xzwj87.mineflea.model.GoodsModel;

/**
 * Created by JasonWang on 2016/9/24.
 */
public class GoodsModelMapper {

    public static ContentValues map(GoodsModel data){
        ContentValues cv = new ContentValues();

        return cv;
    }

    public static GoodsModel transform(Cursor c){
        GoodsModel data = new GoodsModel();

        return data;
    }
}
