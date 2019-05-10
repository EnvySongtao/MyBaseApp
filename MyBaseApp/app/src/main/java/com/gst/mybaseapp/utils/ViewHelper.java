package com.gst.mybaseapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.WindowManager;

/**
 * author: GuoSongtao on 2019/4/2 13:56
 * email: 157010607@qq.com
 */
public class ViewHelper {


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }


    /**
     * 使用dimen适配时计算px
     * @param context
     * @param name 注意 鼎刷的name 前面要加上 d
     * @return
     */
    public static int dimen2px(Context context, String name) {
        try {
            int resId = context.getResources().getIdentifier(name, "dimen", context.getPackageName());
            return context.getResources().getDimensionPixelSize(resId);
        } catch (Resources.NotFoundException e) {
            return 0;
        }
    }

    /**
     * 使用dimen适配时计算px
     */
    public static int dimen2px(Context context, int dimen) {
        return dimen2px(context, "d" + dimen);//<dimen name="d237">237.0dp</dimen>的情况
    }
}
