package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserGoodsInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.UserPublishedGoodsView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by jason on 10/27/16.
 */

@PerActivity
public class UserGoodsPresenterImpl extends UserGoodsPresenter {

    private MineFleaRepository mRepo;
    private UserPublishedGoodsView mView;
    private List<PublishGoodsInfo> mGoodsList;

    @Inject
    public UserGoodsPresenterImpl(@Named("dataRepository")MineFleaRepository repository){
        mRepo = repository;
    }

    @Override
    public void init() {
        mRepo.init();
        mRepo.setPresenterCallback(this);

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void setView(BaseView view) {
        mView = (UserPublishedGoodsView)view;
    }

    @Override
    public void getGoodsListByUserId(String userId) {
        mView.showProgress(true);
        mRepo.getGoodsListByUserId(userId);
    }

    @Override
    public int getGoodsCount() {
        return mGoodsList.size();
    }

    @Override
    public UserGoodsInfo getGoodsAtPos(int pos) {
        UserGoodsInfo goods = new UserGoodsInfo();

        if(mGoodsList != null) {

            goods.setName(mGoodsList.get(pos).getName());
            goods.setPrice(mGoodsList.get(pos).getPrice());
            goods.setLikes(mGoodsList.get(pos).getStars());
            goods.setGoodsImgUrl(mGoodsList.get(pos).getImageUri());
        }

        return goods;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onGetGoodsListDone(Message message) {
        if(message.obj != null){
            mGoodsList = (List<PublishGoodsInfo>)message.obj;

            renderView();
        // empty list or fail
        }else if(message.arg1 == 0){
            mView.showBlankPage();
        }

        mView.showProgress(false);
    }

    private void renderView(){
        mView.renderView();
    }

    @Override
    public void onGetUserInfoComplete(Message message) {

    }

}
