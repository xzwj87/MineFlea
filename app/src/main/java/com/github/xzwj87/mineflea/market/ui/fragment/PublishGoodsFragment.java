package com.github.xzwj87.mineflea.market.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.data.RepoResponseCode;
import com.github.xzwj87.mineflea.market.internal.di.component.MarketComponent;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.presenter.PublishGoodsPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.PublishGoodsView;
import com.github.xzwj87.mineflea.market.ui.adapter.PublishGoodsImageAdapter;
import com.github.xzwj87.mineflea.utils.StringResUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

/**
 * Created by jason on 10/7/16.
 */

public class PublishGoodsFragment extends BaseFragment
        implements PublishGoodsView,PublishGoodsImageAdapter.ItemClickListener{
    public static final String TAG = PublishGoodsFragment.class.getSimpleName();

    @BindView(R.id.rv_goods_image) RecyclerView mRvGoodsImg;
    @BindView(R.id.et_goods_name) EditText mEtGoodsName;
    @BindView(R.id.et_goods_high_price) EditText mEtHighPrice;
    @BindView(R.id.et_goods_low_price) EditText mEtLowPrice;
    @BindView(R.id.et_note) EditText mEtNote;

    @Inject
    PublishGoodsPresenterImpl mPresenter;
    private ArrayList<String> mFilePath = null;

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
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);

        //setHasOptionsMenu(true);
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

    //TODO: put Model to presenter
    @Override
    public void publishGoods() {
        Log.v(TAG,"publishGoods()");

        PublishGoodsInfo goodsInfo = new PublishGoodsInfo();

        goodsInfo.setName(mEtGoodsName.getText().toString());
        goodsInfo.setPublisher("dummy");
        goodsInfo.setLowerPrice(Double.parseDouble(mEtLowPrice.getText().toString()));
        goodsInfo.setHighPrice(Double.parseDouble(mEtHighPrice.getText().toString()));
        goodsInfo.setImageUri(mFilePath.subList(0,mFilePath.size()-1));

        mPresenter.publishGoods(goodsInfo);
    }

    @Override
    public void onPublishComplete(RepoResponseCode responseCode) {
        Log.v(TAG,"onPublishComplete(): response = " + responseCode);

        if(responseCode.getCode() == RepoResponseCode.RESP_SUCCESS){
            showToast(StringResUtils.getString(R.string.publish_goods_success));
        }else{
            showToast(StringResUtils.getString(R.string.publish_goods_error));
        }
    }

    @Override
    public void finishView() {
        getActivity().finish();
    }

    private void init(){

        getComponent(MarketComponent.class).inject(this);

        mPresenter.setPublishGoodsView(this);
        mPresenter.init();

        mFilePath = new ArrayList<>();
        // add a dummy value
        mFilePath.add(null);

        setupRecycleView();
        addImgToView();
    }

    private void addImgToView(){
        Log.v(TAG,"addImgToView()");

        if(mRvGoodsImg != null){
            PublishGoodsImageAdapter adapter = new PublishGoodsImageAdapter(getContext(),mFilePath);
            adapter.setClickListener(this);

            mRvGoodsImg.setAdapter(adapter);
        }

    }

    private void setupRecycleView(){
        Log.v(TAG,"setupRecycleView()");

        if(mRvGoodsImg != null) {
            GridLayoutManager gridLayoutMgr = new GridLayoutManager(getActivity(), 3);
            mRvGoodsImg.setLayoutManager(gridLayoutMgr);
            mRvGoodsImg.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public void onItemClickListener(int pos) {
        Log.v(TAG,"onItemClickListener(): pos = " + pos);

        FilePickerBuilder.getInstance().setMaxCount(5)
                .setSelectedFiles(mFilePath)
                .setActivityTheme(R.style.AppTheme)
                .pickPhoto(this);
    }

    @Override
    public void onItemLongClickListener(int pos) {
        Log.v(TAG,"onItemLongClickListener(): pos = " + pos);

        FilePickerBuilder.getInstance().setMaxCount(3)
                .setSelectedFiles(mFilePath)
                .setActivityTheme(R.style.AppTheme)
                .pickPhoto(this);
    }

    @Override
    public void onButtonRemoveClickListener(int pos) {
        Log.v(TAG,"onButtonRemoveClickListener(): pos = " + pos);

        mFilePath.remove(pos);

        addImgToView();
    }

    @Override
    public void onActivityResult(int request, int result, Intent data){
        Log.v(TAG,"onActivityResult(): result = " + result);
        switch (request){
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if(result == Activity.RESULT_OK && data != null){
                    ArrayList<String> copy = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);
                    for(int idx = 0; idx < copy.size(); ++idx) {
                        if(!mFilePath.contains(copy.get(idx))) {
                            mFilePath.add(0, copy.get(idx));
                        }
                    }
                    Log.v(TAG,mFilePath.size()-1 + " pictures picked");
                }

                addImgToView();

                break;
            default:
                break;
        }
    }

}
