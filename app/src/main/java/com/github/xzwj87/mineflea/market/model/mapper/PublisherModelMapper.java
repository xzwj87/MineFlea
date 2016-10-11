package com.github.xzwj87.mineflea.market.model.mapper;

import android.content.ContentValues;
import android.database.Cursor;

import com.github.xzwj87.mineflea.market.model.PublisherInfo;

/**
 * Created by JasonWang on 2016/9/24.
 */
public class PublisherModelMapper {

    public static ContentValues map(PublisherInfo publisher){
        ContentValues cv = new ContentValues();

        return cv;
    }


    public static PublisherInfo transform(Cursor c){
        PublisherInfo data = new PublisherInfo();

        return data;
    }
}
