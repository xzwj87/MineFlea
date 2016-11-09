package com.github.xzwj87.mineflea.market.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.internal.di.component.MarketComponent;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.UserDetailPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.UserDetailView;
import com.github.xzwj87.mineflea.market.ui.adapter.UserDetailPageAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jason on 10/22/16.
 */

public class UserDetailActivity extends BaseActivity implements UserDetailView{
    private static final String TAG = UserDetailActivity.class.getSimpleName();

    private ViewPager mPage;
    private UserDetailPageAdapter mPageAdapter;

    @Inject UserDetailPresenterImpl mPresenter;

    @BindView(R.id.head_icon) ImageView mIvHeadIcon;
    @BindView(R.id.tv_user_nick_name) TextView mTvNickName;
    @BindView(R.id.tv_user_email) TextView mTvEmail;
    @BindView(R.id.btn_user_action) TextView mTvAction;
    @BindView(R.id.user_detail_toolbar_layout) CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.user_detail_appbar) AppBarLayout mAppBar;

    private String mUserId;
    private int mInflateMenuId;
    private boolean mIsMe;

    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);

        Intent intent = getIntent();
        mUserId = intent.getStringExtra(UserInfo.USER_ID);

        setContentView(R.layout.activity_user_detail);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.user_detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);
        }

        mPage = (ViewPager)findViewById(R.id.pager_container);
        mPageAdapter = new UserDetailPageAdapter(mUserId,this,getSupportFragmentManager());
        mPage.setAdapter(mPageAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.user_detail_tab);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(mPage);

        for(int i = 0; i < tabLayout.getTabCount(); ++i) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(mPageAdapter.getTabView(i));
        }

        initInjector();

        init();

        initView();
    }

    private void initInjector(){
        mMarketComponent.inject(this);
    }

    @Override
    public MarketComponent getComponent() {
        return mMarketComponent;
    }


    private void init(){
        mPresenter.init();
        mPresenter.setView(this);

        getUserInfo();
    }

    @Override
    public void showLoadProcess(boolean stop) {

    }

    @Override
    public void showGetInfoFailMsg() {
        showToast(getString(R.string.error_get_user_info));
    }

    @Override
    public void renderHeadIcon(String iconUrl) {
        if(!TextUtils.isEmpty(iconUrl)) {
            Picasso.with(this)
                    .load(iconUrl)
                    .resize(1024, 1024)
                    .centerCrop()
                    .into(mIvHeadIcon);
        }else{
            Picasso.with(this)
                    .load(Uri.fromFile(new File(iconUrl)))
                    .resize(1024, 1024)
                    .centerCrop()
                    .into(mIvHeadIcon);
        }
    }

    @Override
    public void renderNickName(String name) {
        mTvNickName.setText(name);
    }

    @Override
    public void renderEmail(String email) {
        mTvEmail.setText(email);
    }

    @Override
    public void updateActionButton(@ACTION_STATE int state) {
        int resId = 0;
        switch (state){
            case IS_ME:
                resId = R.drawable.ic_edit_white_16dp;
                mTvAction.setText(R.string.edit);
                break;
            case ALREADY_FOLLOW:
                resId = R.drawable.ic_done_white_16dp;
                mTvAction.setText(R.string.action_already_followee);
                break;
            case NOT_FOLLOW:
                resId = R.drawable.ic_add_white_16dp;
                mTvAction.setText(R.string.follow);
                break;
            default:
                break;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTvAction.setCompoundDrawablesWithIntrinsicBounds(
                    getDrawable(resId),
                    null, null, null);
        }else{
            mTvAction.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(resId),
                    null, null, null);
        }
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(mInflateMenuId, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finishView();
                break;
            case R.id.edit:
                Intent intent = new Intent(this,EditPersonalInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.favorite:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_user_action)
    public void editOrFavor(){
        if(mIsMe){
            Intent intent = new Intent(this,EditPersonalInfoActivity.class);
            startActivity(intent);
        }else{
            if(mPresenter.isMyFollowee(mUserId)){
                mPresenter.unFollow(mUserId);
            }else{
                mPresenter.follow(mUserId);
            }
        }
    }

    private void getUserInfo(){
        mPresenter.getUserInfoById(mUserId);
    }

    private void initView(){

        mInflateMenuId = R.menu.menu_user_detail_others;

        mIsMe = false;
        if(mPresenter != null){
            if(mPresenter.isMe()){
                mIsMe = true;
                mInflateMenuId = R.menu.menu_user_detail_me;
                mTvAction.setText(R.string.action_edit);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mTvAction.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_edit_white_16dp),
                            null, null, null);
                }else{
                    mTvAction.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_white_16dp),
                            null, null, null);
                }
            }
        }

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= mAppBar.getTotalScrollRange()) {
                    // ok, collapsed
                    mToolbarLayout.setTitleEnabled(true);
                    mToolbarLayout.setTitle(mTvNickName.getText());
                    mToolbarLayout.setCollapsedTitleTextAppearance(android.R.style.TextAppearance_DeviceDefault);
                }else{
                    mToolbarLayout.setTitleEnabled(false);
                    mToolbarLayout.setTitle("");
                }
            }
        });
    }
}
