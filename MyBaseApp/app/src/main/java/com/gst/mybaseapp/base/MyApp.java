package com.gst.mybaseapp.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.gst.mybaseapp.database.DataBaseManager;

import java.util.Locale;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;
import me.jessyan.autosize.utils.LogUtils;

public class MyApp extends Application {

    private static MyApp ourInstance = null;
    private static SharedPreferences mSp = null;


    public static MyApp getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = MyApp.this;
        initAutoSize();
        initUtilsWithNoUiTread();
    }

    /**
     * 非主线程初始化数据
     */
    private void initUtilsWithNoUiTread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataBaseManager.initCityDb(MyApp.this);
                Log.e("MyApp ", "DataBaseManager.initCityDb(MyApp.this) successfully");
            }
        }).start();
    }


    private void initAutoSize() {
        // TODO: 2018/12/11 0011 今日头条屏幕适配
        /**
         * 以下是 AndroidAutoSize 可以自定义的参数, {@link AutoSizeConfig} 的每个方法的注释都写的很详细
         * 使用前请一定记得跳进源码，查看方法的注释, 下面的注释只是简单描述!!!
         */
        AutoSizeConfig.getInstance()
                //屏幕适配监听器
                .setOnAdaptListener(new onAdaptListener() {
                    @Override
                    public void onAdaptBefore(Object target, Activity activity) {
                        //使用以下代码, 可支持 Android 的分屏或缩放模式, 但前提是在分屏或缩放模式下当用户改变您 App 的窗口大小时
                        //系统会重绘当前的页面, 经测试在某些机型, 某些情况下系统不会重绘当前页面, ScreenUtils.getScreenSize(activity) 的参数一定要不要传 Application!!!
//                        AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
//                        AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
                        LogUtils.d(String.format(Locale.ENGLISH, "%s onAdaptBefore!", target.getClass().getName()));
                    }

                    @Override
                    public void onAdaptAfter(Object target, Activity activity) {
                        LogUtils.d(String.format(Locale.ENGLISH, "%s onAdaptAfter!", target.getClass().getName()));
                    }
                });
        AutoSize.initCompatMultiProcess(this);
    }

    public void runOnUiThread(Runnable runnable) {
        MyComponentsManager.getInstance().getCurrentActivity().runOnUiThread(runnable);
    }

    public static SharedPreferences getSharedPreferences() {
        if (mSp == null && ourInstance != null) {
            mSp = ourInstance.getSharedPreferences(AppConfig.SHARED_PREFERENCES_NAME_NORMAL, Context.MODE_PRIVATE);
        }
        return mSp;
    }

}
