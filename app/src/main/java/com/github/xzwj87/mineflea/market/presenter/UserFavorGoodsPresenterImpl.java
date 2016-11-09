package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;
import android.util.Log;

import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserGoodsInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.UserGoodsView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by jason on 10/30/16.
 */

@PerActivity
public class UserFavorGoodsPresenterImpl extends UserFavorGoodsPresenter{
    private static final String TAG = UserFavorGoodsPresenterImpl.class.getSimpleName();

    @Inject MineFleaRepository mRepo;
    private UserGoodsView mView;
    private List<PublishGoodsInfo> mGoodsList;

    @Inject
    public UserFavorGoodsPresenterImpl(MineFleaRepository repository){
        mRepo = repository;
    }

    @Override
    public void init() {
        mRepo.init();
        mRepo.registerCallBack(PRESENTER_FAVOR,new FavorPresenterCallback());
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        mGoodsList = null;
        mRepo.unregisterCallback(PRESENTER_FAVOR);
    }

    @Override
    public void setView(BaseView view) {
        mView = (UserGoodsView) view;
    }

    @Override
    public void getFavorGoodsList(String id) {
        mRepo.queryFavorGoodsListByUserId(id);
    }

    @Override
    public int getGoodsCount() {
        return mGoodsList.size();
    }

    @Override
    public UserGoodsInfo getGoodsAtPos(int pos) {
        UserGoodsInfo goods = new UserGoodsInfo();
        if(mGoodsList != null){

            goods.setName(mGoodsList.get(pos).getName());
            goods.setPrice(mGoodsList.get(pos).getPrice());
            goods.setLikes(mGoodsList.get(pos).getStars());
            goods.setGoodsImgUrl(mGoodsList.get(pos).getImageUri());
        }

        return goods;
    }

    private void renderView(){
        mView.renderView();
    }

    private class FavorPresenterCallback implements PresenterCallback {
        @SuppressWarnings("unchecked")
        @Override
        public void onComplete(Message message) {
            Log.v(TAG,"onComplete(): resp = " + message.what);
            if(message.obj != null){
                mGoodsList = (List<PublishGoodsInfo>)message.obj;

                renderView();
            }else{
                mView.showBlankPage();
            }

            mView.showProgress(false);
        }

        @Override
        public void onNext(Message message) {

        }

        @Override
        public void onError(Throwable e) {

        }
    }
}
