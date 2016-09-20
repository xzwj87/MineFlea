package com.github.xzwj87.mineflea.data.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by JasonWang on 2016/9/20.
 */
public class FleaGoodsContract {
    public static final String PROVIDER_AUTHORITY = "com.github.xzwj87.mineflea";

    public static final Uri PROVIDER_BASE_URI = Uri.parse("content://" + PROVIDER_AUTHORITY);

    public static final String PATH_FAVORITE_GOODS = "favorite_goods";
    public static final String PATH_RELEASE_GOODS = "release_goods";

    public static final class FavorGoodsEntry implements BaseColumns{
        public static final String TABLE_FAVOR_GOODS = "favorite";

        public static final Uri CONTENT_URI_FAVOR = PROVIDER_BASE_URI.buildUpon().
                appendPath(TABLE_FAVOR_GOODS).build();

        public static final String PROVIDER_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + PROVIDER_AUTHORITY + "/" + PATH_FAVORITE_GOODS;
        public static final String PROVIDER_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + PROVIDER_AUTHORITY + "/" + PATH_FAVORITE_GOODS;


        public static final String COL_NAME = "name";
        /* who released */
        public static final String COL_PUBLISHER = "publisher";

        public static final String COL_TELEPHONE_NUMBER = "tel_number";
        /* estimated price */
        public static final String COL_HIGH_PRICE = "high_price";
        public static final String COL_LOW_PRICE = "low_price";
        /* where to be released */
        public static final String COL_PLACE = "place";
        /* when */
        public static final String COL_RELEASE_DATE = "release_date";
        public static final String COL_STAR_DATE = "star_date";
        /* liking stars */
        public static final String COL_LIKING_STARS = "liking_stars";

        public static final String COL_NOTE = "note";

        // TODO: we may want to save IMAGE uri here
    }


    public static final class ReleaseGoodsEntry implements BaseColumns{
        public static final String TABLE_RELEASED_GOODS = "release";

        public static final Uri CONTENT_URI_RELEASE = PROVIDER_BASE_URI.buildUpon().
                appendPath(TABLE_RELEASED_GOODS).build();

        public static final String PROVIDER_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + PROVIDER_AUTHORITY + "/" + PATH_RELEASE_GOODS;
        public static final String PROVIDER_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + PROVIDER_AUTHORITY + "/" + PATH_RELEASE_GOODS;


        public static final String COL_NAME = "name";
        /* who released */
        public static final String COL_PUBLISHER = "publisher";

        public static final String COL_TELEPHONE_NUMBER = "tel_number";
        /* estimated price */
        public static final String COL_HIGH_PRICE = "high_price";
        public static final String COL_LOW_PRICE = "low_price";
        /* where to be released */
        public static final String COL_PLACE = "place";
        /* when */
        public static final String COL_RELEASE_DATE = "release_date";

        public static final String COL_NOTE = "note";

    }

}
