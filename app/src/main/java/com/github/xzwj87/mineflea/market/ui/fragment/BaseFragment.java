package com.github.xzwj87.mineflea.market.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.xzwj87.mineflea.market.internal.di.HasComponent;
import com.github.xzwj87.mineflea.utils.ThemeColorUtils;

/**
 * Created by JasonWang on 2016/9/19.
 */
public class BaseFragment extends Fragment{

    public BaseFragment(){
        super();

        setRetainInstance(true);
    }

    @Override
    public void onResume(){
        super.onResume();

        checkThemeColor();
    }

    public void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
    }


    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType){
        return componentType.cast(((HasComponent<C>)getActivity()).getComponent());
    }

    private void checkThemeColor(){
        ThemeColorUtils.changeThemeColor((AppCompatActivity)getActivity());
    }
}
