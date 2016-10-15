package com.github.xzwj87.mineflea.market.internal.di.component;

import android.app.Activity;

import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.internal.di.module.ActivityModule;
import com.github.xzwj87.mineflea.market.internal.di.module.AppModule;
import com.github.xzwj87.mineflea.market.internal.di.module.MarketModule;
import com.github.xzwj87.mineflea.market.ui.activity.PublishGoodsActivity;
import com.github.xzwj87.mineflea.market.ui.activity.RegisterActivity;
import com.github.xzwj87.mineflea.market.ui.fragment.PublishGoodsFragment;

import dagger.Component;

/**
 * Created by jason on 9/29/16.
 */

@PerActivity
@Component(dependencies = {AppComponent.class},
        modules = {ActivityModule.class,MarketModule.class})
public interface MarketComponent extends ActivityComponent{
    void inject(PublishGoodsFragment fragment);
    void inject(RegisterActivity activity);
}
