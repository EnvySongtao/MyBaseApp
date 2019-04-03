package com.gst.mybaseapp.customView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.gst.mybaseapp.utils.ViewHelper;

/**
 * author: GuoSongtao on 2019/4/2 16:31
 * email: 157010607@qq.com
 */
public class LoadingViewRing extends View {
    private float mWidth = 0f;
    private float mPadding = 0f;
    private float startAngle = 0f;
    private Paint mPaint;

    public LoadingViewRing(Context context) {
        this(context, null);
    }

    public LoadingViewRing(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingViewRing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getMeasuredWidth() > getHeight())
            mWidth = getMeasuredHeight();
        else
            mWidth = getMeasuredWidth();
        mPadding = ViewHelper.dip2px(getContext(), 7);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //drawHuaHua(canvas);

        //渐变动画
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setColor(Color.argb(255, 219, 219, 219));
        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2 - mPadding, mPaint);
        RectF rectF = new RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding);
        float moveAngle = startAngle;
        float sweepAngle = 0;
        int colorRGB = 219;
        for (int i = 16; i > 1; i--) {
            //if(i==2) mPaint.setStrokeCap(Paint.Cap.ROUND);
            moveAngle = moveAngle + sweepAngle;
            colorRGB = colorRGB - i;
            mPaint.setColor(Color.argb(255, colorRGB, colorRGB, colorRGB));
            sweepAngle = i;
            canvas.drawArc(rectF, moveAngle, sweepAngle, false, mPaint);//第四个参数是否显示半径
        }
    }

    private void drawHuaHua(Canvas canvas) {
//        //花花动画  用 valueAnimator 画出来有些模糊影子
        RectF rectF = new RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding);
        float moveAngle = startAngle;
        float sweepAngle = 15;
        int colorRGB = 219;
        for (int i = 8; i > 0; i--) {
            //if(i==2) mPaint.setStrokeCap(Paint.Cap.ROUND);
            moveAngle = moveAngle + 45;
            colorRGB = colorRGB - 20;
            mPaint.setColor(Color.argb(255, colorRGB, colorRGB, colorRGB));
            canvas.drawArc(rectF, moveAngle, sweepAngle, false, mPaint);//第四个参数是否显示半径
        }

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStrokeWidth(ViewHelper.dip2px(getContext(), 9));
    }

    public void startAnim() {
        stopAnim();
        startViewAnim(0f, 1f, 1000);
    }

    public void stopAnim() {
        if (valueAnimator != null) {
            clearAnimation();
            valueAnimator.setRepeatCount(1);
            valueAnimator.cancel();
            valueAnimator.end();
        }
    }

    ValueAnimator valueAnimator;

    private ValueAnimator startViewAnim(float startF, final float endF, long time) {
        valueAnimator = ValueAnimator.ofFloat(startF, endF);

        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);//

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                float value = (float) valueAnimator.getAnimatedValue();
                startAngle = 360 * value;

                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }

        return valueAnimator;
    }

}
