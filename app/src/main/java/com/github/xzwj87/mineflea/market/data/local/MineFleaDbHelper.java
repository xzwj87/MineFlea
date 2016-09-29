package com.github.xzwj87.mineflea.market.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.xzwj87.mineflea.market.data.local.MineFleaContract.*;

/**
 * Created by JasonWang on 2016/9/20.
 */
public class MineFleaDbHelper extends SQLiteOpenHelper {

    public static final String TAG = MineFleaDbHelper.class.getSimpleName();

    public static final String DB_NAME = "FleaGoods";
    public static final int DB_VERSION = 1;

    public static final String COMMA_SEP = ",";
    public static final String TEXT = " TEXT";
    public static final String TEXT_NOT_NULL = " TEXT NOT NULL";
    public static final String REAL = " REAL";
    public static final String INTEGER = " INTEGER";
    public static final String INTEGER_NOT_NULL = " INTEGER NOT NULL";

    public static final String SQL_CREATE_TABLE_FAVOR = "CREATE TABLE " +
            FavorPublisherEntry.TABLE_PUBLISHER + " (" +
            FavorPublisherEntry._ID + " PRIMARY KEY" + COMMA_SEP +
            FavorPublisherEntry.COL_ID + INTEGER_NOT_NULL + COMMA_SEP +
            FavorPublisherEntry.COL_NAME + TEXT_NOT_NULL + COMMA_SEP +
            FavorPublisherEntry.COL_TELEPHONE_NUMBER + TEXT + COMMA_SEP +
            FavorPublisherEntry.COL_EMAIL + TEXT + COMMA_SEP +
            FavorPublisherEntry.COL_CREDITS + INTEGER + COMMA_SEP +
            FavorPublisherEntry.COL_FOLLOWERS + INTEGER + COMMA_SEP +
            FavorPublisherEntry.COL_GOODS_COUNT + INTEGER + COMMA_SEP +
            FavorPublisherEntry.COL_LOCATION + TEXT + COMMA_SEP +
            FavorPublisherEntry.COL_DISTANCE + TEXT + COMMA_SEP +
            " )";

    public static final String SQL_DELETE_TABLE_FAVOR = "DROP TABLE IF EXITS " +
            FavorPublisherEntry.TABLE_PUBLISHER;
    public static final String SQL_DELETE_TABLE_RELEASE = "DROP TABLE IF EXITS " +
            PublishedGoodsEntry.TABLE_PUBLISHER_GOODS;


    public static final String SQL_CREATE_TABLE_RELEASE = "CREATE TABLE " +
            PublishedGoodsEntry.TABLE_PUBLISHER_GOODS + " (" +
            PublishedGoodsEntry._ID + " PRIMARY KEY" + COMMA_SEP +
            PublishedGoodsEntry.COL_PUBLISHER_ID + INTEGER_NOT_NULL + COMMA_SEP +
            PublishedGoodsEntry.COL_PUBLISHER_NAME + TEXT_NOT_NULL + COMMA_SEP +
            PublishedGoodsEntry.COL_TELEPHONE_NUMBER + TEXT + COMMA_SEP +
            PublishedGoodsEntry.COL_GOODS_ID + INTEGER + COMMA_SEP +
            PublishedGoodsEntry.COL_GOODS_NAME + TEXT_NOT_NULL + COMMA_SEP +
            PublishedGoodsEntry.COL_HIGH_PRICE + REAL + COMMA_SEP +
            PublishedGoodsEntry.COL_LOW_PRICE + REAL + COMMA_SEP +
            PublishedGoodsEntry.COL_PLACE + TEXT + COMMA_SEP +
            PublishedGoodsEntry.COL_RELEASE_DATE + INTEGER_NOT_NULL + COMMA_SEP +
            PublishedGoodsEntry.COL_NOTE + TEXT +
            " )";

    private static MineFleaDbHelper sInstance;

    public static MineFleaDbHelper getInstance(Context context){
        if(sInstance == null){
            sInstance = new MineFleaDbHelper(context);
        }

        return sInstance;
    }

    public MineFleaDbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG,"onCreate()");

        db.execSQL(SQL_CREATE_TABLE_FAVOR);
        db.execSQL(SQL_CREATE_TABLE_RELEASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(TAG,"onUpgrade(): oldVersion(" + oldVersion +
                ")-> newVersion(" + newVersion +")");

        db.execSQL(SQL_DELETE_TABLE_FAVOR);
        db.execSQL(SQL_DELETE_TABLE_RELEASE);

        onCreate(db);
    }
}
