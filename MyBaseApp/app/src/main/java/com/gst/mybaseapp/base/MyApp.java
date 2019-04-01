package com.gst.mybaseapp.base;

import android.app.Application;

public class MyApp extends Application {

    private static MyApp ourInstance=null;
    public static MyApp getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance=MyApp.this;
    }
}
