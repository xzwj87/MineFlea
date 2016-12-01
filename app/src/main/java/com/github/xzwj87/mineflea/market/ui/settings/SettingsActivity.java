package com.github.xzwj87.mineflea.market.ui.settings;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.ui.activity.BaseActivity;
import com.github.xzwj87.mineflea.utils.ThemeColorUtils;

public class SettingsActivity extends BaseActivity {
    public static final String LOG_TAG = "SettingsActivity";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate()");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);
            actionBar.setTitle(getResources().getString(R.string.settings));
        }


        if(savedInstanceState == null) {
            SettingsFragment fragment = new SettingsFragment();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commit();

            getComponent().inject(fragment);
        }

        ThemeColorUtils.changeThemeColor(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Log.v(LOG_TAG,"onOptionsItemSelected()");

        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
