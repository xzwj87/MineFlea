package com.github.xzwj87.mineflea.market.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.xzwj87.mineflea.market.ui.fragment.DiscoverTabFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.NearbyTabFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.TabHolderFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.UserCenterFragment;
import com.github.xzwj87.mineflea.utils.StringResUtils;

/**
 * Created by JasonWang on 2016/9/19.
 */
public class SectionsPageAdapter extends FragmentPagerAdapter{

    public static final int NUMBER_OF_TABS = 3;

    public static final int FRAGMENT_DISCOVER_TAB = 0;
    public static final int FRAGMENT_NEARBY_TAB = 1;
    public static final int FRAGMENT_SETTING_TAB = 2;


    public SectionsPageAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position){
            case FRAGMENT_DISCOVER_TAB:
                return DiscoverTabFragment.newInstance();
            case FRAGMENT_NEARBY_TAB:
                return NearbyTabFragment.newInstance();
            case FRAGMENT_SETTING_TAB:
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
            case FRAGMENT_DISCOVER_TAB:
                return StringResUtils.getTabName(0);
            case FRAGMENT_NEARBY_TAB:
                return StringResUtils.getTabName(1);
            case FRAGMENT_SETTING_TAB:
                return StringResUtils.getTabName(2);
        }

        return null;
    }
}
