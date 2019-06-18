package com.gst.mybaseapp.base;


import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.multidex.MultiDex;
import android.widget.Toast;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.entry.DefaultApplicationLike;

import java.util.Locale;

/**
 * 自定义ApplicationLike 在MyApp中被引用
 *
 * enableProxyApplication = false 的情况
 * 这是Tinker推荐的接入方式，一定程度上会增加接入成本，但具有更好的兼容性。
 *
 * 注意：tinker需要你开启MultiDex,你需要在dependencies中进行配置compile "com.android.support:multidex:1.0.1"才可以使用MultiDex.install方法；
 * SampleApplicationLike这个类是Application的代理类，以前所有在Application的实现必须要全部拷贝到这里，在onCreate方法调用SDK的初始化方法，
 * 在onBaseContextAttached中调用Beta.installTinker(this);。
 *
 * 1,tinker升级到1.9.9之后，ApplicationLike和ApplicationLifeCycle这两个类的包名要改成com.tencent.tinker.entry
 * 2,你用了d8和java7以上的新特性？（lambda、try with resource之类的），这样的话就会打出037格式的dex，tinker暂时还不支持这种新格式的dex哇
 *
 * author: GuoSongtao on 2019/4/17 15:21
 * email: 157010607@qq.com
 */
public class BuglyTinkerApplicationLike  extends DefaultApplicationLike {

    public static final String TAG = "Tinker.SampleApplicationLike";

    public BuglyTinkerApplicationLike(Application application, int tinkerFlags,
                                 boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                                 long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
                applicationStartMillisTime, tinkerResultIntent);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁，默认为true
        Beta.canAutoDownloadPatch = true;
        // 设置是否自动合成补丁，默认为true
        Beta.canAutoPatch = true;
        // 设置是否提示用户重启，默认为false
        Beta.canNotifyUserRestart = true;
        // 补丁回调接口
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFile) {
                Toast.makeText(getApplication(), "补丁下载地址" + patchFile, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
                Toast.makeText(getApplication(),
                        String.format(Locale.getDefault(), "%s %d%%",
                                Beta.strNotificationDownloading,
                                (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadSuccess(String msg) {
                Toast.makeText(getApplication(), "补丁下载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadFailure(String msg) {
                Toast.makeText(getApplication(), "补丁下载失败", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onApplySuccess(String msg) {
                Toast.makeText(getApplication(), "补丁应用成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApplyFailure(String msg) {
                Toast.makeText(getApplication(), "补丁应用失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPatchRollback() {

            }
        };

        // 设置开发设备，默认为false，上传补丁如果下发范围指定为“开发设备”，需要调用此接口来标识开发设备
        Bugly.setIsDevelopmentDevice(getApplication(), true);
//        Bugly.setIsDevelopmentDevice(getApplication(), true);
        // 多渠道需求塞入
        // String channel = WalleChannelReader.getChannel(getApplication());
        // Bugly.setAppChannel(getApplication(), channel);
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
//        Bugly.init(getApplication(), "e0b1ba785f", true);
        Bugly.init(getApplication(), "1da114cf26", false);

        //bug错误日志抓取
        CrashReport.initCrashReport(getApplication(), "1da114cf26", false);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // TODO: 安装tinker
        Beta.installTinker(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(
            Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Beta.unInit();
    }
}
