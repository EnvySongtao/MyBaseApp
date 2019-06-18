package com.gst.mybaseapp.utils;

import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.SoftReference;

/**
 * 防内存泄漏的 handler
 * author: GuoSongtao on 2019/5/9 11:52
 * email: 157010607@qq.com
 */
public class PreventLeakHandler extends Handler {
    private SoftReference<AppCompatActivity> activitySoftReference = null;

    public PreventLeakHandler() {
    }

    public void initContext(SoftReference<AppCompatActivity> activitySoftReference) {
        this.activitySoftReference = activitySoftReference;
    }

    @Override
    public void handleMessage(Message msg) {
        if (activitySoftReference.get() == null || activitySoftReference.get().isFinishing())
            return;
        super.handleMessage(msg);
    }

    @Override
    public void dispatchMessage(Message msg) {
        if (activitySoftReference.get() == null || activitySoftReference.get().isFinishing())
            return;

        super.dispatchMessage(msg);
    }
}
