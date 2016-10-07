package com.github.xzwj87.mineflea.market.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.internal.di.HasComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.DaggerMarketComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.MarketComponent;
import com.github.xzwj87.mineflea.market.model.GoodsModel;
import com.github.xzwj87.mineflea.market.presenter.PublishGoodsPresenter;
import com.github.xzwj87.mineflea.market.presenter.PublishGoodsPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.PublishGoodsView;
import com.github.xzwj87.mineflea.market.ui.fragment.PublishGoodsFragment;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jason on 9/27/16.
 */

public class PublishGoodsActivity extends BaseActivity
        implements HasComponent<MarketComponent> {
    public static final String TAG = PublishGoodsActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar mToolBar;

    private GoodsModel mGoods;
    private MarketComponent mMarketComponent;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_publish_goods);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        }

        initInjector();

        FragmentManager fragmentManager = getSupportFragmentManager();
        PublishGoodsFragment fragment = (PublishGoodsFragment) fragmentManager.findFragmentByTag(PublishGoodsFragment.TAG);
        if(fragment == null){
            fragment = PublishGoodsFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container,fragment,PublishGoodsFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public MarketComponent getComponent() {
        return mMarketComponent;
    }

    private void initInjector(){
        mMarketComponent = DaggerMarketComponent.builder()
                .appComponent(getAppComponent())
                .activityModule(getActivityModule())
                .build();

        mMarketComponent.inject(this);
    }
}
