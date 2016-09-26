package com.github.xzwj87.mineflea.data.local;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by JasonWang on 2016/9/20.
 */
public class MineFleaContract {
    public static final String PROVIDER_AUTHORITY = "com.github.xzwj87.mineflea";

    public static final Uri PROVIDER_BASE_URI = Uri.parse("content://" + PROVIDER_AUTHORITY);

    public static final String PATH_FAVOR_PUBLISHER = "favor_publisher";
    public static final String PATH_FAVOR_GOODS = "favor_goods";
    public static final String PATH_PUBLISHED_GOODS = "release_goods";
    public static final String PATH_FOLLOWERS = "followers";

    public static final class FavorPublisherEntry implements BaseColumns{
        public static final String TABLE_PUBLISHER = "favor_publisher";

        public static final Uri CONTENT_URI_PUBLISHER = PROVIDER_BASE_URI.buildUpon().
                appendPath(TABLE_PUBLISHER).build();

        public static final String PROVIDER_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + PROVIDER_AUTHORITY + "/" + PATH_FAVOR_PUBLISHER;
        public static final String PROVIDER_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + PROVIDER_AUTHORITY + "/" + PATH_FAVOR_PUBLISHER;


        public static final String COL_ID = "id";
        public static final String COL_NAME = "publisher";
        public static final String COL_TELEPHONE_NUMBER = "tel_number";
        public static final String COL_EMAIL = "email";
        public static final String COL_CREDITS = "credits";
        public static final String COL_FOLLOWERS = "followers";
        public static final String COL_GOODS_COUNT = "goods_count";
        /* where to be released */
        public static final String COL_LOCATION = "place";
        public static final String COL_DISTANCE = "distance";
    }

    public static final class FavorGoodsEntry implements BaseColumns{
        public static final String TABLE_FAVOR_GOODS = "favor_goods";

        public static final Uri CONTENT_URI_FAVOR = PROVIDER_BASE_URI.buildUpon().
                appendPath(TABLE_FAVOR_GOODS).build();

        public static final String PROVIDER_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + PROVIDER_AUTHORITY + "/" + PATH_FAVOR_GOODS;
        public static final String PROVIDER_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + PROVIDER_AUTHORITY + "/" + PATH_FAVOR_GOODS;

        public static final String COL_ID = "id";
        public static final String COL_NAME = "name";
        public static final String COL_PUBLISHER_ID = "publisher_id";
        /* estimated price */
        public static final String COL_HIGH_PRICE = "high_price";
        public static final String COL_LOW_PRICE = "low_price";
        /* when */
        public static final String COL_RELEASE_DATE = "release_date";
        public static final String COL_STAR_DATE = "star_date";
        /* liking stars */
        public static final String COL_LIKING_STARS = "liking_stars";

        public static final String COL_NOTE = "note";

        public static final String COL_IMAGE_URI = "image_uri";
    }


    public static final class FollowerEntry implements BaseColumns{
        public static final String TABLE_FOLLOWER = "follower";

        public static final Uri CONTENT_URI_FOLLOWERS = PROVIDER_BASE_URI.buildUpon().
                appendPath(TABLE_FOLLOWER).build();

        public static final String PROVIDER_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + PROVIDER_AUTHORITY + "/" + PATH_FOLLOWERS;
        public static final String PROVIDER_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + PROVIDER_AUTHORITY + "/" + PATH_FOLLOWERS;

        public static final String COL_ID = "id";

        public static final String COL_NAME = "name";

        public static final String COL_HEAD_ICON = "icon";

        public static final String COL_TEL_NUMBER = "tel_number";

        public static final String COL_EMAIL = "email";

        public static final String COL_CREDITS = "credits";
    }

    public static final class PublishedGoodsEntry implements BaseColumns{
        public static final String TABLE_PUBLISHER_GOODS = "published_goods";

        public static final Uri CONTENT_URI_PUBLISHED = PROVIDER_BASE_URI.buildUpon().
                appendPath(TABLE_PUBLISHER_GOODS).build();

        public static final String PROVIDER_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + PROVIDER_AUTHORITY + "/" + PATH_PUBLISHED_GOODS;
        public static final String PROVIDER_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + PROVIDER_AUTHORITY + "/" + PATH_PUBLISHED_GOODS;

        public static final String COL_PUBLISHER_ID = "id";
        public static final String COL_PUBLISHER_NAME = "publisher";
        public static final String COL_TELEPHONE_NUMBER = "tel_number";

        public static final String COL_GOODS_ID = "goods_id";
        public static final String COL_GOODS_NAME = "goods_name";
        /* estimated price */
        public static final String COL_HIGH_PRICE = "high_price";
        public static final String COL_LOW_PRICE = "low_price";
        /* where to be released */
        public static final String COL_PLACE = "place";
        /* when */
        public static final String COL_RELEASE_DATE = "release_date";

        public static final String COL_NOTE = "note";

    }

    public static Uri buildUriWithId(Uri uri,long id){
        return ContentUris.withAppendedId(uri,id);
    }

    public static long getIdFromUri(Uri uri){
        return Long.parseLong(uri.getPathSegments().get(1));
    }

}
