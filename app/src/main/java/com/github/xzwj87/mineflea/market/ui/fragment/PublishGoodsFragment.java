package com.github.xzwj87.mineflea.market.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.internal.di.component.MarketComponent;
import com.github.xzwj87.mineflea.market.presenter.PublishGoodsPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.PublishGoodsView;
import com.github.xzwj87.mineflea.market.ui.adapter.PublishGoodsImageAdapter;

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

    @BindView(R.id.rv_goods_image) RecyclerView mRvGoodsImg;
    @BindView(R.id.et_goods_name) EditText mEtGoodsName;
    @BindView(R.id.et_goods_high_price) EditText mEtHighPrice;
    @BindView(R.id.et_goods_low_price) EditText mEtLowPrice;
    @BindView(R.id.et_note) EditText mEtNote;

    @Inject
    PublishGoodsPresenterImpl mPresenter;
    private ArrayList<String> mFilePath;
    private PublishGoodsImageAdapter mGoodsImgAdapter;

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
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }


    @Override
    public void onPause(){
        super.onPause();

        mPresenter.onPause();
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

        init();

        return rootView;

    }

    @Override
    public void publishGoods() {
        Log.v(TAG,"publishGoods()");

        mPresenter.setGoodsName(mEtGoodsName.getText().toString());
        mPresenter.setGoodsLowPrice(Double.parseDouble(mEtLowPrice.getText().toString()));
        mPresenter.setGoodsHighPrice(Double.parseDouble(mEtHighPrice.getText().toString()));
        mPresenter.setGoodsNote(mEtNote.getText().toString());
        mPresenter.setGoodsImgUrl(mFilePath.subList(0,mFilePath.size()-1));

        mPresenter.publishGoods();
    }

    @Override
    public void onPublishComplete(boolean success) {
        Log.v(TAG,"onPublishComplete(): " + (success ? "success" : "failure"));

        // Todo: save those failed info to "draft box"
        if(!success){
            showToast(getString(R.string.error_publish_fail));
        }
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
/*            PublishGoodsImageAdapter adapter = new PublishGoodsImageAdapter(getContext(),mFilePath);
            adapter.setClickListener(this);

            mRvGoodsImg.setAdapter(adapter);*/

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

}
