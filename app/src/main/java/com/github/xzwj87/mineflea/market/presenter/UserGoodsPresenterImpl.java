package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import static com.github.xzwj87.mineflea.market.data.ResponseCode.*;
import com.github.xzwj87.mineflea.market.data.repository.DataRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserGoodsInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.UserGoodsView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by jason on 10/27/16.
 */

@PerActivity
public class UserGoodsPresenterImpl extends UserGoodsPresenter {

    @Inject
    DataRepository mRepo;
    private UserGoodsView mView;
    private List<PublishGoodsInfo> mGoodsList;

    @Inject
    public UserGoodsPresenterImpl(DataRepository repository){
        mRepo = repository;
    }

    @Override
    public void init() {
        mRepo.init();
        mRepo.registerCallBack(PRESENTER_GOODS_LIST,new UserGoodsPresenterCallback());
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        mRepo.unregisterCallback(PRESENTER_GOODS_LIST);
        mGoodsList = null;
        mRepo = null;
    }

    @Override
    public void setView(BaseView view) {
        mView = (UserGoodsView)view;
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


    private void renderView(){
        mView.renderView();
    }

    private class UserGoodsPresenterCallback implements PresenterCallback {
        @SuppressWarnings("unchecked")
        @Override
        public void onComplete(Message message) {
            int what = message.what;
            switch (what) {
                case RESP_GET_GOODS_LIST_SUCCESS:
                    if (message.obj != null) {
                        mGoodsList = (List<PublishGoodsInfo>) message.obj;

                        renderView();
                        // empty list or fail
                    } else if (message.arg1 == 0) {
                        mView.showBlankPage();
                    }

                    mView.showProgress(false);
                    break;
                case RESP_GET_USER_INFO_SUCCESS:
                    break;
            }
        }

        @Override
        public void onNext(Message message) {

        }

        @Override
        public void onError(Throwable e) {

        }
    }

}
