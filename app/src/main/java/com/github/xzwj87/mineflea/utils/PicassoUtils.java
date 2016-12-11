package com.github.xzwj87.mineflea.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.github.xzwj87.mineflea.app.AppGlobals;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by jason on 11/12/16.
 */

public class PicassoUtils {

    private static final int RESIZE_DEFAULT_RESOLUTION = 512;
    private static final int RESIZE_HIGH_RESOLUTION = 1024;

    public static void loadImage(ImageView iv, String imgUrl){
        if(iv == null || TextUtils.isEmpty(imgUrl)) return;

        if(URLUtil.isNetworkUrl(imgUrl)){
            Picasso.with(AppGlobals.getAppContext())
                   .load(imgUrl)
                   .resize(RESIZE_HIGH_RESOLUTION, RESIZE_HIGH_RESOLUTION)
                   .centerCrop()
                   .into(iv);
        // from file
        }else{
            Picasso.with(AppGlobals.getAppContext())
                    .load(Uri.fromFile(new File(imgUrl)))
                    .resize(RESIZE_HIGH_RESOLUTION, RESIZE_HIGH_RESOLUTION)
                    .centerCrop()
                    .into(iv);
        }
    }

    public static void loadImage(Context context,ImageView iv, String imgUrl){
        if(context == null || iv == null || TextUtils.isEmpty(imgUrl)) return;

        if(URLUtil.isNetworkUrl(imgUrl)){
            Picasso.with(context)
                    .load(imgUrl)
                    .resize(RESIZE_HIGH_RESOLUTION, RESIZE_HIGH_RESOLUTION)
                    .centerCrop()
                    .into(iv);
            // from file
        }else{
            Picasso.with(context)
                    .load(new File(imgUrl))
                    .resize(RESIZE_HIGH_RESOLUTION, RESIZE_HIGH_RESOLUTION)
                    .centerCrop()
                    .into(iv);
        }
    }
}
