package com.gst.envy.otherlibs.base;

import com.tencent.bugly.Bugly;
import com.tencent.tinker.entry.ApplicationLike;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * enableProxyApplication = false 的情况  继承 TinkerApplication
 * 这是Tinker推荐的接入方式，一定程度上会增加接入成本，但具有更好的兼容性。
 */
public class MyApp extends TinkerApplication {

    private ApplicationLike tinkerApplicationLike;


    /**
     * 这个类集成TinkerApplication类，这里面不做任何操作，所有Application的代码都会放到ApplicationLike继承类当中
     * 参数解析
     * 参数1：tinkerFlags 表示Tinker支持的类型 dex only、library only or all suuport，default: TINKER_ENABLE_ALL
     * 参数2：delegateClassName Application代理类 这里填写你自定义的ApplicationLike
     * 参数3：loaderClassName Tinker的加载器，使用默认即可
     * 参数4：tinkerLoadVerifyFlag 加载dex或者lib是否验证md5，默认为false
     */
    public MyApp() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.gst.mybaseapp.base.BuglyTinkerApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    public static MyApp getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initTinkers();
    }


    /**
     * 初始化 tinker
     * PS:初始化的代码建议紧跟 super.onCreate(),并且所有进程都需要初始化，已达到所有进程都可以被 patch 的目的
     */
    private void initTinkers() {
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        Bugly.init(this, "1da114cf26", false);
    }


}
