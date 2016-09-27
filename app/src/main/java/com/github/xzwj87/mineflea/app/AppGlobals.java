package com.github.xzwj87.mineflea.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by JasonWang on 2016/9/19.
 */
public class AppGlobals extends Application{

    private static Context sContext;

    @Override
    public void onCreate(){
        super.onCreate();

        sContext = getApplicationContext();
    }

    public AppGlobals(){
        super();
    }

    public static Context getAppContext(){
        return sContext;
    }
}
