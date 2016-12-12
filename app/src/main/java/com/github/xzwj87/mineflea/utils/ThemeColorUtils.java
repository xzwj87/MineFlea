package com.github.xzwj87.mineflea.utils;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.github.xzwj87.mineflea.BuildConfig;
import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.app.AppGlobals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jason on 11/10/16.
 */

public class ThemeColorUtils {

    private static List<String> sThemeColorList = new ArrayList<>();
    private static List<String> sThemeDarkColorList = new ArrayList<>();

    static{
        Resources res = AppGlobals.getAppContext().getResources();
        sThemeColorList = Arrays.asList(res.getStringArray(R.array.pref_colorList_values));
        sThemeDarkColorList = Arrays.asList(res.getStringArray(R.array.color_list_dark_values));
    }


    public static void changeThemeColor(AppCompatActivity activity,String color){
        if(activity == null || TextUtils.isEmpty(color)) return;

        int c = Color.parseColor(color);

        String keyThemeColor = activity.getString(R.string.key_pref_theme_color);
        SharePrefsHelper.getInstance(activity).updateValue(keyThemeColor,color);

        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null){
            actionBar.setBackgroundDrawable(new ColorDrawable(c));
        }

        int ind = sThemeColorList.indexOf(color);
        if(ind != -1){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().setStatusBarColor(
                        Color.parseColor(sThemeDarkColorList.get(ind)));
            }
        }
    }

    public static void changeThemeColor(AppCompatActivity activity){
        if(activity == null) return;

        SharePrefsHelper helper = SharePrefsHelper.getInstance(activity);
        String color = helper.getThemeColor();
        int c = Color.parseColor(color);

        String keyThemeColor = activity.getString(R.string.key_pref_theme_color);
        helper.updateValue(keyThemeColor,color);


        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null){
            actionBar.setBackgroundDrawable(new ColorDrawable(c));
        }

        int ind = sThemeColorList.indexOf(color);
        if(ind != -1){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().setStatusBarColor(
                        Color.parseColor(sThemeDarkColorList.get(ind)));
            }
        }
    }

    public static int findThemeColorIndex(String color){
        return sThemeColorList.indexOf(color);
    }
}
