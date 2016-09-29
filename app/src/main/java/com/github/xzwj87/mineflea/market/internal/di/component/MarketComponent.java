package com.github.xzwj87.mineflea.market.internal.di.component;

import android.app.Activity;

import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.internal.di.module.ActivityModule;
import com.github.xzwj87.mineflea.market.internal.di.module.AppModule;
import com.github.xzwj87.mineflea.market.internal.di.module.MarketModule;
import com.github.xzwj87.mineflea.market.ui.activity.PublishGoodsActivity;

import dagger.Component;

/**
 * Created by jason on 9/29/16.
 */

@PerActivity
@Component(dependencies = {AppModule.class},
        modules = {ActivityModule.class,MarketModule.class})
public interface MarketComponent extends ActivityComponent{
    void inject(PublishGoodsActivity activity);
}
