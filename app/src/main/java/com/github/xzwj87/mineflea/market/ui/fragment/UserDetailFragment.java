package com.github.xzwj87.mineflea.market.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.UserDetailPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.UserDetailView;
import com.github.xzwj87.mineflea.market.ui.activity.UserDetailActivity;
import com.github.xzwj87.mineflea.utils.UserPrefsUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jason on 10/22/16.
 */

public class UserDetailFragment extends BaseFragment implements UserDetailView{
    public static final String TAG = UserDetailFragment.class.getSimpleName();

    private CollapsingToolbarLayout mToolbarLayout;
    private FloatingActionButton mFab;
    private String mUserId;

    private boolean mIsMe;
    private ProgressDialog mProgressDlg;
    @BindView(R.id.tv_email) TextView mEmail;
    @BindView(R.id.tv_tel) TextView mTel;
    @BindView(R.id.tv_followers) TextView mFollowers;
    @BindView(R.id.tv_goods) TextView mGoods;

    @Inject UserDetailPresenterImpl mPresenter;

    public UserDetailFragment(){}

    public static UserDetailFragment newInstance(String userId){
        UserDetailFragment fragment = new UserDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(UserInfo.USER_ID,userId);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedState){
        View root = inflater.inflate(R.layout.fragment_user_detail,container,false);

        ButterKnife.bind(this,root);

        mUserId = getArguments().getString(UserInfo.USER_ID);

        init();
        initView();

        return root;
    }

    @Override
    public void showLoadProcess(boolean stop) {
        if(stop && mProgressDlg.isShowing()){
            mProgressDlg.dismiss();
        }else {
            mProgressDlg = ProgressDialog.show(getActivity(), "",
                    getString(R.string.get_user_detail_progress_hint));
        }
    }

    @Override
    public void showGetUserInfoFail() {
        showToast(getString(R.string.error_get_user_info));
    }

    @Override
    public void showGetGoodsListFail() {
        showToast(getString(R.string.error_get_goods_list));
    }

    @Override
    public void onGetUserInfoComplete(UserInfo userInfo) {
        mToolbarLayout.setTitle(userInfo.getNickName());
        mEmail.setText(userInfo.getUserEmail());
        mTel.setText(userInfo.getUserTelNumber());
        mFollowers.setText(userInfo.getFollowees());
        mGoods.setText(getString(R.string.check_user_goods_list));
    }

    @Override
    public void onGetGoodsListDone(List<PublishGoodsInfo> goodsList) {
        Log.v(TAG,"onGetGoodsListDone(): goods size = " + goodsList.size());
    }

    @Override
    public void finishView() {
        getActivity().finish();
    }

    private void init(){
        mPresenter.init();
        mPresenter.setView(this);

        getUserInfo();
    }

    private void getUserInfo(){
        mPresenter.getUserInfoById(mUserId);
    }

    private void initView(){
        mToolbarLayout = (CollapsingToolbarLayout)getActivity().
                findViewById(R.id.user_detail_toolbar_layout);
        mFab = (FloatingActionButton)getActivity().
                findViewById(R.id.user_detail_fab);

        String userId = UserPrefsUtil.getString(UserInfo.USER_ID,"");
        if(TextUtils.isEmpty(userId)){
            userId = mPresenter.getCurrentUserId();
            if(!TextUtils.isEmpty(userId) && userId.equals(mUserId)) {
                mFab.setImageResource(R.drawable.ic_edit_white_24dp);
                mIsMe = true;
            }else {
                mFab.setImageResource(R.drawable.ic_favorite_white_24dp);
                mIsMe = false;
            }
        }else if(userId.equals(mUserId)){
            mFab.setImageResource(R.drawable.ic_edit_white_24dp);
            mIsMe = true;
        }else{
            mFab.setImageResource(R.drawable.ic_favorite_white_24dp);
            mIsMe = false;
        }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGoodsList();
            }
        });
    }


    private void getGoodsList(){
        mPresenter.getGoodsList(mUserId);
    }
}
