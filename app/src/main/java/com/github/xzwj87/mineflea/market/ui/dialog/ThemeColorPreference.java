package com.github.xzwj87.mineflea.market.ui.dialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.ListPreference;
import android.util.AttributeSet;

/**
 * Created by jason on 11/4/16.
 */

public class ThemeColorPreference extends ListPreference {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    public ThemeColorPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ThemeColorPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ThemeColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThemeColorPreference(Context context) {
        super(context);
    }
}
