package com.github.xzwj87.mineflea.market.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.utils.ThemeColorUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jason on 9/27/16.
 */

public class GoodsDetailActivity extends BaseActivity {
    public static final String TAG = GoodsDetailActivity.class.getSimpleName();

    private String mGoodsId;

    @BindView(R.id.vp_photos) ViewPager mVpGoodsImages;
    @BindView(R.id.tv_goods_name) TextView mTvName;
    @BindView(R.id.tv_goods_price) TextView mTvPrice;
    @BindView(R.id.tv_location) TextView mTvLocation;
    @BindView(R.id.tv_likes) TextView mTvLikes;
    @BindView(R.id.iv_more) ImageView mIvCheckMore;
    @BindView(R.id.layout_user_info) RelativeLayout mRlUserInfo;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_goods_detail);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mGoodsId = intent.getStringExtra(PublishGoodsInfo.GOODS_ID);
    }

    @Override
    public void onResume(){
        super.onResume();

        ThemeColorUtils.changeThemeColor(this);
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
