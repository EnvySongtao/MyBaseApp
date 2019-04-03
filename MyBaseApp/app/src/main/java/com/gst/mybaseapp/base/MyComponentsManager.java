package com.gst.mybaseapp.base;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * author: GuoSongtao on 2019/4/1 14:08
 * email: 157010607@qq.com
 */
public class MyComponentsManager {
    private WeakReference<Activity> sCurrentActivityWeakRef;
    private static final MyComponentsManager ourInstance = new MyComponentsManager();

    public static MyComponentsManager getInstance() {
        return ourInstance;
    }

    private MyComponentsManager() {
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<>(activity);
    }

}
