package com.github.xzwj87.mineflea.market.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.ui.fragment.UserFavoritesFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.UserFolloweeFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.UserFollowerFragment;
import com.github.xzwj87.mineflea.market.ui.fragment.UserPublishedGoodsFragment;

/**
 * Created by jason on 10/26/16.
 */

public class UserDetailPageAdapter extends FragmentPagerAdapter{
    private static final String TAG = UserDetailPageAdapter.class.getSimpleName();

    private Context mContext;
    private TextView mTvCount;
    private String[] mTabNames;
    private String mUserId;

    private static final int MA_TABS = 4;

    private static final int TAB_PUBLISHED = 0;
    private static final int TAB_FAVORITE = 1;
    private static final int TAB_FOLLOWER = 2;
    private static final int TAB_FOLLOWEE = 3;

    public UserDetailPageAdapter(String userId,Context context, FragmentManager fm){
        super(fm);

        mUserId = userId;
        mContext = context;
        mTabNames = mContext.getResources().getStringArray(R.array.user_detail_tab_name);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case TAB_PUBLISHED:
                return UserPublishedGoodsFragment.newInstance(mUserId);
            case TAB_FAVORITE:
                return UserFavoritesFragment.newInstance();
            case TAB_FOLLOWER:
                return UserFollowerFragment.newInstance();
            case TAB_FOLLOWEE:
                return UserFolloweeFragment.newInstance();
        }

        return null;
    }

    @Override
    public int getCount() {
        return MA_TABS;
    }

    @Override
    public CharSequence getPageTitle(int pos){
        return null;
    }


    public View getTabView(int pos){
        View root = LayoutInflater.from(mContext)
                .inflate(R.layout.tab_item_layout,null);

        mTvCount = (TextView)root.findViewById(R.id.tv_count);
        mTvCount.setText(String.valueOf(0));

        TextView tvName = (TextView)root.findViewById(R.id.tv_tab_name);
        tvName.setText(mTabNames[pos]);

        return root;
    }
}
