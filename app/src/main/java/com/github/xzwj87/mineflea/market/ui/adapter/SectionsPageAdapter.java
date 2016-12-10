package com.github.xzwj87.mineflea.market.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amap.api.maps2d.model.LatLng;
import com.github.xzwj87.mineflea.market.internal.di.component.MarketComponent;
import com.github.xzwj87.mineflea.market.ui.fragment.DiscoverTabFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.NearbyTabFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.TabHolderFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.UserCenterFragment;
import com.github.xzwj87.mineflea.utils.StringResUtils;

/**
 * Created by JasonWang on 2016/9/19.
 */
public class SectionsPageAdapter extends FragmentPagerAdapter {

    private MarketComponent mComponent;

    public static final int NUMBER_OF_TABS = 3;

    public static final int FRAGMENT_DISCOVER_TAB = 0;
    public static final int FRAGMENT_NEARBY_TAB = 1;
    public static final int FRAGMENT_USER_CENTER_TAB = 2;

    private DiscoverTabFragment fragment1;
    private NearbyTabFragment fragment2;
    private UserCenterFragment fragment3;

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setMarketComponent(MarketComponent component) {
        mComponent = component;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case FRAGMENT_DISCOVER_TAB:
                fragment1 = DiscoverTabFragment.newInstance();
                if (mComponent != null) {
                    mComponent.inject(fragment1);
                }
                return fragment1;
            case FRAGMENT_NEARBY_TAB:
                fragment2 = NearbyTabFragment.newInstance();
                if (mComponent != null) {
                    mComponent.inject(fragment2);
                }
                return fragment2;
            case FRAGMENT_USER_CENTER_TAB:
                fragment3 = UserCenterFragment.newInstance();
                if (mComponent != null) {
                    mComponent.inject(fragment3);
                }
                return fragment3;
        }

        return TabHolderFragment.newInstance(position + 1);
    }

    public LatLng getLocation() {
        if (fragment2 != null) {
            LatLng loc = fragment2.getMyLocation();
            if (loc != null) {
                return loc;
            }
        }
        return null;
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
            case FRAGMENT_USER_CENTER_TAB:
                return StringResUtils.getTabName(2);
        }

        return null;
    }
}
