package com.gst.mybaseapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * https://blog.csdn.net/qq_31344287/article/details/52399812
 * author: GuoSongtao on 2019/4/12 10:45
 * email: 157010607@qq.com
 */
public class DownloadUtil {
    public static final String QQ_APP_STORE = "com.tencent.android.qqdownloader"; //腾讯应用宝
    public static final String APP_STORE_360 = "com.qihoo.appstore"; // 360手机助手
    public static final String BAIDU_APP_STORE = "com.baidu.appsearch"; // com.baidu.appsearch 百度手机助手
    public static final String XIAOMI_APP_STORE = "com.xiaomi.market"; // com.xiaomi.market 小米应用商店
    public static final String HUAWEI_APP_STORE = "com.huawei.appmarket"; //  com.huawei.appmarket 华为应用商店
    public static final String LENOVO_APP_STORE = "com.lenovo.leos.appstore"; //  com.lenovo.leos.appstore 联想应用商店
    public static final String ZTE_APP_STORE = "zte.com.market"; //  zte.com.market 中兴应用商店
    public static final String OPPO_APP_STORE = "com.oppo.market"; //   com.oppo.market OPPO应用商店
    public static final String WANDOUJIA_APP_STORE = "com.wandoujia.phoenix2"; //  com.wandoujia.phoenix2 豌豆荚
    public static final String APP_STORE_91 = "com.dragon.android.pandaspace"; //  com.dragon.android.pandaspace 91手机助手
    public static final String QQ_MANAGER_APP_STORE = "com.tencent.qqpimsecure"; //  com.tencent.qqpimsecure QQ手机管家
    public static final String PP_APP_STORE = "com.pp.assistant"; //   com.pp.assistant PP手机助手
    public static final String ANZHI_APP_STORE = "com.hiapk.marketpho"; //  com.hiapk.marketpho 安智应用商店
    public static final String YINGYONGHUI_APP_STORE = "com.yingyonghui.market"; //  com.yingyonghui.market 应用汇
    public static final String JIFENG_APP_STORE = "com.mappn.gfan"; //  com.mappn.gfan 机锋应用市场
    public static final String GO_APP_STORE = "cn.goapk.market"; //  cn.goapk.market GO市场
    public static final String YULONG_APP_STORE = "com.yulong.android.coolmart"; //  com.yulong.android.coolmart 宇龙Coolpad应用商店
    public static final String COOL_APP_STORE = "com.coolapk.market"; // com.coolapk.market cool市场

    /******已上架的应用市场*****/
    public static final String[] app_Markets = {QQ_APP_STORE, APP_STORE_360, BAIDU_APP_STORE, XIAOMI_APP_STORE, HUAWEI_APP_STORE, LENOVO_APP_STORE, OPPO_APP_STORE};

    public static void tryGoToYingYongBao(Context context, String packageName) {
        if (isAvilible(context, "com.tencent.android.qqdownloader")) {
            // 市场存在
            launchAppDetail(context, packageName, "com.tencent.android.qqdownloader");
        } else {
            Uri uri = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=" + packageName);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        }
    }

    /**
     * 启动到app详情界面
     *
     * @param appPkg    App的包名
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     *                       
     */
    public static void launchAppDetail(Context context, String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg))
                return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg))
                intent.setPackage(marketPkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 判断市场是否存在的方法

    /**
     * 判断市场是否存在的方法
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();// 用于存储所有已安装程序的包名
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }
}
