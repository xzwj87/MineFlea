package com.github.xzwj87.mineflea.market.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.xzwj87.mineflea.utils.PicassoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * an image page adapter to show a list of images on ViewPage
 */


public class ImagePageAdapter extends PagerAdapter{

    private List<View> mImgList;

    public ImagePageAdapter(Context context, List<String> imgList){
        createViews(context,imgList);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
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
        return mImgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private void createViews(Context context,List<String> urlList){
        mImgList = new ArrayList<>();

        for(int i = 0; i < urlList.size(); ++i){
            ImageView iv = new ImageView(context,null);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

            PicassoUtils.loadImage(context,iv,urlList.get(i));

            mImgList.add(iv);
        }
    }
}
