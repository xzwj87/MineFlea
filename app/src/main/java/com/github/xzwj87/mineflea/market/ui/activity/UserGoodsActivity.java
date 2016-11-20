package com.github.xzwj87.mineflea.market.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.ui.fragment.BaseFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.UserFavoritesFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.UserPublishedGoodsFragment;
import com.github.xzwj87.mineflea.utils.ThemeColorUtils;

/**
 * Created by jason on 11/1/16.
 */

public class UserGoodsActivity extends BaseActivity{
    private static final String TAG = UserGoodsActivity.class.getSimpleName();

    private String mFragmentTag;
    private String mUserId;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);

        setContentView(R.layout.activity_user_goods);

        Intent intent = getIntent();
        mFragmentTag = intent.getStringExtra("FragmentTag");
        mUserId = intent.getStringExtra("UserId");

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);
        }

        // new fragment
        FragmentManager fragmentMgr = getSupportFragmentManager();
        BaseFragment fragment = (BaseFragment)fragmentMgr.findFragmentByTag(mFragmentTag);
        if(fragment == null && mFragmentTag != null){
            if(mFragmentTag.equals(UserPublishedGoodsFragment.TAG)){
                fragment = UserPublishedGoodsFragment.newInstance(mUserId);
                if(actionBar != null){
                    actionBar.setTitle(R.string.published_goods);
                }
                //init injector
                getComponent().inject((UserPublishedGoodsFragment)fragment);
            }else if(mFragmentTag.equals(UserFavoritesFragment.TAG)){
                fragment = UserFavoritesFragment.newInstance(mUserId);
                getComponent().inject((UserFavoritesFragment)fragment);
                if(actionBar != null){
                    actionBar.setTitle(R.string.my_favorites);
                }
            }
        }

        fragmentMgr.beginTransaction()
                   .add(fragment,mFragmentTag)
                   .commit();

        ThemeColorUtils.changeThemeColor(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
