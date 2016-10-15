package com.github.xzwj87.mineflea.market.presenter;

import com.github.xzwj87.mineflea.market.ui.BaseView;

/**
 * Created by jason on 9/27/16.
 */

public interface BasePresenter {

    void init();

    void onPause();

    void onDestroy();

    void setView(BaseView view);
}
