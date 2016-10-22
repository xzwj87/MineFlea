package com.github.xzwj87.mineflea.market.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.UserDetailPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.UserDetailView;
import com.github.xzwj87.mineflea.market.ui.activity.UserDetailActivity;
import com.github.xzwj87.mineflea.utils.UserPrefsUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by jason on 10/22/16.
 */

public class UserDetailFragment extends BaseFragment implements UserDetailView{
    public static final String TAG = UserDetailFragment.class.getSimpleName();

    private CollapsingToolbarLayout mToolbarLayout;
    private FloatingActionButton mFab;
    private String mUserId;

    private boolean mIsMe;

    @Inject UserDetailPresenterImpl mPresenter;

    public UserDetailFragment(){}

    public static UserDetailFragment newInstance(String userId){
        UserDetailFragment fragment = new UserDetailFragment();
        Bundle bundle = fragment.getArguments();
        bundle.putString(UserInfo.USER_ID,userId);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedState){
        View root = inflater.inflate(R.layout.fragment_user_detail,container,false);

        ButterKnife.bind(this,root);

        mUserId = getArguments().getString(UserInfo.USER_ID);

        initView();

        return root;
    }

    private void initView(){
        mToolbarLayout = (CollapsingToolbarLayout)getActivity().
                findViewById(R.id.user_detail_toolbar_layout);
        mFab = (FloatingActionButton)getActivity().
                findViewById(R.id.user_detail_fab);

        if(!mUserId.equals(UserPrefsUtil.getString(UserInfo.USER_ID,""))){
            mFab.setImageResource(R.drawable.ic_favorite_white_24dp);
            mIsMe = false;
        }else{
            mFab.setImageResource(R.drawable.ic_edit_white_24dp);
            mIsMe = true;
        }
    }

    @Override
    public void showLoadProcess() {

    }

    @Override
    public void finishView() {
        getActivity().finish();
    }
}
