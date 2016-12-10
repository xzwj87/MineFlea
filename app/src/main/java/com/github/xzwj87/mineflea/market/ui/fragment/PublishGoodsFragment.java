package com.github.xzwj87.mineflea.market.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.internal.di.component.MarketComponent;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.PublishGoodsPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.PublishGoodsView;
import com.github.xzwj87.mineflea.market.ui.activity.HomeActivity;
import com.github.xzwj87.mineflea.market.ui.activity.PublishGoodsActivity;
import com.github.xzwj87.mineflea.market.ui.adapter.PublishGoodsImageAdapter;
import com.github.xzwj87.mineflea.utils.UserPrefsUtil;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by jason on 10/7/16.
 */

public class PublishGoodsFragment extends BaseFragment
        implements PublishGoodsView,PublishGoodsImageAdapter.ItemClickListener{
    public static final String TAG = PublishGoodsFragment.class.getSimpleName();

    private static final int IMG_COL_NUMBER = 3;
    private static final int MAX_NUM_PICTURES = 5;

    @BindView(R.id.et_note) EditText mEtNote;
    @BindView(R.id.rv_goods_image) RecyclerView mRvGoodsImg;

    ProgressDialog mProcessBar;

    CollapsingToolbarLayout mCollapsingToolbar;
    EditText mEtGoodsName;
    EditText mEtPrice;

    @Inject PublishGoodsPresenterImpl mPresenter;
    private ArrayList<String> mFilePath;
    private PublishGoodsImageAdapter mGoodsImgAdapter;
    // get location
    private AMapLocationClient mLocClient;

    public PublishGoodsFragment(){}

    public static PublishGoodsFragment newInstance(){
        PublishGoodsFragment fragment = new PublishGoodsFragment();

        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.menu_publish_goods,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();

        switch (id){
            case R.id.action_publish:
                publishGoods();
                return true;
            case android.R.id.home:
                Intent intent = new Intent(getActivity(),HomeActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        setUpAmapLocationClient();
    }

    @Override
    public void onPause(){
        super.onPause();

        mPresenter.onPause();
        mLocClient.stopLocation();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        mPresenter.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedState){
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_publish_goods,container,false);
        ButterKnife.bind(this,rootView);

        initView();

        init();

        return rootView;

    }

    @Override
    public void publishGoods() {
        Log.v(TAG,"publishGoods()");

        mPresenter.setGoodsName(mEtGoodsName.getText().toString());
        mPresenter.setGoodsPrice(mEtPrice.getText().toString());
        mPresenter.setGoodsNote(mEtNote.getText().toString());
        mPresenter.setGoodsImgUrl(mFilePath.subList(0,mFilePath.size()-1));
        mPresenter.setPublisherName(UserPrefsUtil.getString(UserInfo.USER_NAME,"dummy"));
        mPresenter.setLocation(((PublishGoodsActivity)getActivity()).getMyLoc());
        UserPrefsUtil.updateCurrentLocation(((PublishGoodsActivity)getActivity()).getMyLoc());

        if(mPresenter.validGoodsInfo()) {
            mPresenter.publishGoods();

            mProcessBar = ProgressDialog.show(getContext(),"",getString(R.string.progress_publish_goods));
        }
    }

    @Override
    public void onPublishComplete(boolean success) {
        Log.v(TAG,"onPublishComplete(): " + (success ? "success" : "failure"));

        // Todo: save those failed info to "draft box"
        if(getActivity() != null) {
            if (!success) {
                showToast(getString(R.string.publish_goods_error));
            } else {
                showToast(getString(R.string.publish_goods_success));
            }

            if(mProcessBar.isShowing()){
                mProcessBar.dismiss();
            }
        }
    }

    @Override
    public void updateUploadProcess(int count) {
        Log.v(TAG,"updateUploadProcess(): count = " + count);
        //mProcessBar.setProgress(count);
    }

    @Override
    public void showNameInvalidMsg() {
        mEtGoodsName.setError(getString(R.string.error_field_required));
        //showToast(getString(R.string.error_invalid_goods_name));
    }

    @Override
    public void showPriceInvalidMsg() {
        mEtPrice.setError(getString(R.string.error_field_required));
        //showToast(getString(R.string.error_invalid_goods_price));
    }

    @Override
    public void showNoteInvalidMsg() {
        mEtNote.setError(getString(R.string.error_field_required));
        showToast(getString(R.string.error_no_note));
    }

    @Override
    public void showNoPicturesMsg() {
        showToast(getString(R.string.error_no_pictures));
    }

    @Override
    public void showNoNetConnectionMsg() {
        Log.v(TAG,"showNoNetConnectionMsg()");
        showToast(getString(R.string.hint_no_network_connection));
    }

    @Override
    public void showNeedLoginMsg() {
        Log.v(TAG,"showNeedLoginMsg()");
        showToast(getString(R.string.need_to_login));
    }

    @Override
    public void finishView() {
        getActivity().finish();
    }

    private void init(){

        getComponent(MarketComponent.class).inject(this);

        mPresenter.setView(this);
        mPresenter.init();

        mFilePath = new ArrayList<>();
        mFilePath.add(null);

        setupRecycleView();
        addImgToView();
    }

    private void addImgToView(){
        Log.v(TAG,"addImgToView()");

        if(mRvGoodsImg != null){
            mGoodsImgAdapter = new PublishGoodsImageAdapter(getContext(),mFilePath);
            mGoodsImgAdapter.setClickListener(this);

            mRvGoodsImg.setAdapter(mGoodsImgAdapter);
        }

    }

    private void setupRecycleView(){
        Log.v(TAG,"setupRecycleView()");

        if(mRvGoodsImg != null) {
            StaggeredGridLayoutManager gridLayoutMgr = new
                    StaggeredGridLayoutManager(IMG_COL_NUMBER, OrientationHelper.VERTICAL);
            mRvGoodsImg.setLayoutManager(gridLayoutMgr);
            mRvGoodsImg.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public void onItemClickListener(int pos) {
        Log.v(TAG,"onItemClickListener(): pos = " + pos);

        if(mFilePath.size() < (MAX_NUM_PICTURES+1)) {
            PhotoPicker.builder()
                    .setPhotoCount(MAX_NUM_PICTURES)
                    .setPreviewEnabled(true)
                    .setShowGif(true)
                    .setShowCamera(true)
                    .start(getActivity(), this, PhotoPicker.REQUEST_CODE);
        }else{
            showToast(getString(R.string.pictures_over_max_count_hints));
        }
    }

    @Override
    public void onItemLongClickListener(int pos) {
        Log.v(TAG,"onItemLongClickListener(): pos = " + pos);

        if(mFilePath.size() < (MAX_NUM_PICTURES+1)) {
            PhotoPicker.builder()
                    .setPhotoCount(MAX_NUM_PICTURES)
                    .setPreviewEnabled(true)
                    .setShowGif(true)
                    .setShowCamera(true)
                    .start(getActivity(), this, PhotoPicker.REQUEST_CODE);
        }else{
            showToast(getString(R.string.__picker_over_max_count_tips));
        }
    }

    @Override
    public void onButtonRemoveClickListener(int pos) {
        Log.v(TAG,"onButtonRemoveClickListener(): pos = " + pos);

        mFilePath.remove(pos);

        //addImgToView();
        if(mGoodsImgAdapter != null){
            mGoodsImgAdapter.notifyDataSetChanged();
        }else {
            addImgToView();
        }
    }

    @Override
    public void onActivityResult(int request, int result, Intent data){
        Log.v(TAG,"onActivityResult(): result = " + result);
        switch (request){
            case PhotoPicker.REQUEST_CODE:
                if(result == Activity.RESULT_OK && data != null){
                    ArrayList<String> copy = data.
                            getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    for(int idx = 0; idx < copy.size(); ++idx) {
                        if(!mFilePath.contains(copy.get(idx))) {
                            mFilePath.add(0, copy.get(idx));
                        }
                    }
                    Log.v(TAG,mFilePath.size()-1 + " pictures picked");

                    //addImgToView();
                    if(mGoodsImgAdapter != null){
                        mGoodsImgAdapter.notifyDataSetChanged();
                    }else {
                        addImgToView();
                    }
                }

                break;

            default:
                break;
        }
    }

    private void initView(){
        mCollapsingToolbar = (CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar);
        mEtGoodsName = (EditText) mCollapsingToolbar.findViewById(R.id.et_goods_name);
        mEtPrice = (EditText)mCollapsingToolbar.findViewById(R.id.et_goods_price);
    }

    private void setUpAmapLocationClient(){
        mLocClient = new AMapLocationClient(getActivity());
        AMapLocationClientOption locOptions = new AMapLocationClientOption();
        locOptions.setNeedAddress(true);
        locOptions.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locOptions.setHttpTimeOut(10*1000);
        locOptions.setInterval(2*1000);
        mLocClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation != null){
                    if(aMapLocation.getErrorCode() == 0){
                        //mPresenter.setLocation(new LatLng(aMapLocation.getLatitude(),
                                //aMapLocation.getLongitude()));
                        // save current location
                        //LatLng current = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                        //
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
