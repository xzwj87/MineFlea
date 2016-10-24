package com.github.xzwj87.mineflea.market.ui.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.internal.di.HasComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.DaggerMarketComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.MarketComponent;
import com.github.xzwj87.mineflea.market.ui.adapter.SectionsPageAdapter;
import com.github.xzwj87.mineflea.market.ui.fragment.UserCenterFragment;

import static com.github.xzwj87.mineflea.market.ui.adapter.SectionsPageAdapter.FRAGMENT_USER_CENTER_TAB;

public class MineFleaHomeActivity extends BaseActivity
            implements HasComponent<MarketComponent>{

    private static final String TAG = MineFleaHomeActivity.class.getSimpleName();

    private MarketComponent mMarketComponent;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPageAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_flea_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initInjector();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.setMarketComponent(mMarketComponent);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        if(mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mine_flea, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.action_publish:
                startPublishActivity();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startPublishActivity(){
        Intent intent = new Intent(this,PublishGoodsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int request,int result, Intent data){
        super.onActivityResult(request,result,data);

        Log.v(TAG,"onActivityResult(): request = " + request);
    }

    @Override
    public MarketComponent getComponent() {
        return mMarketComponent;
    }

    private void initInjector(){
        mMarketComponent =
                DaggerMarketComponent.builder()
                                     .appComponent(getAppComponent())
                                     .activityModule(getActivityModule())
                                     .build();
    }
}
