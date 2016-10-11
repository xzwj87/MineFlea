package com.github.xzwj87.mineflea.utils;

import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.R;

/**
 * Created by JasonWang on 2016/9/19.
 */
public class StringResUtils {


    public static String getTabName(int pos){
        switch (pos){
            case 0:
                return AppGlobals.getAppContext().
                        getString(R.string.tab_trends);
            case 1:
                return AppGlobals.getAppContext().
                        getString(R.string.tab_nearby);
            case 2:
                return AppGlobals.getAppContext().
                        getString(R.string.tab_mine);
        }

        return null;
    }

    public static String getString(int resId){
        return AppGlobals.getAppContext().getString(resId);
    }
}
