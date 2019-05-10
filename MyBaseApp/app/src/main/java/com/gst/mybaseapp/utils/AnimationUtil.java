package com.gst.mybaseapp.utils;

import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.gst.mybaseapp.R;
import com.gst.mybaseapp.base.MyApp;

/**
 * animation为视图动画，视图效果变化的动画
 * animator为属性动画，即动画完成后，View的属性也会发生变化
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
     * animatorSet的使用 注意区分 animationSet
     * 下面两句话原理 先执行a3，然后在同时执行a1和a2
     * animSet.playTogether(a2,a1);//两个动画同时执行
     * animSet.play(a3).before(a2); //先后执行
     *
     * @param view
     */
    public static void animatorSet(View view) {
        ObjectAnimator a1 = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0f, 1.0f);
        ObjectAnimator a2 = ObjectAnimator.ofFloat(view, "translationX", 0f, -50, 0f);
        ObjectAnimator a3 = ObjectAnimator.ofFloat(view, "scaleY", 1, 0.5F, 1);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(3000);
        animSet.setInterpolator(new LinearInterpolator());
        //animSet.playTogether(a1, a2, ...); //两个动画同时执行
        animSet.playTogether(a2, a1);//两个动画同时执行
        animSet.play(a3).before(a2); //先后执行
        animSet.start();
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
        }
        translateAnimation.setInterpolator(interpolator);
        view.startAnimation(translateAnimation);
    }


    /**
     * PathInterpolator
     *
     * @param view
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void testPathObjectAnim(View view) {
//        Path path = new Path();
//        path.cubicTo(0.2f, 0f, 0.1f, 1f, 0.5f, 1f);
//        path.lineTo(1,1);
//        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 500);
//        animator.setInterpolator(PathInterpolatorCompat.create(path));
//        animator.start();

        Path path = new Path();
        path.moveTo(100, 100);
        path.quadTo(1000, 300, 300, 700);

        //ObjectAnimator：继承自ValueAnimator，允许你指定要进行动画的对象以及该对象 的一个属性。
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
        animator.setDuration(3000);
        animator.start();

    }

    public static void propertyValuesHolder(View view) {
        //PropertyValuesHolder：多属性动画同时工作管理类。有时候我们需要同时修改多个属性，那就可以用到此类
        PropertyValuesHolder a1 = PropertyValuesHolder.ofFloat("alpha", 1, 0, 1);
        PropertyValuesHolder a2 = PropertyValuesHolder.ofFloat("scaleX", 1, 0, 1);
        PropertyValuesHolder a3 = PropertyValuesHolder.ofFloat("scaleY", 1, 0.5F, 1);
        PropertyValuesHolder a4 = PropertyValuesHolder.ofFloat("translationX", 0, 50, 0);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, a1, a2, a3, a4);
        animator.setDuration(3000);
        animator.start();
    }

    /**
     * Drawable Animation（Drawable动画）使用详解  真实父类就是Drawable。
     * Drawable动画其实就是Frame动画（帧动画），它允许你实现像播放幻灯片一样的效果，这种动画的实质其实是Drawable，所以这种动画的XML定义方式文件一般放在res/drawable/目录下。
     * 特别注意，AnimationDrawable的start()方法不能在Activity的onCreate方法中调运，因为AnimationDrawable还未完全附着到window上，所以最好的调运时机是onWindowFocusChanged()方法中。
     */
    public static void loading3PointsMove(ImageView rocketImage) {
        rocketImage.setBackgroundResource(R.drawable.animations_loading_3_points);

        AnimationDrawable rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
        rocketAnimation.start();
    }


    /***
     * Property Animation（属性动画）使用详解
     * 其实可以看见，属性动画的实现有7个类，进去粗略分析可以发现，好几个是hide的类，而其他可用的类继承关系又如下：
     *  Animator 子类有 ValueAnimator AnimatorSet
     *  ValueAnimator 子类有 TimeAnimator  ObjectAnimator
     *    ViewPropertyAnimator
     */
    public static void textChangeAnimator(final TextView view) {
        //ValueAnimator：属性动画中的时间驱动，管理着动画时间的开始、结束属性值，相应时间属性值计算方法等。
        final ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(5000);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 通过这样一个监听事件，我们就可以获取
                 * 到ValueAnimator每一步所产生的值。
                 *
                 * 通过调用getAnimatedValue()获取到每个时间因子所产生的Value。
                 * */
                Integer value = (Integer) animation.getAnimatedValue();
                view.setText(value + "");
            }
        });
        animator.start();
    }


    /***
     * Property Animation（属性动画）使用详解
     * 其实可以看见，属性动画的实现有7个类，进去粗略分析可以发现，好几个是hide的类，而其他可用的类继承关系又如下：
     *  Animator 子类有 ValueAnimator AnimatorSet
     *  ValueAnimator 子类有 TimeAnimator  ObjectAnimator
     *    ViewPropertyAnimator
     */
    public static void textChangeTimeAnimator(final TextView view) {
        //ValueAnimator：属性动画中的时间驱动，管理着动画时间的开始、结束属性值，相应时间属性值计算方法等。
        final TimeAnimator animator =new TimeAnimator();
        animator.setTimeListener(new TimeAnimator.TimeListener() {
            @Override
            public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
                view.setText(totalTime + "");
            }
        });
        animator.start();
    }

    /**
     * Evaluator 求值器
     */
    public static void textChangeEvaluator(final TextView view) {

        //ValueAnimator：属性动画中的时间驱动，管理着动画时间的开始、结束属性值，相应时间属性值计算方法等。
        final ValueAnimator animator = ValueAnimator.ofInt(0, 50);
        animator.setDuration(5000);
//        animator.setInterpolator(new LinearInterpolator());
        animator.setObjectValues(0, 50); //设置属性值类型
        animator.setEvaluator(new IntEvaluator() {
            @Override
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                //比如，这里实现的是， 返回数变化率和fraction的开方相同
                return (int) (startValue + (endValue - startValue) * Math.sqrt(fraction));
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                view.setText(value + "");
            }
        });
        animator.start();
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
