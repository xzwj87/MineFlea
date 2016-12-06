package com.github.xzwj87.mineflea.utils;

import com.amap.api.maps.model.LatLng;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import static com.github.xzwj87.mineflea.market.model.PublishGoodsInfo.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jason on 10/24/16.
 */

public class PublishGoodsUtils {
    public static final String AV_OBJ_GOODS = "publishGoods";


    public static AVObject toAvObject(PublishGoodsInfo info){
        if(info == null) return null;

        AVObject avObject = new AVObject(AV_OBJ_GOODS);
        AVGeoPoint loc = new AVGeoPoint(info.getLocation().latitude,info.getLocation().longitude);
        avObject.put("whereCreated", loc);
        avObject.put(GOODS_NAME,info.getName());
        avObject.put(GOODS_PUBLISHER,info.getUserId());
        avObject.put(GOODS_PRICE,info.getPrice());
        avObject.put(GOODS_NOTE,info.getNote());
        avObject.put(GOODS_RELEASE_DATE,info.getReleasedDate());
        avObject.put(GOODS_UPDATED_TIME,info.getUpdateTime());
        //avObject.put(GOODS_LOC,info.getLocation());
        avObject.put(GOODS_IMAGES,info.getImageUri());
        avObject.put(GOODS_FAVOR_USER,info.getFavorUserList());

        return avObject;
    }

    @SuppressWarnings("unchecked")
    public static PublishGoodsInfo fromAvObject(AVObject object){
        if(object == null) return null;

        PublishGoodsInfo info = new PublishGoodsInfo();
        info.setId(object.getObjectId());
        info.setName((String)object.get(GOODS_NAME));
        info.setUserId((String)object.get(GOODS_PUBLISHER));
        info.setPrice(object.getDouble(GOODS_PRICE));
        info.setNote((String)object.get(GOODS_NOTE));
        info.setReleasedDate(object.getCreatedAt());
        info.setUpdateTime(object.getUpdatedAt());

        AVGeoPoint loc = (AVGeoPoint)object.get("whereCreated");
        LatLng latLng = new LatLng(loc.getLatitude(),loc.getLongitude());
        info.setLocation(latLng);
        info.setImageUri(object.getList(GOODS_IMAGES));
        info.setGoodsFavorUser(object.getList(GOODS_FAVOR_USER));

        return info;
    }

    public static List<String> getImgUrls(AVObject avObject){
        String imgList = (String)avObject.get(PublishGoodsInfo.GOODS_IMAGES);
        String urls[] = imgList.split(PublishGoodsInfo.GOODS_IMG_STRING_SEP);

        return Arrays.asList(urls);
    }
}
