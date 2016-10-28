package com.github.xzwj87.mineflea.market.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.xzwj87.mineflea.R;

/**
 * Created by jason on 10/26/16.
 */

public class UserFavoritesFragment  extends BaseFragment{

    public UserFavoritesFragment(){}

    public static UserFavoritesFragment newInstance(){
        return new UserFavoritesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){
        View rootView = inflater.inflate(R.layout.fragment_user_favorites,container,false);

        return rootView;
    }
}
