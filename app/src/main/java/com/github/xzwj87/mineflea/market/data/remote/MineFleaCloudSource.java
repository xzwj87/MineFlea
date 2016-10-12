package com.github.xzwj87.mineflea.market.data.remote;

import android.os.Message;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.github.xzwj87.mineflea.market.data.DataSource;
import com.github.xzwj87.mineflea.market.data.RepoResponseCode;
import com.github.xzwj87.mineflea.market.data.repository.BaseRepository;
import com.github.xzwj87.mineflea.market.model.ModelConstants;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.PublisherInfo;
import com.github.xzwj87.mineflea.market.net.NetDataApi;
import com.github.xzwj87.mineflea.market.net.NetDataApiImpl;
import com.github.xzwj87.mineflea.utils.NetConnectionUtils;

import java.util.List;

import rx.Observable;

/**
 * Created by JasonWang on 2016/9/20.
 */
public class MineFleaCloudSource implements DataSource{
    public static final String TAG = MineFleaCloudSource.class.getSimpleName();

    private NetDataApiImpl mNetApi;
    private static MineFleaCloudSource sInstance;
    private CloudSourceCallback mCloudCallback;

    public MineFleaCloudSource(NetDataApi netApi){
        mNetApi = (NetDataApiImpl)netApi;
    }

    public void setCloudCallback(CloudSourceCallback callback){
        mCloudCallback = callback;
    }

    public static MineFleaCloudSource getInstance(){
        if(sInstance == null){
            sInstance = new MineFleaCloudSource(new NetDataApiImpl());
        }

        return sInstance;
    }

    public interface CloudSourceCallback{
        void publishComplete(Message message);
    }

    /**
     * release goods
     *
     * @param goods
     * @source: local and remote
     */
    @Override
    public void publishGoods(final PublishGoodsInfo goods) {

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

                    final Message msg = new Message();
                    msg.arg1 = code;
                    msg.obj = goods.getId();
                    mCloudCallback.publishComplete(new Message());
                }
            });

        }else{
            // broadcast to upper layer about network state
        }
    }

    /**
     * query the info of a publisher
     *
     * @param id
     * @source: remote
     */
    @Override
    public Observable<PublisherInfo> queryPublisherDetail(long id) {
        Log.v(TAG,"queryPublisherDetail(): id = " + id);

        return mNetApi.queryPublisherDetail(id);
    }

    @Override
    public Observable<List<PublisherInfo>> queryPublisherList() {
        throw new UnsupportedOperationException("queryPublisherList() should not be" +
                " called in cloud resource");
    }

    /**
     * query latest goods list
     *
     * @source: remote
     */
    @Override
    public Observable<List<PublishGoodsInfo>> queryLatestGoodsList() {
        Log.v(TAG,"queryLatestGoodsList()");

        return mNetApi.queryLatestGoodsList();
    }

    /**
     * follow some publisher
     *
     * @param publisher
     * @source: remote
     */
    @Override
    public Observable<RepoResponseCode> followPublisher(PublisherInfo publisher) {
        Log.v(TAG,"followPublisher(): publisher = " + publisher);

            return mNetApi.followPublisher(publisher);
    }

    /**
     * query the detail of a goods
     *
     * @param id
     * @source: local
     */
    @Override
    public Observable<PublishGoodsInfo> queryPublishedGoodsDetail(long id) {
        throw new UnsupportedOperationException(
                "queryPublishedGoodsDetail() should be called in local data base");
    }

    @Override
    public Observable<List<PublishGoodsInfo>> queryPublishedGoodsList() {
        throw new UnsupportedOperationException(
                "queryPublishedGoodsList() should be called in local data base");
    }

    /**
     * add goods to a favorite one
     *
     * @param goods
     * @source: local
     */

    // TODO: it need to notify the Server about the state of this goods
    @Override
    public Observable<RepoResponseCode> favorGoods(PublishGoodsInfo goods) {
        throw new UnsupportedOperationException("favorGoods() " +
                "should be called in local data base");
    }

    /**
     * query the detail info of a favored goods
     *
     * @param id
     * @source: local
     */
    @Override
    public Observable<PublishGoodsInfo> queryFavorGoodsDetail(long id) {
        throw new UnsupportedOperationException(
                "queryFavorGoodsDetail() should be called in local data base");
    }

    @Override
    public Observable<List<PublishGoodsInfo>> queryFavorGoodsList() {
        throw new UnsupportedOperationException(
                "queryFavorGoodsList() should be called in local data base");
    }
}
