package com.github.xzwj87.mineflea.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.xzwj87.mineflea.data.provider.FleaGoodsContract.*;

/**
 * Created by JasonWang on 2016/9/20.
 */
public class FleaGoodsDbHelper extends SQLiteOpenHelper {

    public static final String TAG = FleaGoodsDbHelper.class.getSimpleName();

    public static final String DB_NAME = "FleaGoods";
    public static final int DB_VERSION = 1;

    public static final String COMMA_SEP = ",";
    public static final String TEXT = " TEXT";
    public static final String TEXT_NOT_NULL = " TEXT NOT NULL";
    public static final String REAL = " REAL";
    public static final String INTEGER_NOT_NULL = " INTEGER NOT NULL";

    public static final String SQL_CREATE_TABLE_FAVOR = "CREATE TABLE " +
            FavorGoodsEntry.TABLE_FAVOR_GOODS + " (" +
            FavorGoodsEntry._ID + " PRIMARY KEY" + COMMA_SEP +
            FavorGoodsEntry.COL_NAME + TEXT_NOT_NULL + COMMA_SEP +
            FavorGoodsEntry.COL_PUBLISHER + TEXT_NOT_NULL + COMMA_SEP +
            FavorGoodsEntry.COL_TELEPHONE_NUMBER + TEXT + COMMA_SEP +
            FavorGoodsEntry.COL_HIGH_PRICE + REAL + COMMA_SEP +
            FavorGoodsEntry.COL_LOW_PRICE + REAL + COMMA_SEP +
            FavorGoodsEntry.COL_PLACE + TEXT + COMMA_SEP +
            FavorGoodsEntry.COL_RELEASE_DATE + INTEGER_NOT_NULL + COMMA_SEP +
            FavorGoodsEntry.COL_STAR_DATE + INTEGER_NOT_NULL + COMMA_SEP +
            FavorGoodsEntry.COL_LIKING_STARS + INTEGER_NOT_NULL + COMMA_SEP +
            FavorGoodsEntry.COL_NOTE + TEXT +
            " )";

    public static final String SQL_DELETE_TABLE_FAVOR = "DROP TABLE IF EXITS " +
            FavorGoodsEntry.TABLE_FAVOR_GOODS;
    public static final String SQL_DELETE_TABLE_RELEASE = "DROP TABLE IF EXITS " +
            ReleaseGoodsEntry.TABLE_RELEASED_GOODS;


    public static final String SQL_CREATE_TABLE_RELEASE = "CREATE TABLE " +
            ReleaseGoodsEntry.TABLE_RELEASED_GOODS + " (" +
            ReleaseGoodsEntry._ID + " PRIMARY KEY" + COMMA_SEP +
            ReleaseGoodsEntry.COL_NAME + TEXT_NOT_NULL + COMMA_SEP +
            ReleaseGoodsEntry.COL_PUBLISHER + TEXT_NOT_NULL + COMMA_SEP +
            ReleaseGoodsEntry.COL_TELEPHONE_NUMBER + TEXT + COMMA_SEP +
            ReleaseGoodsEntry.COL_HIGH_PRICE + REAL + COMMA_SEP +
            ReleaseGoodsEntry.COL_LOW_PRICE + REAL + COMMA_SEP +
            ReleaseGoodsEntry.COL_PLACE + TEXT + COMMA_SEP +
            ReleaseGoodsEntry.COL_RELEASE_DATE + INTEGER_NOT_NULL + COMMA_SEP +
            ReleaseGoodsEntry.COL_NOTE + TEXT +
            " )";

    private static FleaGoodsDbHelper sInstance;

    public static FleaGoodsDbHelper getInstance(Context context){
        if(sInstance == null){
            sInstance = new FleaGoodsDbHelper(context);
        }

        return sInstance;
    }

    public FleaGoodsDbHelper(Context context){
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
