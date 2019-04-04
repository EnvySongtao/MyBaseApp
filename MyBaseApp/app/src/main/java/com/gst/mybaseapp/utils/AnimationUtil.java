package com.gst.mybaseapp.utils;

import android.graphics.Path;
import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.gst.mybaseapp.R;
import com.gst.mybaseapp.base.MyApp;

/**
 * 注意  overridePendingTransition(R.anim.slide_in_top, R.anim.slide_in_top); 只支持资源文件动画
 * author: GuoSongtao on 2019/4/3 17:17
 * email: 157010607@qq.com
 */
public class AnimationUtil {

    /**
     * 取消动画
     *
     * @param view
     */
    public static void clearAnimation(View view) {
        view.clearAnimation();
    }

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
     * 顺序执行动画
     * 注意不同于xml  ScaleAnimation动画再次变化时 将以变化后的属性为基础再变化
     * setStartOffset中间有重叠时属性的会重叠，产生怪异效果
     *
     * @param view
     */
    public static void squenceAnimations(View view) {
        AnimationSet animationSet = new AnimationSet(false);
        float toX = 0.5F, toY = 0.5F;
        ScaleAnimation scaleAnimation1 = new ScaleAnimation(1, toX, 1, toY, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        //设置动画的持续时间
        scaleAnimation1.setDuration(500);
        animationSet.addAnimation(scaleAnimation1);

        //nimationSet.setFillAfter方法用户设置动画结束后是否保存状态；
        //但是设置为true的时候总是无效，原来需要设置setFillEnabled属性才有效;
        //scaleAnimation1.setFillAfter(false); //实际测试， 无论animationSet和scaleAnimation1设置为false或true效果仍然没变化
        //scaleAnimation1.setFillEnabled(false);

        toX = 1.3F / 0.5F;//
        toY = 1.3F / 0.5F;//0.5是当前控件大小比上初始的
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(1, toX, 1, toY, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        scaleAnimation2.setDuration(1000);
        scaleAnimation2.setStartOffset(600);
        animationSet.addAnimation(scaleAnimation2);

        toX = 1F / 1.3F;
        toY = 1F / 1.3F;//1.3是当前控件大小比上初始的
        ScaleAnimation scaleAnimation3 = new ScaleAnimation(1, toX, 1, toY, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        scaleAnimation3.setDuration(500);
        scaleAnimation3.setStartOffset(1700);
        animationSet.addAnimation(scaleAnimation3);

        view.startAnimation(animationSet);
    }


    /**
     * 顺序执行动画
     *
     * @param view
     */
    public static void squenceAnimByRes(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.sequence_anim));
    }

    /**
     * 插值器简介
     * java类	xml id值	描述
     * AccelerateDecelerateInterpolator	@android:anim/accelerate_decelerate_interpolator	动画始末速率较慢，中间加速
     * AccelerateInterpolator	@android:anim/accelerate_interpolator	动画开始速率较慢，之后慢慢加速
     * AnticipateInterpolator	@android:anim/anticipate_interpolator	开始的时候从后向前甩
     * AnticipateOvershootInterpolator	@android:anim/anticipate_overshoot_interpolator	类似上面AnticipateInterpolator
     * BounceInterpolator	@android:anim/bounce_interpolator	动画结束时弹起
     * CycleInterpolator	@android:anim/cycle_interpolator	循环播放速率改变为正弦曲线
     * DecelerateInterpolator	@android:anim/decelerate_interpolator	动画开始快然后慢
     * LinearInterpolator	@android:anim/linear_interpolator	动画匀速改变
     * OvershootInterpolator	@android:anim/overshoot_interpolator	向前弹出一定值之后回到原来位置
     * PathInterpolator	 	新增，定义路径坐标后按照路径坐标来跑。  主要给属性动画使用
     */
    public static void translateX(View view, Interpolator interpolator) {
        int xMove = ViewHelper.dip2px(MyApp.getInstance(), 50);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, xMove, 0, 0);
        translateAnimation.setDuration(500);
        if (interpolator == null) {
            interpolator = new CycleInterpolator(9);//Cycle循环插值器 从-1倍变换到1倍，参数为循环多少次
            interpolator = new LinearInterpolator();//线性插值器 从from变换线性(等差数列)到to

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                //The Path must start at (0,0) and end at (1,1)    The Path cannot loop back on itself.
                Path path = new Path();
                path.reset();
                path.moveTo(0, 0);
                path.lineTo(1,1);
                path.lineTo(-1,-1);
                path.lineTo(1,1);
                //主要给属性动画使用  view动画会报异常
                interpolator = new PathInterpolator(path);
            }
        }
        translateAnimation.setInterpolator(interpolator);
        view.startAnimation(translateAnimation);
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
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.push_bottom_out));
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
