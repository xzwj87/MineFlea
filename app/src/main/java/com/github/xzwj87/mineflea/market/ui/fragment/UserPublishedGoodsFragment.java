package com.github.xzwj87.mineflea.market.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.model.UserGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.UserGoodsPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.UserPublishedGoodsView;
import com.github.xzwj87.mineflea.market.ui.adapter.UserGoodsAdapter;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by jason on 10/26/16.
 */

public class UserPublishedGoodsFragment extends BaseFragment
                implements UserGoodsAdapter.PublishedGoodsCallback,UserPublishedGoodsView{

    @Inject UserGoodsPresenterImpl mPresenter;
    private UserGoodsAdapter mAdapter;
    private RecyclerView mRvGoodsList;
    private SwipeRefreshLayout mSrLayout;

    private String mUserId;

    public UserPublishedGoodsFragment(){ }

    public static UserPublishedGoodsFragment newInstance(String id){
        UserPublishedGoodsFragment fragment = new UserPublishedGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(UserInfo.USER_ID,id);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){
        View root = inflater.inflate(R.layout.fragment_user_published_goods,container,false);

        ButterKnife.bind(this,root);

        mSrLayout = (SwipeRefreshLayout)root.findViewById(R.id.srl_container);

        mRvGoodsList = (RecyclerView)root.findViewById(R.id.rv_container);
        LinearLayoutManager layoutMgr = new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL,false);
        mRvGoodsList.setLayoutManager(layoutMgr);
        mRvGoodsList.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new UserGoodsAdapter();

        init();

        return root;
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
        if(mSrLayout.isRefreshing()){
            mSrLayout.setRefreshing(false);
        }
    }

    @Override
    public void renderView() {
        mRvGoodsList.setAdapter(mAdapter);
        mAdapter.setCallback(this);
    }

    @Override
    public void finishView() {
        // should not be called
    }

    private void init(){
        mPresenter.init();
        mPresenter.setView(this);

        mUserId = (String)getArguments().get(UserInfo.USER_ID);
        mPresenter.getGoodsListByUserId(mUserId);
    }
}
