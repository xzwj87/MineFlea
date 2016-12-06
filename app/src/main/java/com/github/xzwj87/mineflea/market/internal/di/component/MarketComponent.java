package com.github.xzwj87.mineflea.market.internal.di.component;

import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.internal.di.module.ActivityModule;
import com.github.xzwj87.mineflea.market.internal.di.module.MarketModule;
import com.github.xzwj87.mineflea.market.ui.activity.EditPersonalInfoActivity;
import com.github.xzwj87.mineflea.market.ui.activity.GoodsDetailActivity;
import com.github.xzwj87.mineflea.market.ui.activity.LoginActivity;
import com.github.xzwj87.mineflea.market.ui.activity.RegisterActivity;
import com.github.xzwj87.mineflea.market.ui.activity.UserDetailActivity;
import com.github.xzwj87.mineflea.market.ui.fragment.DiscoverTabFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.NearbyTabFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.PublishGoodsFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.UserCenterFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.UserFavoritesFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.UserFolloweeFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.UserFollowerFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.UserPublishedGoodsFragment;
import com.github.xzwj87.mineflea.market.ui.settings.SettingsFragment;

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
    void inject(LoginActivity activity);
    void inject(NearbyTabFragment fragment);
    void inject(DiscoverTabFragment fragment);
    void inject(UserCenterFragment fragment);
    void inject(UserDetailActivity activity);
    void inject(UserPublishedGoodsFragment fragment);
    void inject(UserFavoritesFragment fragment);
    void inject(UserFollowerFragment fragment);
    void inject(UserFolloweeFragment fragment);
    void inject(EditPersonalInfoActivity activity);
    void inject(SettingsFragment fragment);
    void inject(GoodsDetailActivity activity);
}
