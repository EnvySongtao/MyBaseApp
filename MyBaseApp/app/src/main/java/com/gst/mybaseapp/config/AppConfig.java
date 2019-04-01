package com.gst.mybaseapp.config;

import android.os.Environment;

public class AppConfig {
    /**
     * 是否测试，控制日志输出
     */
    public static boolean IS_DEBUG=true;
//    public static String WEB_HOST="https://pro.baoyjr.com/";
    public static String WEB_HOST="http://m.baoyjr.com:8888/";
//    public static String WEB_HOST="http://112.74.240.30:18888/";//富管家sit
    public static String IP="";
    public static String cityName="上海市";
    public static String UUId="00000000-1159-017c-ffff-ffff8f37a0c1";
    public static String channel="";
    public static String FILEPATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/myBaseApp";
    public static String DOWNLOAD_PATH= FILEPATH+"/download/";



}
