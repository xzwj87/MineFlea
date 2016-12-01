package com.github.xzwj87.mineflea.market.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.github.xzwj87.mineflea.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JasonWang on 2016/11/7.
 */
public class ColorGridAdapter extends BaseAdapter {
    private Context mContext;
    private CharSequence[] mColorList;
    private List<ImageView> mIvList;
    private int mDefaultPos;

    private ColorItemClickListener mListener;

    public ColorGridAdapter(Context context, CharSequence[] colorList, int defPos){
        mColorList = colorList;
        mContext = context;
        mDefaultPos = defPos;
        mIvList = new ArrayList<>();
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
                                 .inflate(R.layout.item_color_grid,parent,false);
            ViewHolder vh = new ViewHolder(root,position);

            vh.ivColor.setBackgroundColor(Color.parseColor(mColorList[position].toString()));

            if(position == mDefaultPos){
                vh.ivSelected.setVisibility(View.VISIBLE);
                vh.ivSelected.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
            }

            mIvList.add(vh.ivSelected);
        }

        return root;
    }


    private class ViewHolder{
        public ImageView ivColor;
        public ImageView ivSelected;

        public ViewHolder(View root, final int pos){
            ivColor = (ImageView)root.findViewById(R.id.iv_color);
            ivSelected = (ImageView)root.findViewById(R.id.iv_selected);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(pos);

                    for(int i = 0; i < mColorList.length; ++i){
                        if(i == pos){
                            mIvList.get(i).setVisibility(View.VISIBLE);
                            mIvList.get(i).setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_in));
                        }else if(mIvList.get(i).getVisibility() == View.VISIBLE){
                            mIvList.get(i).setVisibility(View.INVISIBLE);
                            mIvList.get(i).setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_out));
                        }
                    }
                }
            });
        }
    }
}
