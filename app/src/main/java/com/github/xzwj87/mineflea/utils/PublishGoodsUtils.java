package com.github.xzwj87.mineflea.utils;

import com.avos.avoscloud.AVObject;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jason on 10/24/16.
 */

public class PublishGoodsUtils {

    public static PublishGoodsInfo fromAvObject(AVObject object){
        PublishGoodsInfo goodsInfo = new PublishGoodsInfo();

        goodsInfo.setId(object.getObjectId());
        goodsInfo.setName((String)object.get(PublishGoodsInfo.GOODS_NAME));
        goodsInfo.setPrice(object.getDouble(PublishGoodsInfo.GOODS_PRICE));
        goodsInfo.setNote((String)object.get(PublishGoodsInfo.GOODS_NOTE));
        goodsInfo.setLocation((String)object.get(PublishGoodsInfo.GOODS_LOC));
        goodsInfo.setPublisherId((String)object.get(PublishGoodsInfo.GOODS_PUBLISHER));
        goodsInfo.setReleasedDate((long)object.get(PublishGoodsInfo.GOODS_RELEASE_DATE));
        goodsInfo.setStars((int)object.get(PublishGoodsInfo.GOODS_LIKES));

        return goodsInfo;
    }

    public static List<String> getImgUrls(AVObject avObject){
        String imgList = (String)avObject.get(PublishGoodsInfo.GOODS_IMAGES);
        String urls[] = imgList.split(PublishGoodsInfo.GOODS_IMG_STRING_SEP);

        return Arrays.asList(urls);
    }
}
