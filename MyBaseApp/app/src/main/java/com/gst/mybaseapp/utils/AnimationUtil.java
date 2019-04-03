package com.gst.mybaseapp.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

import com.gst.mybaseapp.base.MyApp;

/**
 *
 * 注意  overridePendingTransition(R.anim.slide_in_top, R.anim.slide_in_top); 只支持资源文件动画
 * author: GuoSongtao on 2019/4/3 17:17
 * email: 157010607@qq.com
 */
public class AnimationUtil {


    /**
     * 一次执行所有的动画效果
     * Animation 执行完会恢复原状
     *
     * @param view
     */
    public static void executeAllAnimations(View view, float apha1, float apha2) {

        /*
         *  创建一个AnimationSet，它能够同时执行多个动画效果
         *  构造方法的入参如果是“true”，则代表使用默认的interpolator，如果是“false”则代表使用自定义interpolator
         */
        AnimationSet animationSet = new AnimationSet(true);
        //创建一个半透明效果的动画对象，效果从完全透明到完全不透明
        AlphaAnimation alphaAnimation = new AlphaAnimation(apha1, apha2);
        //设置动画的持续时间
        alphaAnimation.setDuration(3000);

        /*
         *  创建一个旋转动画对象
         *  入参列表含义如下：
         *  1.fromDegrees：从哪个角度开始旋转
         *  2.toDegrees：旋转到哪个角度结束
         *  3.pivotXType：旋转所围绕的圆心的x轴坐标的类型，有ABSOLUT绝对坐标、RELATIVE_TO_SELF相对于自身坐标、RELATIVE_TO_PARENT相对于父控件的坐标
         *  4.pivotXValue：旋转所围绕的圆心的x轴坐标,0.5f表明是以自身这个控件的一半长度为x轴
         *  5.pivotYType：y轴坐标的类型
         *  6.pivotYValue：y轴坐标
         */
//        RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);


        /*
         *  创建一个缩放(ScaleAnimation)效果的动画
         *  入参列表含义如下：
         *  fromX：x轴的初始值
         *  toX：x轴缩放后的值
         *  fromY：y轴的初始值
         *  toY：y轴缩放后的值
         *  pivotXType：x轴坐标的类型，有ABSOLUT绝对坐标、RELATIVE_TO_SELF相对于自身坐标、RELATIVE_TO_PARENT相对于父控件的坐标
         *  pivotXValue：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴
         *  pivotYType：y轴坐标的类型
         *  pivotYValue：轴的值，0.5f表明是以自身这个控件的一半长度为y轴
         */
//        ScaleAnimation scaleAnimation = new ScaleAnimation(0,0.1f,0,0.1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);


        /*
         *  创建一个移动动画(TranslateAnimation)效果
         *  入参的含义如下：
         *  fromXType：移动前的x轴坐标的类型
         *  fromXValue：移动前的x轴的坐标
         *  toXType：移动后的x轴的坐标的类型
         *  toXValue：移动后的x轴的坐标
         *  fromYType：移动前的y轴的坐标的类型
         *  fromYValue：移动前的y轴的坐标
         *  toYType：移动后的y轴的坐标的类型
         *  toYValue：移动后的y轴的坐标
         *  Animation.RELATIVE_TO_SELF 相对控件本身大小
         *  Animation.RELATIVE_TO_PARENT 相对控件父亲大小
         */
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);


        //设置动画的持续时间
        translateAnimation.setDuration(3000);

        //将四种动画效果放入同一个AnimationSet中
        animationSet.addAnimation(alphaAnimation);
//        animationSet.addAnimation(rotateAnimation);
//        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);

        //同时执行多个动画效果
        view.startAnimation(animationSet);
    }


    /**
     * 左右抖动 50
     *
     * @param view
     */
    public static void translateX(View view) {
        int xMove = ViewHelper.dip2px(MyApp.getInstance(), 50);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, xMove, 0, 0);
        //设置动画的持续时间
        translateAnimation.setDuration(500);
        translateAnimation.setInterpolator(new CycleInterpolator(9));
        view.startAnimation(translateAnimation);
    }

    /**
     * 上下抖动
     *
     * @param view
     */
    public static void translateY(View view) {
        int yMove = ViewHelper.dip2px(MyApp.getInstance(), 50);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, yMove);
        //设置动画的持续时间
        translateAnimation.setDuration(500);
        translateAnimation.setInterpolator(new CycleInterpolator(9));
        view.startAnimation(translateAnimation);
    }

    /**
     * 从左边进入
     *
     * @param view
     */
    public static void leftIn(View view) {
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0F, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
        //设置动画的持续时间
        translateAnimation.setDuration(500);
        view.startAnimation(translateAnimation);
    }

    /**
     * 从左边出
     *
     * @param view
     */
    public static void leftOut(View view) {
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0F, Animation.RELATIVE_TO_PARENT, -1.0F, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
        //设置动画的持续时间
        translateAnimation.setDuration(500);
        view.startAnimation(translateAnimation);
    }

    /**
     * 从右边进入
     *
     * @param view
     */
    public static void rightIn(View view) {
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0F, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
        //设置动画的持续时间
        translateAnimation.setDuration(500);
        view.startAnimation(translateAnimation);
    }

    /**
     * 从右边出
     *
     * @param view
     */
    public static void rightOut(View view) {
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0F, Animation.RELATIVE_TO_PARENT, 1.0F, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
        //设置动画的持续时间
        translateAnimation.setDuration(500);
        view.startAnimation(translateAnimation);
    }

    /**
     * @param view 顺时针转圈
     */
    public static void waitingIn(View view) {
        RotateAnimation rotateAnimation = new RotateAnimation(-270, 0, view.getWidth() / 2F, view.getHeight() / 2F);
        rotateAnimation.setDuration(100);
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        view.startAnimation(rotateAnimation);
    }

    /**
     * @param view 逆时针转圈
     */
    public static void waitingOut(View view) {
        RotateAnimation rotateAnimation = new RotateAnimation(0, -270, view.getWidth() / 2F, view.getHeight() / 2F);
        rotateAnimation.setDuration(100);
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        view.startAnimation(rotateAnimation);
    }

    /**
     * @param view 下上划入
     */
    public static void push_bottom_in(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0F, Animation.RELATIVE_TO_PARENT, 0F, Animation.RELATIVE_TO_PARENT, 1F, Animation.RELATIVE_TO_PARENT, 0F);
        animationSet.addAnimation(translateAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1F, 0F);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(200);
        view.startAnimation(animationSet);

    }

    /**
     * @param view 上下划出
     */
    public static void push_bottom_out(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0F, Animation.RELATIVE_TO_PARENT, 0F, Animation.RELATIVE_TO_PARENT, 0F, Animation.RELATIVE_TO_PARENT, 0.5F);
        animationSet.addAnimation(translateAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1F, 0F);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(200);
        view.startAnimation(animationSet);
    }

    /**
     * @param view 上下划入
     */
    public static void menu_bottombar_in(View view) {
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0F, Animation.RELATIVE_TO_SELF, 1.0F, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1F);
        //设置动画的持续时间
        translateAnimation.setDuration(250);
        view.startAnimation(translateAnimation);
    }
}
