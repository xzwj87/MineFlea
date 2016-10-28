package com.github.xzwj87.mineflea.market.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.xzwj87.mineflea.R;

/**
 * Created by jason on 10/26/16.
 */

public class UserFolloweeFragment extends BaseFragment {

    public UserFolloweeFragment(){}

    public static UserFolloweeFragment newInstance(){
        return new UserFolloweeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){
        View root = inflater.inflate(R.layout.fragment_user_followee,container,false);

        return  root;
    }
}
