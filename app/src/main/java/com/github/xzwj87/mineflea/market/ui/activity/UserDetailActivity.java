package com.github.xzwj87.mineflea.market.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.internal.di.HasComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.DaggerMarketComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.MarketComponent;
import com.github.xzwj87.mineflea.market.ui.adapter.UserDetailPageAdapter;

import butterknife.BindView;

/**
 * Created by jason on 10/22/16.
 */

public class UserDetailActivity extends BaseActivity
            implements HasComponent<MarketComponent>{
    private static final String TAG = UserDetailActivity.class.getSimpleName();

    private MarketComponent mMarketComponent;

    private ViewPager mPage;
    private UserDetailPageAdapter mPageAdapter;

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

        mPage = (ViewPager)findViewById(R.id.pager_container);
        mPageAdapter = new UserDetailPageAdapter(this,getSupportFragmentManager());
        mPage.setAdapter(mPageAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.user_detail_tab);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(mPage);

        for(int i = 0; i < tabLayout.getTabCount(); ++i){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(mPageAdapter.getTabView(i));
        }

        initInjector();
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
