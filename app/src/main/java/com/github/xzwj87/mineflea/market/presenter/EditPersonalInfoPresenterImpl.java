package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.EditPersonalInfoView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by jason on 11/2/16.
 */

@PerActivity
public class EditPersonalInfoPresenterImpl extends EditPersonalInfoPresenter{

    private EditPersonalInfoView mView;
    private MineFleaRepository mRepo;

    @Inject
    public EditPersonalInfoPresenterImpl(@Named("dataRepository")MineFleaRepository repository){
        mRepo = repository;
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
        mView = (EditPersonalInfoView)view;
    }

    @Override
    public void setNickName(String nickName) {

    }

    @Override
    public void setHeadIcon(String iconUrl) {

    }

    @Override
    public void setEmail(String email) {

    }

    @Override
    public void setTelNumber(String telNumber) {

    }

    @Override
    public void setIntro(String intro) {

    }
}
