package com.gst.mybaseapp.utils;

import android.app.Application;

import com.gst.mybaseapp.been.ShortUrl;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.BoxStoreBuilder;

/**
 * 数据库操作 会用到IOC和类的类型，操作十分复杂，所以无法像图片一样 封装数据库工具类 建议用接口实现需要存储数据的存储
 * author: GuoSongtao on 2019/4/8 16:16
 * email: 157010607@qq.com
 */
public class DataBaseUtil {
    private static BoxStore mBoxStore;

    public static void initDatabase(Application application) {
        mBoxStore = BoxStoreBuilder.createDebugWithoutModel().androidContext(application).build();
    }

    public static BoxStore getBoxStore() {
        return mBoxStore;
    }


}
