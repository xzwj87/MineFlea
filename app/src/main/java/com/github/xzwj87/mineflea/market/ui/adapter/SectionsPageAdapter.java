package com.github.xzwj87.mineflea.market.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.xzwj87.mineflea.market.ui.fragment.TabHolderFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.UserCenterFragment;
import com.github.xzwj87.mineflea.utils.StringResUtils;

/**
 * Created by JasonWang on 2016/9/19.
 */
public class SectionsPageAdapter extends FragmentPagerAdapter{

    public static final int NUMBER_OF_TABS = 3;

    public SectionsPageAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment;
        switch (position){
            case 0:
                break;
            case 1:
                break;
            case 2:
                return UserCenterFragment.newInstance();
        }

        return TabHolderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return NUMBER_OF_TABS;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return StringResUtils.getTabName(0);
            case 1:
                return StringResUtils.getTabName(1);
            case 2:
                return StringResUtils.getTabName(2);
        }

        return null;
    }
}
