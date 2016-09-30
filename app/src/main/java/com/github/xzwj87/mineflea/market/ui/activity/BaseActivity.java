package com.github.xzwj87.mineflea.market.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.internal.di.HasComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.ActivityComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.AppComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.DaggerActivityComponent;
import com.github.xzwj87.mineflea.market.internal.di.module.ActivityModule;

/**
 * Created by jason on 9/29/16.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedState){
        super.onCreate(savedState);

        getAppComponent().inject(this);
    }

    public AppComponent getAppComponent() {
        return ((AppGlobals)getApplication()).getComponent();
    }

    public ActivityModule getActivityModule(){
        return new ActivityModule(this);
    }
}
