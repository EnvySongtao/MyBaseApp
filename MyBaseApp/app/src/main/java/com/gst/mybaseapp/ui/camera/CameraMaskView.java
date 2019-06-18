package com.gst.mybaseapp.ui.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;


import com.gst.mybaseapp.R;
import com.gst.mybaseapp.base.AppConfig;
import com.gst.mybaseapp.utils.BitmapUtils;
import com.gst.mybaseapp.utils.ViewHelper;

import java.lang.ref.SoftReference;

import me.jessyan.autosize.utils.LogUtils;


/**
 * 自定义身份证和银行卡裁剪相机
 * author: GuoSongtao on 2018/4/10 11:22
 * email: 157010607@qq.com
 */

public class CameraMaskView extends View {
    public CameraMaskView(Context context) {
        this(context, null);
    }

    public CameraMaskView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraMaskView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    int width, height;
    Paint mPaint;
    int colorHalfTran = Color.parseColor("#99666666");
    int colorGrey = Color.parseColor("#393939");
    Frame mFrame = new Frame();
    private boolean isShowPhoto = false;
    private SoftReference<Bitmap> softPhoto;
    private SoftReference<Bitmap> maskPhoto;
    private boolean needMask = true;
    private int maskFlag = AppConfig.flag_photo_default;
    int tipPaddingX = 0, tipPaddingTop = 0, tipPaddingBottom = 0;

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mFrame.marginTop = ViewHelper.dip2px(getContext(), 120);
        tipPaddingX = ViewHelper.dip2px(getContext(), 15);
        tipPaddingTop = ViewHelper.dip2px(getContext(), 35);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        if (height > width) {
            mFrame.widthSide = (int) (width * 1.0 / 10);
            mFrame.frameW = (int) (width * 4.0 / 5);
            mFrame.frameH = (int) (760 * (width * 4.0 / 5) / 500);
            mFrame.marginTop = (height - mFrame.frameH) / 2;
//            mFrame.marginTop = heightSide < DensityUtil.dip2px(getContext(), 120) ? heightSide : DensityUtil.dip2px(getContext(), 120);
        } else {
            //横屏
            int heightSide = (int) (height * 1.0 / 10);
            mFrame.frameH = (int) (height * 4.0 / 5);
            mFrame.frameW = (int) (760 * mFrame.frameH / 500);
            mFrame.widthSide = (width - mFrame.frameW) / 2;
            mFrame.marginTop = heightSide;
        }
        tipPaddingBottom = (int) (height - tipPaddingTop - ((width - 2 * tipPaddingX) * (845F / 609)));

    }

    //drawArc  drawRoundRect 都是Build.VERSION_CODES.LOLLIPOP（21）的
    //表示注解目标只能够在指定的版本API及以上运行,消除高版本Api在低版本SDK上的报错
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        updateMask();
        int dx;
        //画遮罩层
        switch (maskFlag) {
//            case 0x01://默认
//                mPaint.setColor(colorHalfTran);
//                if (softPhoto != null && softPhoto.get() != null && isShowPhoto)
//                    mPaint.setColor(colorGrey);
//                mPaint.setStyle(Paint.Style.FILL);
//                mPaint.setStrokeWidth(0);
//                canvas.drawRect(0, 0, width, mFrame.marginTop, mPaint);
//                canvas.drawRect(0, mFrame.marginTop, mFrame.widthSide, mFrame.marginTop + mFrame.frameH, mPaint);
//                canvas.drawRect(width - mFrame.widthSide, mFrame.marginTop, width, mFrame.marginTop + mFrame.frameH, mPaint);
//                canvas.drawRect(0, mFrame.marginTop + mFrame.frameH, width, height, mPaint);
//
//                mPaint.setColor(Color.WHITE);
//                if (softPhoto != null && softPhoto.get() != null && isShowPhoto) {
//                    Rect srcRect = new Rect(0, 0, softPhoto.get().getWidth(), softPhoto.get().getHeight());
//                    Rect desRect = new Rect(mFrame.widthSide, mFrame.marginTop, width - mFrame.widthSide, mFrame.marginTop + mFrame.frameH);
//                    canvas.drawBitmap(softPhoto.get(), srcRect, desRect, mPaint);
//                }
//
//                mPaint.setColor(Color.WHITE);
//                mPaint.setStyle(Paint.Style.STROKE);
//                mPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 2));
//                dx= DensityUtil.dip2px(getContext(), 1);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    canvas.drawRoundRect(mFrame.widthSide - dx, mFrame.marginTop - dx, width - mFrame.widthSide + dx, mFrame.marginTop + mFrame.frameH + dx,
//                            DensityUtil.dip2px(getContext(), 5), DensityUtil.dip2px(getContext(), 5), mPaint);
//                } else {
//                    canvas.drawRect(mFrame.widthSide - dx, mFrame.marginTop - dx, width - mFrame.widthSide + dx, mFrame.marginTop + mFrame.frameH + dx, mPaint);
//                }
//                break;
            case AppConfig.flag_photo_default://全屏拍
                mPaint.setColor(Color.WHITE);
                if (softPhoto != null && softPhoto.get() != null && isShowPhoto) {
                    Rect srcRect = new Rect(0, 0, softPhoto.get().getWidth(), softPhoto.get().getHeight());
                    Rect desRect = new Rect(0, 0, width, height);
                    canvas.drawBitmap(softPhoto.get(), srcRect, desRect, mPaint);
                }
                break;
            case AppConfig.flag_takeCreditCard_hold://手持信用卡正面
            case AppConfig.flag_takeIdCard_hold://手持身份证正面
                mPaint.setColor(Color.WHITE);
                if (softPhoto != null && softPhoto.get() != null && isShowPhoto) {
                    Rect srcRect = new Rect(0, 0, softPhoto.get().getWidth(), softPhoto.get().getHeight());
                    Rect desRect = new Rect(0, 0, width, height);
                    canvas.drawBitmap(softPhoto.get(), srcRect, desRect, mPaint);
                } else {
                    if (maskPhoto != null && maskPhoto.get() != null) {
                        Rect srcRect = new Rect(0, 0, maskPhoto.get().getWidth(), maskPhoto.get().getHeight());
                        Rect desRect = new Rect(tipPaddingX, tipPaddingTop, width - tipPaddingX, height - tipPaddingBottom);
                        canvas.drawBitmap(maskPhoto.get(), srcRect, desRect, mPaint);
                    }
                }
                break;
            case AppConfig.flag_takeCreditCard_Z://信用卡正面
            case AppConfig.flag_takeBankCard_Z://信用卡正面
            case AppConfig.flag_takeIdCard_F://身份证反面
            case AppConfig.flag_takeIdCard_Z://身份证正面

                mPaint.setColor(colorHalfTran);
                if (softPhoto != null && softPhoto.get() != null && isShowPhoto)
                    mPaint.setColor(colorGrey);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setStrokeWidth(0);
                canvas.drawRect(0, 0, width, mFrame.marginTop, mPaint);
                canvas.drawRect(0, mFrame.marginTop, mFrame.widthSide, mFrame.marginTop + mFrame.frameH, mPaint);
                canvas.drawRect(width - mFrame.widthSide, mFrame.marginTop, width, mFrame.marginTop + mFrame.frameH, mPaint);
                canvas.drawRect(0, mFrame.marginTop + mFrame.frameH, width, height, mPaint);

                mPaint.setColor(Color.WHITE);
                if (softPhoto != null && softPhoto.get() != null && isShowPhoto) {
                    Rect srcRect = new Rect(0, 0, softPhoto.get().getWidth(), softPhoto.get().getHeight());
                    Rect desRect = new Rect(mFrame.widthSide, mFrame.marginTop, width - mFrame.widthSide, mFrame.marginTop + mFrame.frameH);
                    canvas.drawBitmap(softPhoto.get(), srcRect, desRect, mPaint);
                } else {
                    if (maskPhoto != null && maskPhoto.get() != null) {
                        Rect srcRect = new Rect(0, 0, maskPhoto.get().getWidth(), maskPhoto.get().getHeight());
                        Rect desRect = new Rect(mFrame.widthSide, mFrame.marginTop, width - mFrame.widthSide, mFrame.marginTop + mFrame.frameH);
                        canvas.drawBitmap(maskPhoto.get(), srcRect, desRect, mPaint);
                    }
                }
                break;
        }

    }

    /**
     * 边框属性
     */
    public class Frame {
        public int marginTop = 0, widthSide, frameW, frameH;//边框距顶部高度，左侧的距离，边框宽和高
    }

    public Frame getmFrame() {
        return mFrame;
    }

    public void clearPhoto() {
        isShowPhoto = false;
        if (softPhoto != null && softPhoto.get() != null && !softPhoto.get().isRecycled()) {
            softPhoto.get().recycle();
        }
        if (softPhoto != null) {
            softPhoto.clear();
        }
        invalidate();
    }

    public void showPhoto(String filePath) {
        if (softPhoto != null && softPhoto.get() != null && !softPhoto.get().isRecycled()) {
            softPhoto.get().recycle();
        }
        if (softPhoto != null) {
            softPhoto.clear();
        }
        invalidate();
        if (TextUtils.isEmpty(filePath)) return;
        isShowPhoto = true;
        softPhoto = new SoftReference<>(BitmapUtils.getFitSampleBitmap(filePath));

        int ori = BitmapUtils.readPictureDegree(filePath);
        LogUtils.d("ImageUtils.readPictureDegree(filePath) ori = " + ori);
        if (ori != 0 && softPhoto.get() != null) {
            softPhoto = new SoftReference<>(BitmapUtils.rotateBitmapByDegree(softPhoto.get(), ori));
        }
        invalidate();
    }

    public void setNeedMask(boolean needMask) {
        this.needMask = needMask;
    }

    public boolean isNeedMask() {
        return needMask;
    }

    public void setMaskFlag(int maskFlag) {
        this.maskFlag = maskFlag;
        updateMask();
        invalidate();
    }


    private void updateMask() {
        int resId = -1;
        if (getContext() != null) {
            switch (maskFlag) {
                case AppConfig.flag_takeIdCard_Z:
                    resId = R.drawable.camera_front;
                    break;
                case AppConfig.flag_takeIdCard_F:
                    resId = R.drawable.camera_back;
                    break;
                case AppConfig.flag_takeIdCard_hold:
                    resId = R.drawable.camera_check;
                    break;
                case AppConfig.flag_takeCreditCard_hold:
                    resId = R.drawable.camera_credit_check;
                    break;
                case AppConfig.flag_takeCreditCard_Z:
                    resId = R.drawable.camera_credit_front;
                    break;
                case AppConfig.flag_takeBankCard_Z:
                    resId = R.drawable.camera_bankcard_front;
                    break;
            }
        }

        if (resId != -1 && (maskPhoto == null || maskPhoto.get() == null || maskPhoto.get().isRecycled())) {
            maskPhoto = new SoftReference<>(BitmapFactory.decodeResource(getContext().getResources(), resId));
        }
    }
}
