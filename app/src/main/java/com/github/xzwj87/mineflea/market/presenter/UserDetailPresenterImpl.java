package com.github.xzwj87.mineflea.market.presenter;

import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by jason on 10/22/16.
 */

@PerActivity
public class UserDetailPresenterImpl extends UserDetailPresenter{
    private static final String TAG = UserDetailPresenterImpl.class.getSimpleName();

    private MineFleaRepository mDataRepo;

    @Inject
    public UserDetailPresenterImpl(@Named("dataRepository")MineFleaRepository repository){
        mDataRepo = repository;
    }

    @Override
    public UserInfo getUserInfoById(String id) {
        return null;
    }

    @Override
    public void updateFollowers() {

    }

    @Override
    public List<PublishGoodsInfo> getGoodsList(String userId) {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void setView(BaseView view) {

    }
}
