package com.github.xzwj87.mineflea.utils;

import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.maps2d.model.LatLng;
import com.github.xzwj87.mineflea.app.AppGlobals;

import java.util.Calendar;

/**
 * Created by seaice on 2016/8/12.
 */
public class UiUtils {
    private static final String TAG = "UiUtils";

    public static Resources getResource() {
        return AppGlobals.getAppContext().getResources();
    }

    /**
     * 获取字符串数组
     *
     * @param tabNames
     * @return
     */
    public static String[] getStringArray(int tabNames) {
        return getResource().getStringArray(tabNames);
    }

    public static String getString(int str){
        return getResource().getString(str);
    }

    public static void runOnUiThread(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        //证明在主线程
        if (android.os.Process.myTid() == AppGlobals.getMainThreadId()) {
            Log.e(TAG, "主线程");
            runnable.run();
        } else {
            Log.e(TAG, "非主线程");
            AppGlobals.getHandler().post(runnable);
        }
    }

    public static String getDayOfWeek(int day) {
        String dayOfWeek = "";
        switch (day) {
            case Calendar.MONDAY:
                dayOfWeek = "一";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "二";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "三";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "四";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "五";
                break;
            case Calendar.SATURDAY:
                dayOfWeek = "六";
                break;
            case Calendar.SUNDAY:
                dayOfWeek = "日";
                break;
            default:
                break;

        }
        return dayOfWeek;
    }

    public static void showToast(String str){
        Toast.makeText(AppGlobals.getAppContext(), str, Toast.LENGTH_SHORT).show();
    }

    //获取两点的距离，米为单位
    public static double getDistanceM(LatLng start, LatLng end) {
        double dis = getDistance(start, end)*1000;
        return dis;
    }

    //获取两点的距离，千米为单位
    public static double getDistanceKm(LatLng start, LatLng end) {
        double dis = getDistance(start, end);
        return dis;
    }

    //计算两点的距离
    public static double getDistance(LatLng start, LatLng end) {
        if (start != null && end != null) {
            double lon1 = (Math.PI / 180) * start.longitude;
            double lon2 = (Math.PI / 180) * end.longitude;
            double lat1 = (Math.PI / 180) * start.latitude;
            double lat2 = (Math.PI / 180) * end.latitude;

            // 地球半径
            double R = 6371;

            // 两点间距离 km，如果想要米的话，结果*1000就可以了
            double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
            return d;
        }
        return 0;
    }

}
