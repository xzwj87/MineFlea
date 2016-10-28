package com.github.xzwj87.mineflea.market.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.model.UserInfo;

import butterknife.ButterKnife;

/**
 * Created by jason on 10/26/16.
 */

public class UserPublishedGoodsFragment extends BaseFragment{

    public UserPublishedGoodsFragment(){
    }

    public static UserPublishedGoodsFragment newInstance(String id){
        UserPublishedGoodsFragment fragment = new UserPublishedGoodsFragment();
        Bundle bundle = fragment.getArguments();
        bundle.putString(UserInfo.USER_ID,id);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){
        View root = inflater.inflate(R.layout.fragment_publish_goods,container,false);

        ButterKnife.bind(this,root);



        return root;
    }
}
