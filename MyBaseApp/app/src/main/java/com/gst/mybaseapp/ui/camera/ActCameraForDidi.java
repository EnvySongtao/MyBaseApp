package com.gst.mybaseapp.ui.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;
import com.gst.mybaseapp.R;
import com.gst.mybaseapp.base.AppConfig;
import com.gst.mybaseapp.base.BaseActivity;
import com.gst.mybaseapp.customView.LoadingView;
import com.gst.mybaseapp.utils.BitmapUtils;
import com.gst.mybaseapp.utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.List;

import me.jessyan.autosize.utils.LogUtils;


/**
 * Created by Yanjun.Lu on 2018/4/11.
 */
public class ActCameraForDidi extends BaseActivity implements
        AspectRatioFragment.Listener, View.OnClickListener {
    //图片验证成功（主要是调接口对身份证正反面的验证） 手持会直接传验证成功
    protected static final int VERIFY_PHOTO_SUCC = 0x001;
    protected static final int TAKE_PICTURE = 0x002;
    protected static final int TAKE_PICTURE_ERROR = 0x003;

    private static final int GET_CUT_PHOTO = 0x11;//得到剪切图
    //private static final int GET_ID_NUMBER_SUCC = 0x12;//获取身份证号成功
    //private static final int GET_ID_NUMBER_FAIL = 0x13;//获取身份证号成功

    protected RelativeLayout mRlContent;
    //protected TextView mWhichPhotoTv, mPhotoIntroTv;
    protected Button mRetakePhotoBtn, mNextBtn;
    private View mTakePhotoBtn, mCameraChangeBtn;
    protected CameraView mCameraView;
    protected CameraMaskView mCameraMaskView;

    private int maskFlag = AppConfig.flag_takeCreditCard_Z;
    private int requestCode;
    private String photoPath, whichPicture, tip;
    private boolean isFrontCamera = false;

    protected Handler mBackgroundHandler;
    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case VERIFY_PHOTO_SUCC:
                    showNext();
                    try {
                        mCameraMaskView.showPhoto((String) msg.obj);
                        getBackgroundHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                System.gc();
                            }
                        });
                    } catch (OutOfMemoryError e) {
                        Runtime.getRuntime().gc();
                    } catch (Throwable e) {

                    }
                    break;

                case TAKE_PICTURE:
                    LoadingView.dismiss();
                    mTakePhotoBtn.setClickable(true);
                    if (AppConfig.flag_takeCreditCard_Z == maskFlag) {
                        LoadingView.show(ActCameraForDidi.this, ActCameraForDidi.this, "正在裁剪图片......");
                    }
                    break;
                case TAKE_PICTURE_ERROR:
                    LoadingView.dismiss();
                    showTakePhoto();
                    break;


                case GET_CUT_PHOTO:
                    LoadingView.dismiss();
                    sendVerifyPhotoSucc(photoPath);
                    LogUtils.d("IDFront:裁剪完成： " + photoPath);
                    showTakePhoto();
                    break;
            }
            return false;
        }
    });

    /**
     * 拍照
     *
     * @param photoPath
     */
    public static void actionStart(AppCompatActivity activity, int requestCode, String photoPath, int maskFlag) {//, String whichPic, String picTip
        if (activity == null) return;
        Intent intent = new Intent(activity, ActCameraForDidi.class);
        intent.putExtra("photoPath", photoPath);
        intent.putExtra("maskFlag", maskFlag);
//        intent.putExtra("whichPicture", whichPic);
//        intent.putExtra("tip", picTip);
        intent.putExtra("requestCode", requestCode);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_camera_for_didi);

        initView();
        initAction();
        //initLogicView();
        showTakePhoto();
    }

    private void initView() {
        mRlContent = (RelativeLayout) findViewById(R.id.rl_content);
        mTakePhotoBtn = findViewById(R.id.btn_take_photo);
        mCameraChangeBtn = findViewById(R.id.btn_camera_change);
        mRetakePhotoBtn = (Button) findViewById(R.id.btn_re_take_photo);
        mNextBtn = (Button) findViewById(R.id.btn_next);
        mCameraView = (CameraView) findViewById(R.id.camera);
        mCameraMaskView = (CameraMaskView) findViewById(R.id.cmv_cut);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }

        mNextBtn.setText("确认");

        if (maskFlag == AppConfig.flag_takeCreditCard_hold || maskFlag == AppConfig.flag_takeIdCard_hold) {
            mCameraChangeBtn.setVisibility(View.VISIBLE);
        } else {
            mCameraChangeBtn.setVisibility(View.GONE);
        }

        Intent getIntent = getIntent();
        if (getIntent != null) {
            photoPath = getIntent.getStringExtra("photoPath");
            //this.maskFlag = getIntent.getIntExtra("maskFlag", Constant.flag_takeCreditCard_Z);
            this.whichPicture = getIntent.getStringExtra("whichPicture");
            tip = getIntent.getStringExtra("tip");
            requestCode = getIntent.getIntExtra("requestCode", 0);
            maskFlag = getIntent.getIntExtra("maskFlag", AppConfig.flag_takeCreditCard_Z);
        }

        mCameraMaskView.setMaskFlag(maskFlag);
    }

    protected void initAction() {
        mTakePhotoBtn.setOnClickListener(this);
        mRetakePhotoBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mCameraChangeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_take_photo:
                if (mCameraView != null) {
                    mTakePhotoBtn.setClickable(false);
                    try {
                        LoadingView.show(this, this, "正在拍照......");
                        mCameraView.takePicture();
                    } catch (Exception e) {
                        mTakePhotoBtn.setClickable(true);
                        LoadingView.dismiss();
//                        ScreenUtils.showPermissionDialog(this, getString(R.string.no_permission_camera));
                        ToastUtil.show(getString(R.string.no_permission_camera));
                    }
                }
                break;
            case R.id.btn_re_take_photo:
                showTakePhoto();
                break;
            case R.id.btn_next:
                mNextBtn.setClickable(false);
                doCompleted();
                break;
            case R.id.btn_camera_change:
                //更换摄像头
                if (mCameraView != null) {
                    int facing = mCameraView.getFacing();
                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                            CameraView.FACING_BACK : CameraView.FACING_FRONT);
                    isFrontCamera = (mCameraView.getFacing() == CameraView.FACING_FRONT);
                }
                break;
        }
    }

    //设置不同页面textView文字
    protected void initLogicView() {

    }

    //点击完成
    protected void doCompleted() {
        setResult(RESULT_OK, getIntent());
        finish();
    }

    //拍完照片后执行
    protected void onPhotoTaken(CameraView cameraView, final byte[] data) {
        LogUtils.d("IDFront拍到照片: " + data.length);
        getBackgroundHandler().post(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(photoPath)) {
                    photoPath = AppConfig.ImagePath + mUser.getCustid() + AppConfig.flag_photo_default + ".jpg";
                }

                mCameraMaskView.setNeedMask(true);
                File file = new File(photoPath);
                String filePath = "";
                OutputStream os = null;
                try {
                    os = new FileOutputStream(file);
                    os.write(data);
                    os.close();
                    filePath = file.getAbsolutePath();
                    sendMessage(mHandler, TAKE_PICTURE, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    ToastUtil.show("拍摄异常，请确认是否有文件读写权限");
                    sendMessage(mHandler, TAKE_PICTURE_ERROR, null);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtil.show("拍摄异常，请重新拍照");
                    sendMessage(mHandler, TAKE_PICTURE_ERROR, null);
                    return;
                } finally {
                }


                try {
                    if (maskFlag == AppConfig.flag_takeCreditCard_Z || maskFlag == AppConfig.flag_takeIdCard_F
                            || maskFlag == AppConfig.flag_takeBankCard_Z || maskFlag == AppConfig.flag_takeIdCard_Z) {
                        filePath = cutBitmap(file);
                    } else if (maskFlag == AppConfig.flag_photo_default || maskFlag == AppConfig.flag_takeIdCard_hold
                            || maskFlag == AppConfig.flag_takeCreditCard_hold) {
                        filePath = cutBitmapNoMask(file);
                    }
                } catch (OutOfMemoryError e) {
                    LogUtils.d("IDFront:OutOfMemoryError " + file);
                    Runtime.getRuntime().gc();
                    mCameraMaskView.setNeedMask(false);
                    ToastUtil.show("裁剪照片失败，使用原图");
                } catch (Throwable throwable) {
                    LogUtils.d("IDFront:Cannot write to " + file);
                    throwable.printStackTrace();
                    mCameraMaskView.setNeedMask(false);
                    ToastUtil.show("裁剪照片失败，使用原图");
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            // Ignore
                        }
                    }
                    sendMessage(mHandler, GET_CUT_PHOTO, filePath);
                }
            }
        });
    }

    @Override
    public void onAspectRatioSelected(@NonNull AspectRatio ratio) {
        if (mCameraView != null) {
            Toast.makeText(this, ratio.toString(), Toast.LENGTH_SHORT).show();
            mCameraView.setAspectRatio(ratio);
        }
    }

    protected Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    protected CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            LogUtils.d("onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            LogUtils.d("onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            mTakePhotoBtn.setEnabled(true);
            if (ActivityCompat.checkSelfPermission(ActCameraForDidi.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                LoadingView.dismiss();
                ToastUtil.show(getString(R.string.no_permission_write_external_storage_photo));
//                ScreenUtils.showSingleAlertDialog(ActCameraForDidi.this, getString(R.string.no_permission_write_external_storage_photo));
                return;
            }
            //防止第一次使用  java.io.FileNotFoundException
            File dir = new File(AppConfig.ImagePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            onPhotoTaken(cameraView, data);
        }

    };

//
//    /**
//     * 先裁剪 后旋转
//     *
//     * @param file
//     * @throws Throwable
//     */
//    protected String cutBitmap(File file) throws Throwable {
//        int ori = ImageUtils.readPictureDegree(file.getAbsolutePath());
//        LogUtils.d("mCameraView.getDisplayOrientation() = " + ori);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = false;
//        //旋转前先压缩一下 否则旋转会OOM，当然不旋转没问题
//        SoftReference<Bitmap> softBmOld = new SoftReference<>(BitmapFactory.decodeFile(file.getAbsolutePath(), options));
//        Bitmap bitmapOld = softBmOld.get();
//        //截取后图片宽度为屏幕的3/4  宽高比是760:500
//        CameraMaskView.Frame frame = mCameraMaskView.getmFrame();
//        int startX = (int) (options.outWidth * 1.0 / 10);
//        int bWidth = (int) (options.outWidth * 4.0 / 5);
//        int bHeight = (int) (bWidth * 500.0 / 760);
//        int startY = (int) (options.outHeight * frame.marginTop * 1.0 / mCameraMaskView.getHeight());
//        if (ori == 90) {//需要旋转顺时针90度
//            bHeight = (int) (options.outHeight * 4.0 / 5);
//            bWidth = (int) (bHeight * 500.0 / 760);
//            startX = (int) (options.outWidth * frame.marginTop * 1.0 / mCameraMaskView.getHeight());
//            startY = (int) (options.outHeight * 1.0 / 10);
//        } else if (ori == 180) {
//            bWidth = (int) (options.outWidth * 4.0 / 5);
//            bHeight = (int) (bWidth * 500.0 / 760);
//            startX = (int) (options.outWidth * 1.0 / 10);
//            startY = (int) (options.outHeight - bHeight - options.outHeight * frame.marginTop * 1.0 / mCameraMaskView.getHeight());
//        } else if (ori == 270) {
//            bHeight = (int) (options.outHeight * 4.0 / 5);
//            bWidth = (int) (bHeight * 500.0 / 760);
//            startX = (int) (options.outWidth - bHeight - options.outWidth * frame.marginTop * 1.0 / mCameraMaskView.getHeight());
//            startY = (int) (options.outHeight * 1.0 / 10);
//        }
//
//        SoftReference<Bitmap> softCut = new SoftReference<>(Bitmap.createBitmap(bitmapOld, startX, startY, bWidth, bHeight));
//        ImageUtils.saveBitmap(softCut.get(), file, 70);
//        if (!bitmapOld.isRecycled()) {
//            bitmapOld.recycle();
//        }
//        if (softCut != null && softCut.get() != null && !softCut.get().isRecycled()) {
//            softCut.get().recycle();
//        }
//
//        softBmOld = new SoftReference<>(BitmapUtils.getFitSampleBitmap(file.getAbsolutePath()));
//        Bitmap bitmapNew = softBmOld.get();
//        if (ori != 0 && bitmapNew != null) {
//            bitmapNew = ImageUtils.rotateBitmapByDegree(bitmapNew, ori);
//        }
//        int quailty = 100;
//        if (bitmapNew.getWidth() > 744) quailty = 80;
//        else if (bitmapNew.getWidth() > 512) quailty = 90;
//        ImageUtils.saveBitmap(bitmapNew, file, quailty);
//
//        if (bitmapNew != null && !bitmapNew.isRecycled()) {
//            bitmapNew.recycle();
//        }
//        return file.getAbsolutePath();
//    }

//    /**
//     * 裁剪图片
//     *
//     * @param file
//     * @return 文件路径
//     * @throws Throwable
//     */
//    protected String cutBitmap(File file) throws Throwable {
//        int ori = ImageUtils.readPictureDegree(file.getAbsolutePath());
//        com.qh.qhpay.util.LogUtils.d("mCameraView.getDisplayOrientation() = " + ori);
////        BitmapFactory.Options options = new BitmapFactory.Options();
////        options.inJustDecodeBounds = true;//仅边界
//
//        //旋转前先压缩一下 否则旋转会OOM，当然不旋转没问题
//        SoftReference<Bitmap> softBmOld = new SoftReference<>(BitmapUtils.getFitSampleBitmap(file.getAbsolutePath()));
//        Bitmap bitmapOld = softBmOld.get();
//        if (ori != 0 && bitmapOld != null) {
//            bitmapOld = ImageUtils.rotateBitmapByDegree(bitmapOld, ori);
//        }
//        //截取后图片宽度为屏幕的3/4  宽高比是760:500
//        CameraMaskView.Frame frame = mCameraMaskView.getmFrame();
//        int startX = (int) (bitmapOld.getWidth() * 1.0 / 10);
//        int bWidth = (int) (bitmapOld.getWidth() * 4.0 / 5);
//        int bHeight = (int) (bWidth * 500.0 / 760);
////        int startY = (bitmapOld.getHeight() - bHeight) / 2;
//        int startY = (int) (bitmapOld.getHeight() * frame.marginTop * 1.0 / mCameraMaskView.getHeight());
//
//        SoftReference<Bitmap> bitmapNew = new SoftReference<>(Bitmap.createBitmap(bitmapOld, startX, startY, bWidth, bHeight));
//
//        if (!bitmapOld.isRecycled()) {
//            bitmapOld.recycle();
//        }
//        int quailty = 100;
//        if (bitmapNew != null && bitmapNew.get() != null) {
//            if (bitmapNew.get().getWidth() > 744) quailty = 80;
//            else if (bitmapNew.get().getWidth() > 512) quailty = 90;
//        }
//        ImageUtils.saveBitmap(bitmapNew.get(), file, quailty);
//
//        if (bitmapNew != null && !bitmapNew.get().isRecycled()) {
//            bitmapNew.get().recycle();
//        }
//        return file.getAbsolutePath();
//    }

    /**
     * 裁剪图片
     *
     * @param file
     * @return 文件路径
     * @throws Throwable
     */
    protected String cutBitmap(File file) throws Throwable {
        int ori = BitmapUtils.readPictureDegree(file.getAbsolutePath());
        LogUtils.d("mCameraView.getDisplayOrientation() = " + ori);

        //旋转前先压缩一下 否则旋转会OOM，当然不旋转没问题
        SoftReference<Bitmap> softBmOld = new SoftReference<>(BitmapUtils.getFitSampleBitmap(file.getAbsolutePath()));
        Bitmap bitmapOld = softBmOld.get();
        if (ori != 0 && bitmapOld != null) {
            bitmapOld = BitmapUtils.rotateBitmapByDegree(bitmapOld, ori);
        }
        //截取后图片宽度为屏幕的3/4  宽高比是760:500
        CameraMaskView.Frame frame = mCameraMaskView.getmFrame();
        int startX, startY;//屏幕上图片截取的左上点
        int bWidth, bHeight;//屏幕上图片截取 宽和高
        float bitmapHeight, bitmapWidth;//出现在屏幕上的图片高和宽
        if (mRlContent.getWidth() * 1F / mRlContent.getHeight() > 3 / 4F) {
            //屏幕高宽比大于3/4 图片底部有部分没显示
            bitmapWidth = bitmapOld.getWidth();//图片宽占满了全屏
            bitmapHeight = (int) (bitmapWidth * mRlContent.getHeight() * 1F / mRlContent.getWidth());
        } else {
            //屏幕宽高比小于3/4 图片右边有部分没显示
            bitmapHeight = bitmapOld.getHeight();//图片高占满了全屏
            bitmapWidth = (int) (bitmapHeight * mRlContent.getWidth() * 1F / mRlContent.getHeight());//图片高占满了全屏
        }
        bWidth = (int) (bitmapWidth * 4.0 / 5);//高度不正确
        startX = (int) (bitmapWidth * 1.0 / 10);//正确
        bHeight = (int) (bitmapHeight * (mRlContent.getHeight() - 2 * frame.marginTop) * 1F / mRlContent.getHeight());
        startY = (int) (bitmapHeight * frame.marginTop * 1F / mRlContent.getHeight());//正确

        //横屏模式
        //bHeight = (int) (bitmapHeight * 4.0 / 5);//高度不正确
        //bWidth = (int) (bitmapHeight * (mRlContent.getWidth() - 2 * frame.widthSide) * 1F / mRlContent.getWidth());
        //startY = (int) (bitmapHeight * 1.0 / 10);//正确
        //startX= (int) (bitmapHeight * frame.widthSide * 1.0 / mRlContent.getWidth());

        SoftReference<Bitmap> bitmapNew = new SoftReference<>(Bitmap.createBitmap(bitmapOld, startX, startY, bWidth, bHeight));

        if (!bitmapOld.isRecycled()) {
            bitmapOld.recycle();
        }
        int quailty = 100;
        if (bitmapNew != null && bitmapNew.get() != null) {
            if (bitmapNew.get().getWidth() > 744) quailty = 80;
            else if (bitmapNew.get().getWidth() > 512) quailty = 90;
        }
        BitmapUtils.saveBitmap(bitmapNew.get(), file, quailty);

        if (bitmapNew != null && !bitmapNew.get().isRecycled()) {
            bitmapNew.get().recycle();
        }
        return file.getAbsolutePath();
    }

    /**
     * 裁剪图片
     *
     * @param file
     * @return 文件路径
     * @throws Throwable
     */
    protected String cutBitmapNoMask(File file) throws Throwable {
        int ori = BitmapUtils.readPictureDegree(file.getAbsolutePath());
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;//仅边界

        //旋转前先压缩一下 否则旋转会OOM，当然不旋转没问题
        SoftReference<Bitmap> softBmOld = new SoftReference<>(BitmapUtils.getFitSampleBitmap(file.getAbsolutePath()));
        Bitmap bitmapOld = softBmOld.get();
        if (ori != 0 && bitmapOld != null) {
            bitmapOld = BitmapUtils.rotateBitmapByDegree(bitmapOld, ori);
        }
        //截取后图片宽度为屏幕的3/4  宽高比是760:500
        CameraMaskView.Frame frame = mCameraMaskView.getmFrame();
        int startX = 0;
        int startY = 0;
        int bWidth = bitmapOld.getWidth(), bHeight = bitmapOld.getHeight();
        LogUtils.d("mCameraView=(" + mCameraView.getWidth() + "," + mCameraView.getWidth() +
                ") bitmapOld=(" + bWidth + "," + bHeight + ")");
        //裁剪屏幕这样的大小
        float height2width = mRlContent.getHeight() * 1F / mRlContent.getWidth();
        if (!isFrontCamera) {
            if (height2width > 4 / 3F) {
                //屏幕（展示view）高宽比大于4/3  图片右边没显示出来
                bHeight = bitmapOld.getHeight();
                bWidth = (int) (bHeight / height2width);
            } else {
                //屏幕（展示view）宽高比大于3/4 图片下边没显示出来
                bWidth = bitmapOld.getWidth();
                bHeight = (int) (bWidth * height2width);
            }
        } else {
            //注意前置摄像头 拍出照片与mCameraView展示的 会左右点到
            if (height2width > 4 / 3F) {
                //屏幕（展示view）高宽比大于4/3 图片右边没显示出来
                startX = (int) ((3F / 4 - 1 / height2width) * bitmapOld.getWidth());
                bHeight = bitmapOld.getHeight();
                bWidth = (bitmapOld.getWidth() - startX);
            } else {
                //屏幕（展示view）宽高比大于3/4  图片下边没显示出来
                bWidth = bitmapOld.getWidth();
                bHeight = (int) (bWidth * height2width);
            }
        }

        SoftReference<Bitmap> bitmapNew = new SoftReference<>(Bitmap.createBitmap(bitmapOld, startX, startY, bWidth, bHeight));

        if (!bitmapOld.isRecycled()) {
            bitmapOld.recycle();
        }
        int quailty = 100;
        if (bitmapNew != null && bitmapNew.get() != null) {
            if (bitmapNew.get().getWidth() > 744) quailty = 80;
            else if (bitmapNew.get().getWidth() > 512) quailty = 90;
        }
        BitmapUtils.saveBitmap(bitmapNew.get(), file, quailty);

        if (bitmapNew != null && !bitmapNew.get().isRecycled()) {
            bitmapNew.get().recycle();
        }
        return file.getAbsolutePath();
    }


    //当图片验证成功会发送消息
    protected void sendVerifyPhotoSucc(String filePath) {
        Message msg = Message.obtain();
        msg.obj = filePath;
        msg.what = VERIFY_PHOTO_SUCC;
        mHandler.sendMessage(msg);
    }


    public void showTakePhoto() {
        mTakePhotoBtn.setVisibility(View.VISIBLE);
        mTakePhotoBtn.setClickable(true);
        mRetakePhotoBtn.setVisibility(View.GONE);
        mNextBtn.setVisibility(View.GONE);
        mCameraMaskView.clearPhoto();
        if (maskFlag == AppConfig.flag_takeCreditCard_hold || maskFlag == AppConfig.flag_takeIdCard_hold) {
            mCameraChangeBtn.setVisibility(View.VISIBLE);
        } else {
            mCameraChangeBtn.setVisibility(View.GONE);
        }
    }

    public void showNext() {
        mTakePhotoBtn.setVisibility(View.GONE);
        mCameraChangeBtn.setVisibility(View.GONE);
        mRetakePhotoBtn.setVisibility(View.VISIBLE);
        mNextBtn.setVisibility(View.VISIBLE);
        mTakePhotoBtn.setClickable(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mCameraView.start();
        }
    }

    @Override
    public void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    /*************2017-10-30 申请权限 start ****************/
    @Override
    protected void initCurentActNeedPermissionsAll() {
        super.initCurentActNeedPermissionsAll();
        actNeedPermissionsAll.clear();
        actNeedPermissionsAll.add(Manifest.permission.CAMERA);
        actNeedPermissionsAll.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    protected void onDeniedPermissionsResult(List<String> deniedPermissions, int requestCode) {

        String msg = "";
        if (deniedPermissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            msg = getString(R.string.no_permission_write_external_storage_photo);
        }
        if (deniedPermissions.contains(Manifest.permission.CAMERA)) {
            msg = getString(R.string.no_permission_camera);
        }
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        ToastUtil.show(msg);
    }
    /*************2017-10-30 申请权限 end ****************/

}
