package com.github.xzwj87.mineflea.market.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.GoodsDetailPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.GoodsDetailView;
import com.github.xzwj87.mineflea.market.ui.adapter.ImagePageAdapter;
import com.github.xzwj87.mineflea.utils.PicassoUtils;
import com.github.xzwj87.mineflea.utils.ThemeColorUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jason on 9/27/16.
 */

public class GoodsDetailActivity extends BaseActivity implements GoodsDetailView {
    public static final String TAG = GoodsDetailActivity.class.getSimpleName();

    private String mGoodsId;
    @Inject GoodsDetailPresenterImpl mPresenter;

    private ProgressDialog mProgress;

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

        initView();
        // inject presenter
        getComponent().inject(this);

        ThemeColorUtils.changeThemeColor(this);
    }

    @Override
    public void onResume(){
        super.onResume();

        mPresenter.init();
        mPresenter.setView(this);
        mPresenter.getGoodsInfo(mGoodsId);

        mProgress = ProgressDialog.show(this,"",
                getString(R.string.progress_get_goods_detail));
    }

    @Override
    public void onPause(){
        super.onPause();

        mPresenter.onPause();

        if(mProgress != null && mProgress.isShowing()){
            mProgress.dismiss();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        mPresenter.onDestroy();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.iv_more})
    public void addToFavorites(){
        PopupMenu menu = new PopupMenu(this,mIvCheckMore);
        menu.inflate(R.menu.menu_add_to_favoriates);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mPresenter.addToFavorites(mGoodsId);
                return true;
            }
        });
    }

    @Override
    public void onGetGoodsInfoDone(boolean success) {
        if(mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    public void updateImageListPage(List<String> imgList) {
        ImagePageAdapter adapter = (ImagePageAdapter)mVpGoodsImages.getAdapter();
        adapter.setImageList(imgList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateGoodsName(String name) {
        mTvName.setText(name);
    }

    @Override
    public void updateGoodsPrice(double price) {
        mTvPrice.setText(String.valueOf(price));
    }

    @Override
    public void updateGoodsLocation(String location) {
        mTvLocation.setText(location);
    }

    @Override
    public void updateLikes(int likes) {
        mTvLikes.setText(String.valueOf(likes));
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        if(userInfo != null){
            ImageView header = (ImageView)mRlUserInfo.findViewById(R.id.iv_user_header);
            TextView nickName = (TextView)mRlUserInfo.findViewById(R.id.tv_user_nick_name);
            TextView email = (TextView)mRlUserInfo.findViewById(R.id.tv_email);
            TextView publishedGoodsCnt = (TextView)mRlUserInfo.findViewById(R.id.tv_published_goods_count);
            //TextView favorGoodsCnt = (TextView)mRlUserInfo.findViewById(R.id.tv_favorite_goods);
            TextView followers = (TextView)mRlUserInfo.findViewById(R.id.tv_followers);

            if(!TextUtils.isEmpty(userInfo.getHeadIconUrl())){
                PicassoUtils.loadImage(header,userInfo.getHeadIconUrl());
            }else{
                header.setBackgroundResource(R.drawable.ic_account_circle_grey_72dp);
            }

            nickName.setText(userInfo.getNickName());
            email.setText(userInfo.getUserEmail());
            publishedGoodsCnt.setText(String.valueOf(userInfo.getGoodsCount()));
            followers.setText(String.valueOf(userInfo.getFollowersCount()));
        }
    }

    @Override
    public void finishView() {
        finish();
    }

    private void initView(){
        ImagePageAdapter adapter = new ImagePageAdapter(this);
        mVpGoodsImages.setAdapter(adapter);
    }
}
