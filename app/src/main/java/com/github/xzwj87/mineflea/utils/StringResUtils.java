package com.github.xzwj87.mineflea.utils;

import com.github.xzwj87.mineflea.App.AppGlobals;
import com.github.xzwj87.mineflea.R;

/**
 * Created by JasonWang on 2016/9/19.
 */
public class StringResUtils {

    private static String TAB_NAME_1 = AppGlobals.getAppContext().
            getString(R.string.tab_trends);
    private static String TAB_NAME_2 = AppGlobals.getAppContext().
            getString(R.string.tab_nearby);
    private static String TAB_NAME_3 = AppGlobals.getAppContext().
            getString(R.string.tab_mine);


    public static String getTabName(int pos){
        switch (pos){
            case 0:
                return TAB_NAME_1;
            case 1:
                return TAB_NAME_2;
            case 2:
                return TAB_NAME_3;
        }

        return null;
    }
}
