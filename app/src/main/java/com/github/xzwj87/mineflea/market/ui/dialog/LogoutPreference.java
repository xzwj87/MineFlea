package com.github.xzwj87.mineflea.market.ui.dialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.ListPreference;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;

import com.github.xzwj87.mineflea.R;

/**
 * Created by jason on 11/4/16.
 */

public class LogoutPreference extends ListPreference{

    private static final boolean LOGOUT_CONFIRM = true;
    private static final boolean LOGOUT_CANCEL = false;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LogoutPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LogoutPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LogoutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LogoutPreference(Context context) {
        super(context);
    }

    @Override
    protected void onPrepareDialogBuilder(android.app.AlertDialog.Builder builder){
        builder.setPositiveButton(getPositiveButtonText(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setValue(String.valueOf(!LOGOUT_CONFIRM));
            }
        });
        builder.setNegativeButton(getNegativeButtonText(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //setValue(String.valueOf(!LOGOUT_CANCEL));
            }
        });
    }
}
