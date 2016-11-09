package com.github.xzwj87.mineflea.market.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.utils.Constants;
import com.github.xzwj87.mineflea.utils.MyWindowManager;
import com.github.xzwj87.mineflea.utils.UserPrefsUtil;

/**
 * 自定义组合控件
 * Created by seaice on 2016/3/3.
 */
public class FloatWindowSmallView extends LinearLayout {
    private static final String TAG = "FloatWindowSmallView";

    public static int viewWidth;
    public static int viewHeight;
    public static int screenHeight;
    public static int screenWidth;
    private WindowManager wm;
    private WindowManager.LayoutParams mParams;
    //手指在屏幕上的横坐标
    private float xInScreen;
    //手指在屏幕上的纵坐标
    private float yInScreen;
    //按下屏幕时的横坐标
    private float xDownInScreen;
    //按下屏幕时的纵坐标
    private float yDownInScreen;
    //小悬浮框View上的横坐标
    private float xInView;
    //小悬浮框View上的纵坐标
    private float yInView;
    private Context ctx;
    private final int CLICK = 4;

    public FloatWindowSmallView(Context context) {
        super(context);
        ctx = context;

        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
        View view = findViewById(R.id.small_window_layout);
        view.setAlpha(0.8f);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        screenHeight = wm.getDefaultDisplay().getHeight();
        screenWidth = wm.getDefaultDisplay().getWidth();

        Log.e(TAG, "viewWidth = " + viewWidth);
        Log.e(TAG, "viewHeight = " + viewHeight);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewWidth = this.getWidth();
        viewHeight = this.getHeight();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY();

                Log.e(TAG, "***************DOWN*************");
//                Log.e(TAG, "xInView = " + xInView);
//                Log.e(TAG, "yInView = " + yInView);
//                Log.e(TAG, "xDownInScreen = " + xDownInScreen);
//                Log.e(TAG, "yDownInScreen = " + yDownInScreen);
//                Log.e(TAG, "xInScreen = " + xInScreen);
//                Log.e(TAG, "yInScreen = " + yInScreen);

                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "**********MOVE*************");
                xInScreen = event.getRawX();
                yInScreen = event.getRawY();

                yInScreen -= 1;
                xInScreen -= 1;

                if ((yInScreen - yDownInScreen <= 2 && yDownInScreen - yInScreen <= 2)
                        && xInScreen - xDownInScreen <= 2 && xDownInScreen - xInScreen <= 2) {
                    break;
                }
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                //在这个范围内，都默认是点击事件
                Log.e(TAG, "**********MOVE*************");
                if (isOpenBigWin()) {
                    UserPrefsUtil.setIntPref(ctx, Constants.PREF_START_X, mParams.x);
                    UserPrefsUtil.setIntPref(ctx, Constants.PREF_START_Y, mParams.y);
                    sendMsgToNearbyFragment();
                }
                break;
            default:
                break;
        }
        return false;
    }

    private void sendMsgToNearbyFragment() {
        MyWindowManager.getInstance().removeSmallWindow(ctx);
        Intent intent = new Intent();
        intent.setAction(Constants.COM_SEARCH_GOODS_ACTION);
        AppGlobals.getAppContext().sendBroadcast(intent);
    }

    private boolean isOpenBigWin() {
        boolean ret;
        ret = ((xInScreen - xDownInScreen <= CLICK && xDownInScreen - xInScreen <= CLICK)
                && (yInScreen - yDownInScreen <= CLICK && yDownInScreen - yInScreen <= CLICK));
        return ret;
    }


    //更新浮动窗口的位置
    private void updateViewPosition() {
        Log.e(TAG, "**********updateViewPosition*************");
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);

        Log.e(TAG, "mParams.x = " + mParams.x);
        Log.e(TAG, "mParams.y = " + mParams.y);

        if (mParams.x < 0) {
            mParams.x = 0;
        }
        if (mParams.y < 0) {
            mParams.y = 0;
        }
        if (mParams.x > screenWidth - viewWidth) {
            mParams.x = screenWidth - viewWidth;
        }
        if (mParams.y > screenHeight - viewHeight - 20) {
            mParams.y = screenHeight - viewHeight - 20;
        }

        wm.updateViewLayout(this, mParams);
    }

    public void setmParams(WindowManager.LayoutParams p) {
        mParams = p;
    }

}
