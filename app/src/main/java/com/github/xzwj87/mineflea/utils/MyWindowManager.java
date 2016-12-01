package com.github.xzwj87.mineflea.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import com.github.xzwj87.mineflea.market.ui.view.FloatWindowSmallView;
/**
 * Created by seaice on 2016/7/5.
 */
public class MyWindowManager {
    private static final String TAG = "MyWindowManager";

    private static MyWindowManager instance = null;
    private FloatWindowSmallView smallWin;
    private WindowManager.LayoutParams smallWinParams;

    private WindowManager windowManager;
    private static final int SMALL_WIN = 1;

    private MyWindowManager() {}

    public static MyWindowManager getInstance() {
        if (instance == null) {
            instance = new MyWindowManager();
        }
        return instance;
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SMALL_WIN:
                    windowManager.addView(smallWin, smallWinParams);
                    break;
                default:
                    break;
            }
        }
    };

    //创建小的悬浮窗
    public void createSmallWindow(Context ctx) {
        synchronized (this) {
            windowManager = getWindowManager(ctx);
            int screenWidth = windowManager.getDefaultDisplay().getWidth();
            int screenHeight = windowManager.getDefaultDisplay().getHeight();
            if (smallWin == null) {
                smallWin = new FloatWindowSmallView(ctx);
                if (smallWinParams == null) {
                    Log.e(TAG, "CREATE SMALL WINDOW");
                    smallWinParams = new WindowManager.LayoutParams();
                    smallWinParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                    smallWinParams.format = PixelFormat.RGBA_8888;
                    smallWinParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
                    smallWinParams.gravity = Gravity.LEFT | Gravity.TOP;
                    smallWinParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    smallWinParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    smallWinParams.x = screenWidth / 2;
                    smallWinParams.y = screenHeight / 2;
                }
                smallWin.setParams(smallWinParams);
                windowManager.addView(smallWin, smallWinParams);
                //handler.sendEmptyMessageDelayed(SMALL_WIN, 300);
            }
        }
    }

    //将悬浮窗从屏幕上移除
    public void removeSmallWindow(Context context) {
        synchronized (this) {
            if (smallWin != null) {
                WindowManager windowManager = getWindowManager(context);
                windowManager.removeView(smallWin);
                smallWin = null;
            }
        }
    }

    private WindowManager getWindowManager(Context ctx) {
        if (windowManager == null) {
            windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        }
        return windowManager;
    }

    public void removeWindow(Context ctx) {
        removeSmallWindow(ctx);
    }
}
