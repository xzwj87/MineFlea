package com.github.xzwj87.mineflea.net;

import android.accounts.NetworkErrorException;
import android.text.TextUtils;
import android.util.Log;

import com.github.xzwj87.mineflea.data.RepoResponseCode;
import com.github.xzwj87.mineflea.exception.NetNoConnectionException;
import com.github.xzwj87.mineflea.model.GoodsModel;
import com.github.xzwj87.mineflea.model.PublisherModel;
import com.github.xzwj87.mineflea.model.mapper.GoodsJsonMapper;
import com.github.xzwj87.mineflea.model.mapper.PublisherJsonMapper;
import com.github.xzwj87.mineflea.utils.NetConnectionUtils;

import java.net.MalformedURLException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by jason on 9/25/16.
 */

public class NetDataApiImpl implements NetDataApi{
    public static final String TAG = NetDataApiImpl.class.getSimpleName();

    private static final String BASE_URL = "www.baidu.com";
    private static final String GOODS_DETAIL_URL = " ";
    private static final String ALL_GOODS_URL = " ";
    private static final String PUBLISHER_DETAIL_URL = " ";

    // TODO: we may want to cache data using disk
    public NetDataApiImpl(){
    }

    @Override
    public Observable<RepoResponseCode> publishGoods(GoodsModel goods) {
        Log.v(TAG,"publishGoods(): goods = " + goods);

        int responseCode = RepoResponseCode.RESP_SUCCESS;
        if(NetConnectionUtils.isNetworkConnected()) {
            HttpUrlApi httpApi = createHttpApi(BASE_URL);
            if (httpApi != null) {
                String resp = httpApi.postData(GoodsJsonMapper.map(goods));
                if(TextUtils.isEmpty(resp)){
                    responseCode = RepoResponseCode.RESP_NETWORK_ERROR;
                }
            }
        }else{
            responseCode = RepoResponseCode.RESP_NETWORK_NOT_CONNECTED;
        }

        final RepoResponseCode response = new RepoResponseCode(responseCode);
        return Observable.create(new Observable.OnSubscribe<RepoResponseCode>() {
            @Override
            public void call(Subscriber<? super RepoResponseCode> subscriber) {
                subscriber.onNext(response);
            }
        });
    }

    @Override
    public Observable<PublisherModel> queryPublisherDetail(long id) {
        Log.v(TAG,"queryPublisherDetail()");

        if(NetConnectionUtils.isNetworkConnected()) {
            HttpUrlApi httpApi = createHttpApi(PUBLISHER_DETAIL_URL + id);

            if (httpApi != null) {
                final String json = httpApi.getData();
                return Observable.create(new Observable.OnSubscribe<PublisherModel>() {
                    @Override
                    public void call(Subscriber<? super PublisherModel> subscriber) {
                        if (json != null) {
                            subscriber.onNext(PublisherJsonMapper.transform(json));
                        } else {
                            subscriber.onError(new NetworkErrorException("queryPublisherDetail()" +
                                    "fail to get publisher detail for data received is null"));
                        }
                    }
                });
            }
        }else{
            return Observable.create(new Observable.OnSubscribe<PublisherModel>() {
                @Override
                public void call(Subscriber<? super PublisherModel> subscriber) {
                    subscriber.onError(new NetNoConnectionException("network is not connected"));
                }
            });
        }

        return null;
    }

    @Override
    public Observable<List<GoodsModel>> queryLatestGoodsList() {
        Log.v(TAG,"queryLatestGoodsList()");

        if(NetConnectionUtils.isNetworkConnected()) {
            HttpUrlApi httpUri = createHttpApi(ALL_GOODS_URL);
            if (httpUri != null) {
                final String json = httpUri.getData();

                return Observable.create(new Observable.OnSubscribe<List<GoodsModel>>() {
                    @Override
                    public void call(Subscriber<? super List<GoodsModel>> subscriber) {
                        if (json != null) {
                            subscriber.onNext(GoodsJsonMapper.transformToList(json));
                        } else {
                            subscriber.onError(new NetworkErrorException("queryLatestGoodsList():" +
                                    "fail to get latest goods list"));
                        }
                    }
                });

            }
        }else{
            return Observable.create(new Observable.OnSubscribe<List<GoodsModel>>() {
                @Override
                public void call(Subscriber<? super List<GoodsModel>> subscriber) {
                    subscriber.onError(new NetNoConnectionException("network is not connected"));
                }
            });
        }

        return null;
    }

    @Override
    public Observable<RepoResponseCode> followPublisher(PublisherModel publisher) {
        Log.v(TAG,"followPublisher(): publisher = " + publisher);

        int responseCode = RepoResponseCode.RESP_SUCCESS;
        if(NetConnectionUtils.isNetworkConnected()) {
            HttpUrlApi httpApi = createHttpApi(BASE_URL);
            if (httpApi != null) {
                String resp = httpApi.postData(PublisherJsonMapper.map(publisher));

                if(TextUtils.isEmpty(resp)){
                    responseCode = RepoResponseCode.RESP_NETWORK_ERROR;
                }
            }
        }else{
            responseCode = RepoResponseCode.RESP_NETWORK_NOT_CONNECTED;
        }

        final RepoResponseCode response = new RepoResponseCode(responseCode);

        return Observable.create(new Observable.OnSubscribe<RepoResponseCode>() {
            @Override
            public void call(Subscriber<? super RepoResponseCode> subscriber) {
                subscriber.onNext(response);
            }
        });
    }

    private HttpUrlApi createHttpApi(String url){
        try {
            return HttpUrlApi.create(url);
        }catch (MalformedURLException e){
            Log.e(TAG,"fail to create http connection");
            e.printStackTrace();
        }

        return null;
    }
}
