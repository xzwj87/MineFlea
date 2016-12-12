package com.github.xzwj87.mineflea.market.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.presenter.LogoutPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.dialog.ThemeColorPickerPreference;
import com.github.xzwj87.mineflea.utils.SharePrefsHelper;
import com.github.xzwj87.mineflea.utils.ThemeColorUtils;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by JasonWang on 2016/4/22.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String LOG_TAG = "SettingsFragment";

    @Inject LogoutPresenterImpl mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate()");

        addPreferencesFromResource(R.xml.settings);

        initPref();
    }

    @Override
    public void onResume(){
        super.onResume();

        mPresenter.init();

        getPreferenceManager().getSharedPreferences().
                registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause(){
        super.onPause();

        mPresenter.onPause();
        getPreferenceManager().getSharedPreferences().
                unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.v(LOG_TAG,"onSharedPreferenceChanged(): key = " + key);
        String keyLogState = getString(R.string.key_login_state);
        String keyThemeColor = getString(R.string.key_pref_theme_color);


        if(!key.equals(getString(R.string.key_pref_wifi_only_sync))){

            String val = sharedPreferences.getString(key, "");

            if (key.equals(keyLogState) && !Boolean.parseBoolean(val)) {
                mPresenter.logout();

                Toast.makeText(getActivity(), R.string.hint_logout_success, Toast.LENGTH_LONG)
                        .show();
            }else if(keyThemeColor.equals(key)){
                ThemeColorUtils.changeThemeColor((AppCompatActivity)getActivity(),val);
            }

        }
    }

    private void initPref(){
        ThemeColorPickerPreference pref = (ThemeColorPickerPreference)findPreference(getString(R.string.key_pref_theme_color));
        String color = SharePrefsHelper.getInstance(getActivity()).getThemeColor();
        List<String> name = Arrays.asList(getResources().getStringArray(R.array.pref_colorList));
        int idx = ThemeColorUtils.findThemeColorIndex(color);
        if(idx != -1) {
            pref.setSummary(name.get(idx));
        }
    }
}
