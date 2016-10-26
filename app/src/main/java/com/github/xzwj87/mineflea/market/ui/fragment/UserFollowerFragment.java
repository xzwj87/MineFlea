package com.github.xzwj87.mineflea.market.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.xzwj87.mineflea.R;

/**
 * Created by jason on 10/26/16.
 */

public class UserFollowerFragment extends BaseFragment{

    public UserFollowerFragment(){}

    public static UserFollowerFragment newInstance(){
        return new UserFollowerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){
        View root = inflater.inflate(R.layout.fragment_user_follower,container,false);

        return root;
    }
}
