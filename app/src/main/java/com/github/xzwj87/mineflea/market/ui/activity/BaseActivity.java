package com.github.xzwj87.mineflea.market.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.internal.di.component.AppComponent;
import com.github.xzwj87.mineflea.market.internal.di.module.ActivityModule;

/**
 * Created by jason on 9/29/16.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedState){
        super.onCreate(savedState);

        getAppComponent().inject(this);
    }

    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    public AppComponent getAppComponent() {
        return ((AppGlobals)getApplication()).getComponent();
    }

    public ActivityModule getActivityModule(){
        return new ActivityModule(this);
    }
}
