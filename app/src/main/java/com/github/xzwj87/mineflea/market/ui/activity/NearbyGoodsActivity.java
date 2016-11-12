package com.github.xzwj87.mineflea.market.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.utils.ThemeColorUtils;

import butterknife.ButterKnife;

/**
 * Created by jason on 9/27/16.
 */

public class NearbyGoodsActivity extends BaseActivity {
    public static final String TAG = NearbyGoodsActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_nearby_goods);
        ButterKnife.bind(this);

        checkThemeColor();
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
    protected void checkThemeColor(){
        ThemeColorUtils.changeThemeColor(this);
    }
}
