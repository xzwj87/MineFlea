package com.github.xzwj87.mineflea.market.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.utils.PicassoUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * an image page adapter to show a list of images on ViewPage
 */

public class ImagePageAdapter extends PagerAdapter{

    private Context mContext;
    private List<View> mImgList;

    public ImagePageAdapter(Context context,List<String> imgList){
        if(imgList == null) return;

        mContext = context;
        mImgList = new ArrayList<>(imgList.size());

        createViews(mContext,imgList);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if(mImgList == null || mImgList.size() == 0) return null;

        ImageView iv = (ImageView)mImgList.get(position);
        container.addView(iv);

        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView)object);
    }

    @Override
    public int getCount() {
        return mImgList == null ? 0 : mImgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private void createViews(Context context,List<String> urlList){
        if(urlList.size() == 0){
            ImageView iv = new ImageView(context,null);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

            PicassoUtils.loadImage(iv, R.mipmap.a_x);
            mImgList.add(iv);
        }else {
            for (int i = 0; i < urlList.size(); ++i) {
                ImageView iv = new ImageView(context, null);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

                PicassoUtils.loadImage(context, iv, urlList.get(i));

                mImgList.add(iv);
            }
        }
    }
}
