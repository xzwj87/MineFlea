package com.github.xzwj87.mineflea.market.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.util.Log;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.presenter.RegisterPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.fragment.RegisterFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.RegisterSecondStepFragment;
import com.github.xzwj87.mineflea.utils.ThemeColorUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by jason on 10/13/16.
 */

public class RegisterActivity extends BaseActivity implements RegisterFragment.NextStepCallback{
    public static final String TAG = RegisterActivity.class.getSimpleName();

    @Inject RegisterPresenterImpl mPresenter;

    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

/*        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);
            actionBar.setTitle(R.string.action_register);
        }

        initInjector();

        init();

        ThemeColorUtils.changeThemeColor(this);

        FragmentManager fragmentMgr = getSupportFragmentManager();
        RegisterFragment fragment = (RegisterFragment)fragmentMgr.findFragmentByTag(RegisterFragment.TAG);
        if(fragment == null){
            fragment = RegisterFragment.newInstance();
            fragmentMgr.beginTransaction()
                       .add(R.id.fragment_container,fragment,RegisterFragment.TAG)
                       .commit();

            fragment.setPresenter(mPresenter);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        FragmentManager fragmentMgr = getSupportFragmentManager();
        List<Fragment> fragments = fragmentMgr.getFragments();
        for(int i = 0; i < fragments.size(); ++i){
            Fragment fragment = fragments.get(i);
            if(fragment != null ) {
                if (RegisterFragment.TAG.equals(fragment.getTag())) {
                    ((RegisterFragment) fragment).setPresenter(mPresenter);
                } else {
                    ((RegisterSecondStepFragment) fragment).setPresenter(mPresenter);
                }
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();

        mPresenter.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        mPresenter.onDestroy();
    }

    private void init(){
        mPresenter.init();
    }

    private void initInjector(){
        mMarketComponent.inject(this);
    }

    @Override
    public void onActivityResult(int request, int result, Intent data){
        //super.onActivityResult(request,result,data);
        Log.v(TAG,"onActivityResult(): result = " + result);
        if(request == RegisterFragment.REQUEST_LOGIN){
            setResult(result,data);
            finish();
        }
    }

    @Override
    public void onNextStep() {
        RegisterSecondStepFragment fragment;
        FragmentManager fragmentMgr = getSupportFragmentManager();

        fragment = (RegisterSecondStepFragment)fragmentMgr.findFragmentByTag(RegisterSecondStepFragment.class.getSimpleName());
        if(fragment == null){
            fragment = RegisterSecondStepFragment.newInstance();
        }

        fragmentMgr.beginTransaction()
                .replace(R.id.fragment_container,fragment,RegisterSecondStepFragment.class.getSimpleName())
                .commit();

        fragment.setPresenter(mPresenter);

    }
}
