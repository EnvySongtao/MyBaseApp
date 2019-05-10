package com.gst.mybaseapp.utils;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.gst.mybaseapp.base.MyApp;


public class ToastUtil {
    private static ToastUtil instance = new ToastUtil();

    private ToastUtil() {
        context = MyApp.getInstance().getApplicationContext();
        t = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    private static Toast t;
    private static Context context;

    /**
     * @param msg
     * @param time 多少时间
     */
    public static void showAsTime(CharSequence msg, int time) {
        instance.instanceShow(msg, -1, -1, time);
    }

    /**
     * 4.0后不适用 预留改为handler
     *
     * @param resId
     * @param time
     */
    public static void showAsTime(int resId, int time) {
        CharSequence msg = "";
        try {
            msg = context.getText(resId);
        } catch (Exception e) {
            msg = resId + "";
            e.printStackTrace();
        }
        showAsTime(msg, time);
    }

    public static void show(CharSequence msg, int gravity) {
        instance.instanceShow(msg, Toast.LENGTH_SHORT, gravity, -1);
    }

    /**
     * 默认短时
     *
     * @param msg
     */
    public static void show(CharSequence msg) {
        instance.instanceShow(msg, Toast.LENGTH_SHORT, -1, -1);
    }

    public static void show(int resId) {
        CharSequence msg = "";
        try {
            msg = context.getText(resId);
        } catch (Exception e) {
            msg = resId + "";
            e.printStackTrace();
        }
        show(msg);
    }

    public static void showLong(CharSequence msg) {
        instance.instanceShow(msg, Toast.LENGTH_LONG, -1, -1);
    }

    public static void showLong(int resId) {
        CharSequence msg = "";
        try {
            msg = context.getText(resId);
        } catch (Exception e) {
            msg = resId + "";
            e.printStackTrace();
        }
        showLong(msg);
    }


    private void instanceShow(final CharSequence msg, final int duration, final int gravity, final int time) {
        MyApp.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                t.setText(msg);

                if (gravity > 0) {
                    t.setGravity(gravity, 0, 0);
                }

                if (duration == Toast.LENGTH_LONG || duration == Toast.LENGTH_SHORT) {
                    t.setDuration(duration);
                } else if (time > 0 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    t.setDuration(time);
                } else {
                    t.setDuration(Toast.LENGTH_SHORT);
                }

                t.show();
            }
        });
    }

    /**
     * 保证主线程外 也可以弹出Toast
     */
    private void instanceShow() {
        MyApp.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                t.show();
            }
        });
    }
}
