package com.github.xzwj87.mineflea.market.presenter;

import android.util.Log;

import com.github.xzwj87.mineflea.market.data.repository.DataRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.ui.BaseView;

import javax.inject.Inject;

/**
 * Created by jason on 11/4/16.
 */
@PerActivity
public class LogoutPresenterImpl extends LogoutPresenter{

    private static final String TAG = LogoutPresenterImpl.class.getSimpleName();

    @Inject
    DataRepository mRepo;

    @Inject
    public LogoutPresenterImpl(DataRepository repository){
        mRepo = repository;
    }

    @Override
    public void logout() {
        Log.v(TAG,"logout()");
        mRepo.logout();
    }

    @Override
    public void init() {
        mRepo.init();
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
