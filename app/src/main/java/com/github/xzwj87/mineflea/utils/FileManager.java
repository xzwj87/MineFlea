package com.github.xzwj87.mineflea.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jason on 10/7/16.
 */

public class FileManager {

    public static String saveBitmapToFile(String path, String fileName, Bitmap bitmap){
        File dir = new File(path);
        File imgFile = new File(dir,fileName);

        FileOutputStream fos = null;

        try{
            fos = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);

            return imgFile.getPath();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(fos != null){
                try {
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static Bitmap drawableToBitmap(Drawable drawable){
        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable,int width, int height){
        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
