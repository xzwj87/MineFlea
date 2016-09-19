package com.github.xzwj87.mineflea.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.xzwj87.mineflea.R;

/**
 * Created by JasonWang on 2016/9/19.
 */
public class TabHolderFragment extends BaseFragment{
    public static final String TAG = TabHolderFragment.class.getSimpleName();

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";


    public TabHolderFragment(){
    }


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TabHolderFragment newInstance(int sectionNumber) {
        TabHolderFragment fragment = new TabHolderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mine_flea, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}
