package com.github.xzwj87.mineflea.market.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.tv_user_action) TextView mTvAction;

    private String mUserId;
    private int mInflateMenuId;

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
    public void onGetUserInfoDone(boolean success) {
        if(!success){
            showToast(getString(R.string.error_get_user_info));
        }
    }

    @Override
    public void renderHeadIcon(String iconUrl) {
        if(!TextUtils.isEmpty(iconUrl)) {
            Picasso.with(this)
                    .load(iconUrl)
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
                break;
            case R.id.favorite:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getUserInfo(){
        mPresenter.getUserInfoById(mUserId);
    }

    private void initView(){

        mInflateMenuId = R.menu.menu_user_detail_others;

        if(mPresenter != null){
            if(mPresenter.isMe(mUserId)){
                mInflateMenuId = R.menu.menu_user_detail_me;
                mTvAction.setText(R.string.action_edit);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mTvAction.setCompoundDrawables(getDrawable(R.drawable.ic_edit_white_24dp),
                            null, null, null);
                }else{
                    mTvAction.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_edit_white_24dp),
                            null, null, null);
                }
            }
        }
    }
}
