package com.github.xzwj87.mineflea.market.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.ui.activity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jason on 10/9/16.
 */

public class UserCenterFragment extends BaseFragment{
    public static final String TAG = UserCenterFragment.class.getSimpleName();

    public static final int REQUEST_LOGIN = 1;
    @BindView(R.id.civ_user_header) CircleImageView mCivHeader;
    @BindView(R.id.header_container) LinearLayout mHeaderLayout;

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

    @OnClick(R.id.header_container)
    public void login(){
        Log.v(TAG,"login()");

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent,REQUEST_LOGIN);
    }

    @Override
    public void onActivityResult(int request, int result, Intent data){
        Log.v(TAG,"onActivityResult(): result = " + result);


    }
}
