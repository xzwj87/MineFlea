package com.github.xzwj87.mineflea.market.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.xzwj87.mineflea.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jason on 10/9/16.
 */

public class UserCenterFragment extends BaseFragment{
    public static final String TAG = UserCenterFragment.class.getSimpleName();

    @BindView(R.id.civ_user_header) CircleImageView mCivHeader;

    public UserCenterFragment(){}

    public static UserCenterFragment newInstance(){
        UserCenterFragment fragment = new UserCenterFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedSate){
        View root = inflater.inflate(R.layout.fragment_user_center,container,false);

        ButterKnife.bind(this,root);

        return root;
    }
}
