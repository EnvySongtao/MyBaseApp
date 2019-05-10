package com.gst.mybaseapp.customView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gst.mybaseapp.R;

public class LoadingView {

    private static MyDialog sDialog;
    private static boolean isCanShow = true;
    private static float _alpha = 0.7f;
    private static String viewMessage = "请耐心等待";

    public static void dismiss() {
        if (sDialog != null && sDialog.isShowing()) {
            try {
                if (sDialog.mOwnerActivity != null && !sDialog.mOwnerActivity.isFinishing()) {
                    sDialog.dismiss();
                }
            } catch (Exception ex) {

            }
            sDialog = null;
        }
    }

    public static boolean isShow() {
        if (sDialog != null) {
            return sDialog.isShowing();
        }
        return false;
    }

    public static void show(Context context, Activity ownerActivity) {
        show(context, ownerActivity, context.getString(R.string.loading_view_normal_msg));
    }

    public static void show(Context context, Activity ownerActivity, String message) {
        showInUIThread(context, ownerActivity, message, true);
    }


    public static void showInUIThread(final Context context, final Activity ownerActivity, final String message, final boolean canCancel) {

        if (sDialog != null) {
            dismiss();
        }
        if (!isCanShow) {
            return;
        }

        if (context == null || ownerActivity == null || (ownerActivity != null && ownerActivity.isFinishing())) {
            return;
        }

        viewMessage = message;
        ownerActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sDialog = new MyDialog(context, ownerActivity, message);
                Window window = sDialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.alpha = _alpha;
                window.setAttributes(lp);
                sDialog.setCancelable(canCancel);
                sDialog.setCanceledOnTouchOutside(false);
                sDialog.show();
            }
        });
    }

    /**
     * 设置是否可以取消LoadingView
     *
     * @param context
     * @param ownerActivity
     * @param message
     * @param canCancel
     */
    public static void show(Context context, Activity ownerActivity, String message, boolean canCancel) {
        showInUIThread(context, ownerActivity, message, canCancel);
    }


    public static void show(Context context, Activity ownerActivity, int resourceID) {
        String message = "";
        try {
            message = context.getString(resourceID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        show(context, ownerActivity, message);
    }

    public static void showAlpha(Context context, Activity ownerActivity, float alpha) {
        _alpha = alpha;
        show(context, ownerActivity);
    }

    public static void setCanShow(boolean is) {
        isCanShow = is;
    }

    public static String getViewMessage() {
        return viewMessage;
    }


    /**
     * dialog
     */
    static class MyDialog extends Dialog {
        TextView text;
        LoadingViewRing lvr_show;
        String content;
        Activity mOwnerActivity;

        public MyDialog(Context context, Activity ownerActivity, String content) {
            super(context, R.style.AppTheme_Dialog);
            this.mOwnerActivity = ownerActivity;
            this.content = content;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_progress_bar);
            text = (TextView) findViewById(R.id.text);
            lvr_show = (LoadingViewRing) findViewById(R.id.lvr_show);
            text.setText(content);
        }

        @Override
        public void show() {
            super.show();
            lvr_show.startAnim();
        }

        @Override
        public void dismiss() {
            lvr_show.stopAnim();
            super.dismiss();
        }
    }

}
