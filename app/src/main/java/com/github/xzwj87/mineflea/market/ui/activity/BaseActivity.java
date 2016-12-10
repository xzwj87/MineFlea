package com.github.xzwj87.mineflea.market.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.internal.di.HasComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.AppComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.DaggerAppComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.DaggerMarketComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.MarketComponent;
import com.github.xzwj87.mineflea.market.internal.di.module.ActivityModule;
import com.github.xzwj87.mineflea.utils.ThemeColorUtils;

/**
 * Created by jason on 9/29/16.
 */

public class BaseActivity extends AppCompatActivity
            implements HasComponent<MarketComponent>{

    protected MarketComponent mMarketComponent;

    @Override
    protected void onCreate(Bundle savedState){
        super.onCreate(savedState);
        // reduce overdrawing
        getWindow().setBackgroundDrawable(null);
        getAppComponent().inject(this);

        initInjector();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    public AppComponent getAppComponent() {
        return ((AppGlobals)getApplication()).getComponent();
    }

    public ActivityModule getActivityModule(){
        return new ActivityModule(this);
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
