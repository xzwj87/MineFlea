package com.github.xzwj87.mineflea.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQuery;
import android.net.Uri;
import android.util.Log;

import com.github.xzwj87.mineflea.data.DataSource;
import com.github.xzwj87.mineflea.data.RepoResponseCode;
import com.github.xzwj87.mineflea.model.GoodsModel;
import com.github.xzwj87.mineflea.model.PublisherModel;

import com.github.xzwj87.mineflea.data.local.MineFleaContract.*;
import com.github.xzwj87.mineflea.model.mapper.GoodsModelMapper;
import com.github.xzwj87.mineflea.model.mapper.PublisherModelMapper;
import com.github.xzwj87.mineflea.ui.fragment.TabHolderFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * Created by JasonWang on 2016/9/20.
 */
public class MineFleaLocalSource implements DataSource{
    public static final String TAG = MineFleaLocalSource.class.getSimpleName();

    private MineFleaDbHelper mDbHelper;
    private static MineFleaLocalSource sInstance;

    private static UriMatcher sUriMatcher;

    private static final int FAVOR_PUBLISHER_WITHOUT_ID = 100;
    private static final int FAVOR_PUBLISHER_WITH_ID = 101;

    private static final int FAVOR_GOODS_WITHOUT_ID = 102;
    private static final int FAVOR_GOODS_WITH_ID = 103;

    private static final int FOLLOWERS_WITHOUT_ID = 104;
    private static final int FOLLOWERS_WITH_ID = 105;

    private static final int PUBLISHED_GOODS_WITHOUT_ID = 106;
    private static final int PUBLISHED_GOODS_WITH_ID = 107;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String AUTHORITY = MineFleaContract.PROVIDER_AUTHORITY;

        sUriMatcher.addURI(AUTHORITY,MineFleaContract.PATH_FAVOR_PUBLISHER + "/#",FAVOR_PUBLISHER_WITHOUT_ID);
        sUriMatcher.addURI(AUTHORITY,MineFleaContract.PATH_FAVOR_PUBLISHER,FAVOR_PUBLISHER_WITH_ID);

        sUriMatcher.addURI(AUTHORITY,MineFleaContract.PATH_FAVOR_GOODS + "/#",FAVOR_GOODS_WITHOUT_ID);
        sUriMatcher.addURI(AUTHORITY,MineFleaContract.PATH_FAVOR_GOODS,FAVOR_GOODS_WITH_ID);

        sUriMatcher.addURI(AUTHORITY,MineFleaContract.PATH_FOLLOWERS + "/#",FOLLOWERS_WITHOUT_ID);
        sUriMatcher.addURI(AUTHORITY,MineFleaContract.PATH_FOLLOWERS,FOLLOWERS_WITH_ID);

        sUriMatcher.addURI(AUTHORITY,MineFleaContract.PATH_PUBLISHED_GOODS + "/#",PUBLISHED_GOODS_WITHOUT_ID);
        sUriMatcher.addURI(AUTHORITY,MineFleaContract.PATH_PUBLISHED_GOODS,PUBLISHED_GOODS_WITH_ID);
    }


    private MineFleaLocalSource(Context context){
        mDbHelper = MineFleaDbHelper.getInstance(context);
    }

    public static MineFleaLocalSource getInstance(Context context){
        if(sInstance == null){
            sInstance = new MineFleaLocalSource(context);
        }

        return sInstance;
    }


    @Override
    public Observable<RepoResponseCode> publishGoods(GoodsModel goods) {
        Log.v(TAG,"publishGoods(): goods = " + goods);

        ContentValues cv = GoodsModelMapper.map(goods);

        long id = insert(PublishedGoodsEntry.TABLE_PUBLISHER_GOODS,cv);

        int code = RepoResponseCode.RESP_SUCCESS;
        if(id == -1){
            code = RepoResponseCode.RESP_DATABASE_SQL_ERROR;
        }

        final RepoResponseCode response = new RepoResponseCode(code);

        return Observable.create(new Observable.OnSubscribe<RepoResponseCode>() {
            @Override
            public void call(Subscriber<? super RepoResponseCode> subscriber) {
                subscriber.onNext(response);
            }
        });
    }

    @Override
    public Observable<GoodsModel> queryPublishedGoodsDetail(long id) {
        Log.v(TAG,"queryPublishedGoodsDetail(): id = " + id);

        final String selection = PublishedGoodsEntry._ID + " =? " + id;


        return Observable.create(new Observable.OnSubscribe<GoodsModel>() {
            @Override
            public void call(Subscriber<? super GoodsModel> subscriber) {
                Cursor c = query(PublishedGoodsEntry.TABLE_PUBLISHER_GOODS,
                        null,selection,null,null);
                if(c != null) {
                    c.moveToFirst();
                    GoodsModel data = GoodsModelMapper.transform(c);

                    c.close();
                    subscriber.onNext(data);
                }else{
                    subscriber.onError(new SQLiteException("query failure"));
                }
            }
        });
    }

    @Override
    public Observable<List<GoodsModel>> queryPublishedGoodsList() {
        Log.v(TAG,"queryPublishedGoodsList()");

        return Observable.create(new Observable.OnSubscribe<List<GoodsModel>>() {
            @Override
            public void call(Subscriber<? super List<GoodsModel>> subscriber) {
                String sortBy = PublishedGoodsEntry.COL_RELEASE_DATE + " ASC";
                Cursor c = query(PublishedGoodsEntry.TABLE_PUBLISHER_GOODS,
                        null,null,null,sortBy);

                if(c != null) {
                    List<GoodsModel> goodsList = new ArrayList<GoodsModel>();

                    while (c.moveToNext()) {
                        goodsList.add(GoodsModelMapper.transform(c));
                    }

                    c.close();
                    subscriber.onNext(goodsList);
                }else{
                    subscriber.onError(new SQLiteException("fail to query published goods list"));
                }
            }
        });
    }

    @Override
    public Observable<RepoResponseCode> favorGoods(GoodsModel goods) {
        Log.v(TAG,"favorGoods(): goods = " + goods);
        ContentValues cv = GoodsModelMapper.map(goods);

        long id = insert(FavorGoodsEntry.TABLE_FAVOR_GOODS,cv);

        int code = RepoResponseCode.RESP_SUCCESS;
        if(id == -1){
            code = RepoResponseCode.RESP_DATABASE_SQL_ERROR;
        }

        final RepoResponseCode response = new RepoResponseCode(code);

        return Observable.create(new Observable.OnSubscribe<RepoResponseCode>() {
            @Override
            public void call(Subscriber<? super RepoResponseCode> subscriber) {
                subscriber.onNext(response);
            }
        });
    }

    @Override
    public Observable<GoodsModel> queryFavorGoodsDetail(long id) {
        Log.v(TAG,"queryFavorGoodsDetail(): id = " + id);

        final String selection = FavorGoodsEntry._ID + " =? " + id;

        return Observable.create(new Observable.OnSubscribe<GoodsModel>() {
            @Override
            public void call(Subscriber<? super GoodsModel> subscriber) {
                Cursor c = query(FavorGoodsEntry.TABLE_FAVOR_GOODS,null,
                        selection,null,null);

                if(c != null) {
                    c.moveToFirst();
                    GoodsModel data = GoodsModelMapper.transform(c);

                    c.close();
                    subscriber.onNext(data);
                }else{
                    subscriber.onError(new SQLiteException("fail to query favor goods detail"));
                }
            }
        });
    }

    @Override
    public Observable<List<GoodsModel>> queryFavorGoodsList() {
        Log.v(TAG,"queryFavorGoodsList()");


        return Observable.create(new Observable.OnSubscribe<List<GoodsModel>>() {
            @Override
            public void call(Subscriber<? super List<GoodsModel>> subscriber) {
                String sortBy = FavorGoodsEntry.COL_RELEASE_DATE + " ASC";
                Cursor c = query(FavorGoodsEntry.TABLE_FAVOR_GOODS,null,null,null,sortBy);

                if(c != null){
                    List<GoodsModel> goodsList = new ArrayList<GoodsModel>();

                    while(c.moveToNext()){
                        goodsList.add(GoodsModelMapper.transform(c));
                    }

                    c.close();
                    subscriber.onNext(goodsList);
                }else{
                    subscriber.onError(new SQLiteException("fail to query favor goods list"));
                }

            }
        });
    }

    @Override
    public Observable<PublisherModel> queryPublisherDetail(long id) {
        Log.v(TAG,"queryPublisherDetail(): id = " + id);

        final String selection = FavorPublisherEntry._ID + " =? " + id;

        return Observable.create(new Observable.OnSubscribe<PublisherModel>() {
            @Override
            public void call(Subscriber<? super PublisherModel> subscriber) {
                Cursor c = query(FavorPublisherEntry.TABLE_PUBLISHER,null,
                        selection, null,null);
                if(c != null){
                    c.moveToFirst();
                    subscriber.onNext(PublisherModelMapper.transform(c));

                    c.close();
                }else{
                    subscriber.onError(new SQLiteException("fail to get publisher detail"));
                }
            }
        });
    }

    @Override
    public Observable<List<PublisherModel>> queryPublisherList() {
        Log.v(TAG,"queryPublisherList()");

        return Observable.create(new Observable.OnSubscribe<List<PublisherModel>>() {
            @Override
            public void call(Subscriber<? super List<PublisherModel>> subscriber) {
                String sortBy = FavorPublisherEntry.COL_DISTANCE + " ASC";
                Cursor c = query(FavorPublisherEntry.TABLE_PUBLISHER,null,
                        null,null,sortBy);
                if(c != null){
                    List<PublisherModel> publisherList = new ArrayList<PublisherModel>();

                    while(c.moveToNext()){
                        publisherList.add(PublisherModelMapper.transform(c));
                    }

                    c.close();
                    subscriber.onNext(publisherList);
                }
            }
        });
    }


    @Override
    public Observable<List<GoodsModel>> queryLatestGoodsList() {
        throw new UnsupportedOperationException(
                "it should be queried in remote data source");
    }

    @Override
    public Observable<RepoResponseCode> followPublisher(PublisherModel publisher) {
        throw new UnsupportedOperationException(
                "it should be called in remote data source");
    }

    private long insert(String table, ContentValues cv){

        long id = mDbHelper.getWritableDatabase().insert(table,null,cv);

        return id;
    }

    private Cursor query(String table,String[] projection,String selection,String[] args,
                         String sortOrder){

        Cursor cursor = mDbHelper.getReadableDatabase().query(table,projection,selection,args,
                null,null,sortOrder);

        return cursor;

    }


    private int delete(String table,String where, String[] whereArgs){
        Log.v(TAG,"delete(): table = " + table);

        int ret = -1;

        ret = mDbHelper.getWritableDatabase().delete(table,where,whereArgs);

        return ret;
    }

    private int update(String table,ContentValues cv,String where, String[] whereArgs){
        Log.v(TAG,"update(): table = " + table);

        int ret = -1;

        ret = mDbHelper.getWritableDatabase().update(table,cv,where,whereArgs);

        return ret;
    }
}
