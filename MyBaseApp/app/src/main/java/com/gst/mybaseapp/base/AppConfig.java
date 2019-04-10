package com.gst.mybaseapp.base;

import android.os.Environment;

/**
 * author: GuoSongtao on 2019/4/2 13:51
 * email: 157010607@qq.com
 */
public class AppConfig {

    /***
     * 资源文件路径名
     ***/
    public final static String CityDbName = "YunRichCity.db";// 城市编码数据库名
    public final static String TableName = "YunRichCity2";
    public final static String BasePath = Environment.getExternalStorageDirectory().getAbsolutePath();// SD卡根目录
    public final static String DataBasePath = BasePath + "/myBaseApp";// 数据文件根目录
    public final static String ImagePath = DataBasePath + "/image/";// 图片数据路径
    public final static String NewsImagePath = DataBasePath + "/news/";// 数据路径
    public final static String LogPath = DataBasePath + "/log";// 异常log文件目录
    public final static String ApkPath = DataBasePath + "/download/";// apk下载路径


    public final static String SHARED_PREFERENCES_NAME_NORMAL = "myBaseAppSharedPreferences";// SharedPreferences名字；注意sharedPreferences 尽量别存大数据或大量的数据
    public final static String SP_LOGIN_BUSER_TYPE = "sp_login_buser_type";//登录用户类型

    /**************************网址部分 start*************************/
    public static boolean IS_DEBUG = true;//是否测试，控制日志输出
    //    public static String WEB_HOST="https://pro.baoyjr.com/";
    public static String WEB_HOST = "http://m.baoyjr.com:8888/";
    //    public static String WEB_HOST="http://112.74.240.30:18888/";//富管家sit
    public static String IP = "";
    public static String cityName = "上海市";
    public static String UUId = "00000000-1159-017c-ffff-ffff8f37a0c1";
    public static String channel = "";
    public static String FILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myBaseApp";
    public static String DOWNLOAD_PATH = FILEPATH + "/download/";

    /**************************网址部分 end*************************/

    public static final int flag_BtnClick = 0x1001;// 按钮多次点击
    public static final int flag_noticeImage = 0x1002;// 通知图片
    /***
     * 拍照requestCode
     ***/
    public static final int flag_photo_default = 0x509; // 默认存储文件名
    public static final int flag_takeIdCard_Z = 0x510; // 身份证正面照 requestCode
    public static final int flag_takeIdCard_F = 0x511;// 身份证反面照 requestCode
    public static final int flag_takeIdCard_hold = 0x512;// 个人照片 requestCode
    public static final int flag_takeBankCard_Z = 0x513;// 银行卡正面照 requestCode
    public static final int flag_takeYYZZ = 0x514;// 营业执照 requestCode
    public static final int flag_takeSWDJZ = 0x515;// 税务登记证 requestCode
    public static final int flag_takeJGDMZ = 0x516;// 机构代码证 requestCode
    public static final int flag_takeYYZZ_SD = 0x517;// 营业执照 requestCode
    public static final int flag_takeSWDJZ_SD = 0x518;// 税务登记证 requestCode

    public static final int flag_takeCreditCard_Z = 0x519; // 信用卡正面照 requestCode
    public static final int flag_takeCreditCard_hold = 0x520;// 手持信用卡正面照 requestCode

    public static final int flag_qh_id_card_front = 0x321;//自动识别身份证
    public static final int flag_qh_id_card_back = 0x322;//自动识别身份证
    public static final int flag_qh_id_card_hold = 0x323;//自动识别身份证

}
