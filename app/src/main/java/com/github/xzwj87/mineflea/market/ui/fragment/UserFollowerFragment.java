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
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.model.UserFollowInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.UserFollowerPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.UserFollowView;
import com.github.xzwj87.mineflea.market.ui.adapter.UserFollowAdapter;
import com.github.xzwj87.mineflea.market.ui.adapter.UserGoodsAdapter;

import javax.inject.Inject;

/**
 * Created by jason on 10/26/16.
 */

public class UserFollowerFragment extends BaseFragment
        implements UserFollowView,UserFollowAdapter.UserFollowCallback{

    @Inject UserFollowerPresenterImpl mPresenter;
    private RecyclerView mRvUserList;
    private SwipeRefreshLayout mSrLayout;
    private UserFollowAdapter mAdapter;

    public UserFollowerFragment(){}

    public static UserFollowerFragment newInstance(String userId){
        UserFollowerFragment fragment = new UserFollowerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(UserInfo.USER_ID,userId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){
        View root = inflater.inflate(R.layout.fragment_user_follower,container,false);

        mSrLayout = (SwipeRefreshLayout)root.findViewById(R.id.srl_container);
        mRvUserList = (RecyclerView)root.findViewById(R.id.rv_container);

        setupRecycleView();

        return root;
    }

    @Override
    public void onStart(){
        super.onStart();

        init();
    }

    @Override
    public void showProgress(boolean show) {
        if(mSrLayout.isRefreshing()){
            mSrLayout.setRefreshing(false);
        }
    }

    @Override
    public void showBlankPage() {
        mSrLayout.removeAllViewsInLayout();
        if(getActivity() != null) {
            View blank = View.inflate(getActivity(), R.layout.fragment_blank_hint, null);
            mSrLayout.addView(blank);
        }else{
            View blank = View.inflate(AppGlobals.getAppContext(), R.layout.fragment_blank_hint, null);
            mSrLayout.addView(blank);
        }
    }

    @Override
    public void renderView() {
        mRvUserList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void setupRecycleView(){
        LinearLayoutManager layoutMgr = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL,false);
        mRvUserList.setLayoutManager(layoutMgr);
        mRvUserList.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new UserFollowAdapter();
        mAdapter.setCallback(this);
    }

    private void init(){
        mPresenter.init();
        mPresenter.setView(this);
    }

    @Override
    public void finishView() {
        // empty
    }

    @Override
    public int getUserFollowCount() {
        return mPresenter.getUserFollowCount();
    }

    @Override
    public UserFollowInfo getUserFollowAtPos(int pos) {
        return mPresenter.getUserFollowAtPos(pos);
    }
}
