package com.github.xzwj87.mineflea.market.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.github.xzwj87.mineflea.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JasonWang on 2016/11/7.
 */
public class ColorListAdapter extends BaseAdapter {

    private CharSequence[] mColorList;
    private List<RadioButton> mRbList;
    private Context mContext;
    private int mDefaultPos = 0;
    // click listener to UI
    private ColorItemClickListener mListener;

    public ColorListAdapter(Context context, CharSequence[] colorList, int defPos){
        mContext = context;
        mColorList = colorList;
        mDefaultPos = defPos;
        mRbList = new ArrayList<>(mColorList.length);
    }

    public void setListener(ColorItemClickListener listener){
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mColorList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root = convertView;
        if(root == null){
            root = LayoutInflater.from(mContext)
                                 .inflate(R.layout.item_color_list,parent,false);
            ViewHolder vh = new ViewHolder(root,position);

            mRbList.add(vh.chooser);
            vh.color.setBackgroundColor(Color.parseColor(mColorList[position].toString()));

            if(position == mDefaultPos){
                vh.chooser.setChecked(true);
            }
        }

        return root;
    }


    public class ViewHolder{
        public RadioButton chooser;
        public ImageView color;


        public ViewHolder(View root, final int pos){
            chooser = (RadioButton)root.findViewById(R.id.rb_chooser);
            color = (ImageView)root.findViewById(R.id.iv_color);


            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for(int i = 0; i < mRbList.size(); ++i) {
                        if(i == pos) {
                            mRbList.get(i).setChecked(true);
                        }else if(mRbList.get(i).isChecked()){
                            mRbList.get(i).setChecked(false);
                        }
                    }

                    if(mListener != null) {
                        mListener.onClick(pos);
                    }
                }
            });
        }
    }
}
