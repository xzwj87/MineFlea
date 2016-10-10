package com.github.xzwj87.mineflea.market.net;

import android.accounts.NetworkErrorException;
import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.github.xzwj87.mineflea.market.data.RepoResponseCode;
import com.github.xzwj87.mineflea.market.exception.NetNoConnectionException;
import com.github.xzwj87.mineflea.market.model.ModelConstants;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.PublisherModel;
import com.github.xzwj87.mineflea.market.model.mapper.GoodsJsonMapper;
import com.github.xzwj87.mineflea.market.model.mapper.PublisherJsonMapper;
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

    private NetDataCallback mNetCb;

    // TODO: we may want to cache data using disk
    public NetDataApiImpl(){
    }

    public interface NetDataCallback{
        void onPublishComplete(int response,String goodsId);
    }

    public void setCallback(NetDataCallback cb){
        mNetCb = cb;
    }

    @Override
    public Observable<RepoResponseCode> publishGoods(final PublishGoodsInfo goods) {
        Log.v(TAG,"publishGoods(): goods = " + goods);

        if(NetConnectionUtils.isNetworkConnected()) {

            final AVObject avObject = new AVObject(ModelConstants.AV_OBJ_GOODS);
            avObject.put(PublishGoodsInfo.GOODS_NAME,goods.getName());
            avObject.put(PublishGoodsInfo.GOODS_PUBLISHER,goods.getPublisher());
            avObject.put(PublishGoodsInfo.GOODS_LOW_PRICE,goods.getLowerPrice());
            avObject.put(PublishGoodsInfo.GOODS_HIGH_PRICE,goods.getHighPrice());
            avObject.put(PublishGoodsInfo.GOODS_RELEASE_DATE,goods.getReleasedDate());

            avObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    Log.v(TAG,"saveInBackground(): done");

                    int code = RepoResponseCode.RESP_SUCCESS;
                    if(e == null){
                        goods.setId(avObject.getObjectId());
                    }else{
                        goods.setId("");
                        code = RepoResponseCode.RESP_AV_SAVED_FAILURE;
                    }

                    Log.v(TAG,"publishGoods(): goods = " + goods);
                    mNetCb.onPublishComplete(code, goods.getId());
                }
            });

        }

        final RepoResponseCode response = new RepoResponseCode(RepoResponseCode.RESP_NETWORK_NOT_CONNECTED);
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
    public Observable<List<PublishGoodsInfo>> queryLatestGoodsList() {
        Log.v(TAG,"queryLatestGoodsList()");

        if(NetConnectionUtils.isNetworkConnected()) {
            HttpUrlApi httpUri = createHttpApi(ALL_GOODS_URL);
            if (httpUri != null) {
                final String json = httpUri.getData();

                return Observable.create(new Observable.OnSubscribe<List<PublishGoodsInfo>>() {
                    @Override
                    public void call(Subscriber<? super List<PublishGoodsInfo>> subscriber) {
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
            return Observable.create(new Observable.OnSubscribe<List<PublishGoodsInfo>>() {
                @Override
                public void call(Subscriber<? super List<PublishGoodsInfo>> subscriber) {
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
