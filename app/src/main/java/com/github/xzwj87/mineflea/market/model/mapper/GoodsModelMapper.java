package com.github.xzwj87.mineflea.market.model.mapper;

import android.content.ContentValues;
import android.database.Cursor;

import com.github.xzwj87.mineflea.market.data.local.MineFleaContract.PublishGoodsEntry;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;

/**
 * Created by JasonWang on 2016/9/24.
 */
public class GoodsModelMapper {

    public static ContentValues map(PublishGoodsInfo data){
        ContentValues cv = new ContentValues();

        cv.put(PublishGoodsEntry.COL_PUBLISHER_ID,data.getPublisherId());
        cv.put(PublishGoodsEntry.COL_GOODS_ID,data.getId());
        cv.put(PublishGoodsEntry.COL_GOODS_NAME,data.getName());
        cv.put(PublishGoodsEntry.COL_LOW_PRICE,data.getLowerPrice());
        cv.put(PublishGoodsEntry.COL_HIGH_PRICE,data.getHighPrice());
        cv.put(PublishGoodsEntry.COL_LOCATION,data.getLocation());
        cv.put(PublishGoodsEntry.COL_RELEASE_DATE,data.getReleasedDate());
        cv.put(PublishGoodsEntry.COL_NOTE,data.getNote());

        return cv;
    }

    public static PublishGoodsInfo transform(Cursor c){
        PublishGoodsInfo data = new PublishGoodsInfo();

        return data;
    }
}
