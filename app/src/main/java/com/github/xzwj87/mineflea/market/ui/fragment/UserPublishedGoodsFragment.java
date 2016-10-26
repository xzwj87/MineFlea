package com.github.xzwj87.mineflea.market.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.xzwj87.mineflea.R;

/**
 * Created by jason on 10/26/16.
 */

public class UserPublishedGoodsFragment extends BaseFragment{

    public UserPublishedGoodsFragment(){
    }

    public static UserPublishedGoodsFragment newInstance(){
        return new UserPublishedGoodsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){
        View root = inflater.inflate(R.layout.fragment_publish_goods,container,false);

        return root;
    }
}
