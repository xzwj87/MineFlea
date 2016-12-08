package com.github.xzwj87.mineflea.market.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.model.LatLng;
import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.presenter.DiscoverGoodsPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.DiscoverGoodsView;
import com.github.xzwj87.mineflea.market.ui.activity.GoodsDetailActivity;
import com.github.xzwj87.mineflea.market.ui.adapter.DiscoverGoodsAdapter;
import com.github.xzwj87.mineflea.utils.SharePrefsHelper;
import com.github.xzwj87.mineflea.utils.UserPrefsUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jason on 10/9/16.
 */

public class DiscoverTabFragment extends BaseFragment
            implements DiscoverGoodsAdapter.DiscoverGoodsCallback,DiscoverGoodsView {
    public static final String TAG = DiscoverTabFragment.class.getSimpleName();

    private DiscoverGoodsAdapter mRvAdapter;
    @Inject DiscoverGoodsPresenterImpl mPresenter;

    @BindView(R.id.discover_recycler_view)
    RecyclerView mRvDiscover;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSrlDiscover;

    private AMapLocationClient mLocClient;

    public DiscoverTabFragment() {
    }

    public static DiscoverTabFragment newInstance() {
        DiscoverTabFragment fragment = new DiscoverTabFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedSate) {
        View root = inflater.inflate(R.layout.fragment_discover_tab, container, false);
        ButterKnife.bind(this, root);

        init();

        return root;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        Log.v(TAG,"onAttach()");
    }

    @Override
    public void onResume(){
        super.onResume();

        Log.v(TAG,"onResume()");

        mPresenter.setView(this);
        mPresenter.init();

        setUpAmapLocationClient();
    }

    @Override
    public void onPause(){
        super.onPause();

        mLocClient.stopLocation();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo info){
        super.onCreateContextMenu(menu,v,info);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){

        return super.onContextItemSelected(item);
    }

    private void init() {
        mRvAdapter = new DiscoverGoodsAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(AppGlobals.getAppContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvDiscover.setLayoutManager(layoutManager);
        setSwipeLayout();

        mRvAdapter.setCallback(this);
        mRvDiscover.setAdapter(mRvAdapter);
        // current location

        LatLng loc = UserPrefsUtil.getCurrentLocation();
        if(loc != null) {
            mRvAdapter.setCurrentLoc(loc);
        }
    }

    //设置下拉刷新
    private void setSwipeLayout() {
        String themeColor = SharePrefsHelper.getInstance(getContext())
                .getThemeColor();
        mSrlDiscover.setColorSchemeColors(Color.parseColor(themeColor));
        mSrlDiscover.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getGoodsList();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(AppGlobals.getAppContext(), "显示", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View item,int pos) {

    }

    @Override
    public PublishGoodsInfo getItemAtPos(int pos) {
        return mPresenter.getItemAtPos(pos);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getItemCount();
    }

    @Override
    public String getPublisherHeadIcon(int pos) {
        return mPresenter.getPublisherHeadIcon(pos);
    }

    @Override
    public String getPublisherNickName(int pos) {
        return mPresenter.getPublisherNickName(pos);
    }

    @Override
    public void onGetGoodsListDone(boolean success) {
        Log.v(TAG,"onGetGoodsListDone(): " + (success ? "success" : "fail"));
        if(mSrlDiscover.isRefreshing()){
            mSrlDiscover.setRefreshing(false);

            if(success){
                showToast(getString(R.string.get_goods_list_sucess));
            }else{
                showToast(getString(R.string.error_get_goods_list));
            }
        }
    }

    @Override
    public void finishView() {
        // empty
    }

    private void setUpAmapLocationClient(){
        mLocClient = new AMapLocationClient(getActivity());
        AMapLocationClientOption locOptions = new AMapLocationClientOption();
        locOptions.setNeedAddress(true);
        locOptions.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locOptions.setHttpTimeOut(10*1000);
        locOptions.setInterval(50*1000);
        mLocClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation != null){
                    if(aMapLocation.getErrorCode() == 0){
                        if(mRvAdapter != null){
                            mRvAdapter.setCurrentLoc(new LatLng(aMapLocation.getLatitude(),
                                    aMapLocation.getLongitude()));
                            // save current location
                            LatLng loc = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                            UserPrefsUtil.updateCurrentLocation(loc);
                        }
                    }else{
                        Log.e("AmapError","location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        });

        mLocClient.startLocation();
    }
}
