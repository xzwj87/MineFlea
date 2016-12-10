package com.github.xzwj87.mineflea.market.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.amap.api.maps2d.model.LatLng;
import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.ui.fragment.PublishGoodsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jason on 9/27/16.
 */

public class PublishGoodsActivity extends BaseActivity {
    public static final String TAG = PublishGoodsActivity.class.getSimpleName();

    public LatLng myLoc = null;

    @BindView(R.id.toolbar) Toolbar mToolBar;

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

        FragmentManager fragmentManager = getSupportFragmentManager();
        PublishGoodsFragment fragment = (PublishGoodsFragment) fragmentManager.findFragmentByTag(PublishGoodsFragment.TAG);
        if(fragment == null){
            fragment = PublishGoodsFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container,fragment,PublishGoodsFragment.TAG)
                    .commit();

            mMarketComponent.inject(fragment);
        }
    }

    public LatLng getMyLoc(){
        if(getParent() == null)
            return null;
        if(getParent().isFinishing() || getParent().isDestroyed())
            return null;
        if(getParent() instanceof HomeActivity){
            myLoc = ((HomeActivity)getParent()).getMyLoc();
            return myLoc;
        }
        return null;
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
}
