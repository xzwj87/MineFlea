package com.github.xzwj87.mineflea.market.model.mapper;

import android.content.ContentValues;
import android.database.Cursor;

import com.github.xzwj87.mineflea.market.model.PublisherModel;

/**
 * Created by JasonWang on 2016/9/24.
 */
public class PublisherModelMapper {

    public static ContentValues map(PublisherModel publisher){
        ContentValues cv = new ContentValues();

        return cv;
    }


    public static PublisherModel transform(Cursor c){
        PublisherModel data = new PublisherModel();

        return data;
    }
}
