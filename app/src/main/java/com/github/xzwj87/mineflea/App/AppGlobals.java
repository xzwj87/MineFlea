package com.github.xzwj87.mineflea.App;

import android.app.Application;
import android.content.Context;

import com.github.xzwj87.mineflea.ui.adapter.SectionsPageAdapter;

/**
 * Created by JasonWang on 2016/9/19.
 */
public class AppGlobals extends Application{

    private static Context sContext;

    public AppGlobals(){
        super();

        sContext = getApplicationContext();
    }

    public static Context getAppContext(){
        return sContext;
    }
}
