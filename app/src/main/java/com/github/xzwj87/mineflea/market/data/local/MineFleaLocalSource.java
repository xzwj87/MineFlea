package com.github.xzwj87.mineflea.market.data.local;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.data.DataSource;
import com.github.xzwj87.mineflea.market.data.RepoResponseCode;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.PublisherInfo;

import com.github.xzwj87.mineflea.market.data.local.MineFleaContract.*;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.model.mapper.GoodsModelMapper;
import com.github.xzwj87.mineflea.market.model.mapper.PublisherModelMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by JasonWang on 2016/9/20.
 */

@PerActivity
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

    @Inject
    public MineFleaLocalSource(){
        mDbHelper = MineFleaDbHelper.getInstance(AppGlobals.getAppContext());
    }

    public static MineFleaLocalSource getInstance(){
        if(sInstance == null){
            sInstance = new MineFleaLocalSource();
        }

        return sInstance;
    }

    @Override
    public void publishGoods(PublishGoodsInfo goods) {
        ContentValues cv = GoodsModelMapper.map(goods);

        long id = insert(PublishGoodsEntry.TABLE_PUBLISHER_GOODS,cv);

        Log.v(TAG,"publishGoods(): goods id = " + id);
    }

    @Override
    public void register(UserInfo userInfo) {
        Log.v(TAG,"register(): not supported");
    }

    @Override
    public Observable<PublishGoodsInfo> queryPublishedGoodsDetail(long id) {
        Log.v(TAG,"queryPublishedGoodsDetail(): id = " + id);

        final String selection = PublishGoodsEntry._ID + " =? " + id;


        return Observable.create(new Observable.OnSubscribe<PublishGoodsInfo>() {
            @Override
            public void call(Subscriber<? super PublishGoodsInfo> subscriber) {
                Cursor c = query(PublishGoodsEntry.TABLE_PUBLISHER_GOODS,
                        null,selection,null,null);
                if(c != null) {
                    c.moveToFirst();
                    PublishGoodsInfo data = GoodsModelMapper.transform(c);

                    c.close();
                    subscriber.onNext(data);
                }else{
                    subscriber.onError(new SQLiteException("query failure"));
                }
            }
        });
    }

    @Override
    public Observable<List<PublishGoodsInfo>> queryPublishedGoodsList() {
        Log.v(TAG,"queryPublishedGoodsList()");

        return Observable.create(new Observable.OnSubscribe<List<PublishGoodsInfo>>() {
            @Override
            public void call(Subscriber<? super List<PublishGoodsInfo>> subscriber) {
                String sortBy = PublishGoodsEntry.COL_RELEASE_DATE + " ASC";
                Cursor c = query(PublishGoodsEntry.TABLE_PUBLISHER_GOODS,
                        null,null,null,sortBy);

                if(c != null) {
                    List<PublishGoodsInfo> goodsList = new ArrayList<PublishGoodsInfo>();

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
    public Observable<RepoResponseCode> favorGoods(PublishGoodsInfo goods) {
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
    public Observable<PublishGoodsInfo> queryFavorGoodsDetail(long id) {
        Log.v(TAG,"queryFavorGoodsDetail(): id = " + id);

        final String selection = FavorGoodsEntry._ID + " =? " + id;

        return Observable.create(new Observable.OnSubscribe<PublishGoodsInfo>() {
            @Override
            public void call(Subscriber<? super PublishGoodsInfo> subscriber) {
                Cursor c = query(FavorGoodsEntry.TABLE_FAVOR_GOODS,null,
                        selection,null,null);

                if(c != null) {
                    c.moveToFirst();
                    PublishGoodsInfo data = GoodsModelMapper.transform(c);

                    c.close();
                    subscriber.onNext(data);
                }else{
                    subscriber.onError(new SQLiteException("fail to query favor goods detail"));
                }
            }
        });
    }

    @Override
    public Observable<List<PublishGoodsInfo>> queryFavorGoodsList() {
        Log.v(TAG,"queryFavorGoodsList()");


        return Observable.create(new Observable.OnSubscribe<List<PublishGoodsInfo>>() {
            @Override
            public void call(Subscriber<? super List<PublishGoodsInfo>> subscriber) {
                String sortBy = FavorGoodsEntry.COL_RELEASE_DATE + " ASC";
                Cursor c = query(FavorGoodsEntry.TABLE_FAVOR_GOODS,null,null,null,sortBy);

                if(c != null){
                    List<PublishGoodsInfo> goodsList = new ArrayList<PublishGoodsInfo>();

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
    public Observable<PublisherInfo> queryPublisherDetail(long id) {
        Log.v(TAG,"queryPublisherDetail(): id = " + id);

        final String selection = FavorPublisherEntry._ID + " =? " + id;

        return Observable.create(new Observable.OnSubscribe<PublisherInfo>() {
            @Override
            public void call(Subscriber<? super PublisherInfo> subscriber) {
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
    public Observable<List<PublisherInfo>> queryPublisherList() {
        Log.v(TAG,"queryPublisherList()");

        return Observable.create(new Observable.OnSubscribe<List<PublisherInfo>>() {
            @Override
            public void call(Subscriber<? super List<PublisherInfo>> subscriber) {
                String sortBy = FavorPublisherEntry.COL_DISTANCE + " ASC";
                Cursor c = query(FavorPublisherEntry.TABLE_PUBLISHER,null,
                        null,null,sortBy);
                if(c != null){
                    List<PublisherInfo> publisherList = new ArrayList<PublisherInfo>();

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
    public Observable<List<PublishGoodsInfo>> queryLatestGoodsList() {
        throw new UnsupportedOperationException(
                "it should be queried in remote data source");
    }

    @Override
    public Observable<RepoResponseCode> followPublisher(PublisherInfo publisher) {
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
