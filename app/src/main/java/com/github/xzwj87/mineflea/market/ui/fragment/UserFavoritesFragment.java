package com.github.xzwj87.mineflea.market.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.model.UserGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.UserFavorGoodsPresenter;
import com.github.xzwj87.mineflea.market.presenter.UserFavorGoodsPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.UserFavoriteView;
import com.github.xzwj87.mineflea.market.ui.adapter.UserGoodsAdapter;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by jason on 10/26/16.
 */

public class UserFavoritesFragment  extends BaseFragment
            implements UserGoodsAdapter.PublishedGoodsCallback,UserFavoriteView{

    @Inject UserFavorGoodsPresenterImpl mPresenter;
    private UserGoodsAdapter mAdapter;
    private RecyclerView mRvGoodsList;
    private SwipeRefreshLayout mSrLayout;

    private String mUserId;

    public UserFavoritesFragment(){}

    public static UserFavoritesFragment newInstance(String userId){
        UserFavoritesFragment fragment = new UserFavoritesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(UserInfo.USER_ID,userId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){
        View rootView = inflater.inflate(R.layout.fragment_user_favorites,container,false);

        mRvGoodsList = (RecyclerView)rootView.findViewById(R.id.rv_container);
        mSrLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.srl_container);

        return rootView;
    }

    @Override
    public void onStart(){
        init();
    }

    @Override
    public void onPause(){
        mPresenter.onPause();
    }

    @Override
    public void onDestroy(){
        mPresenter.onDestroy();
        mAdapter = null;
    }

    @Override
    public int getGoodsCount() {
        return mPresenter.getGoodsCount();
    }

    @Override
    public UserGoodsInfo getGoodsAtPos(int pos) {
        return mPresenter.getGoodsAtPos(pos);
    }

    @Override
    public void showProgress(boolean show) {
        if(!show && mSrLayout.isRefreshing()){
            mSrLayout.setRefreshing(false);
        }
    }

    @Override
    public void showBlankPage() {

    }

    @Override
    public void renderView() {
        mAdapter = new UserGoodsAdapter();
        mAdapter.setCallback(this);
        mRvGoodsList.setAdapter(mAdapter);
    }

    private void init(){
        mPresenter.init();
        mPresenter.setView(this);

        loadGoodsList();
    }

    @Override
    public void finishView() {
        // should not called
    }

    private void loadGoodsList(){
        mUserId = getArguments().getString(UserInfo.USER_ID);
        mPresenter.getFavorGoodsList(mUserId);
    }
}
