package com.github.xzwj87.mineflea.market.ui.activity;

import android.os.Bundle;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.utils.ThemeColorUtils;

import butterknife.ButterKnife;

/**
 * Created by jason on 9/27/16.
 */

public class GoodsDetailActivity extends BaseActivity {
    public static final String TAG = GoodsDetailActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_nearby_goods);
        ButterKnife.bind(this);
    }

    @Override
    public void onResume(){
        super.onResume();

        checkThemeColor();
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
    protected void checkThemeColor(){
        ThemeColorUtils.changeThemeColor(this);
    }

}