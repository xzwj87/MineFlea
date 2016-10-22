package com.github.xzwj87.mineflea.market.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.internal.di.HasComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.DaggerMarketComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.MarketComponent;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.ui.fragment.UserDetailFragment;

/**
 * Created by jason on 10/22/16.
 */

public class UserDetailActivity extends BaseActivity
            implements HasComponent<MarketComponent>{
    private static final String TAG = UserDetailActivity.class.getSimpleName();

    private MarketComponent mMarketComponent;

    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);

        setContentView(R.layout.activity_user_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.user_detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);
        }


        initInjector();

        FragmentManager fragmentMgr = getSupportFragmentManager();
        UserDetailFragment fragment = (UserDetailFragment)fragmentMgr.
                findFragmentByTag(UserDetailFragment.TAG);

        if(fragment == null){
            String userId = getIntent().getStringExtra(UserInfo.USER_ID);
            fragment = UserDetailFragment.newInstance(userId);
            fragmentMgr.beginTransaction()
                       .add(fragment,UserDetailFragment.TAG)
                       .commit();

            mMarketComponent.inject(fragment);
        }
    }

    private void initInjector(){
        mMarketComponent =
                DaggerMarketComponent.builder()
                                     .appComponent(getAppComponent())
                                     .activityModule(getActivityModule())
                                     .build();

    }

    @Override
    public MarketComponent getComponent() {
        return mMarketComponent;
    }
}
