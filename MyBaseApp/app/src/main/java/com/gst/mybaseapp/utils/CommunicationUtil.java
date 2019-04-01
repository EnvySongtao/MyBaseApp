package com.gst.mybaseapp.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.widget.Toast;

import com.gst.mybaseapp.config.AppConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CommunicationUtil {

    private CommunicationUtil() {
    }

    /************************蓝牙 操作 start ************************/
    /**
     * 通过蓝牙发送文件
     */
    private void sendFile(Activity activity,String name) {
        PackageManager localPackageManager = activity.getPackageManager();
        Intent localIntent = null;

        HashMap<String, ActivityInfo> localHashMap = null;

        try {
            localIntent = new Intent();
            localIntent.setAction(Intent.ACTION_SEND);
            File file = new File(AppConfig.DOWNLOAD_PATH,name);
            localIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            // localIntent.putExtra(Intent.EXTRA_STREAM,
            // Uri.fromFile(new File(localApplicationInfo.sourceDir)));
            localIntent.setType("*/*");
            List<ResolveInfo> localList = localPackageManager.queryIntentActivities(
                    localIntent, 0);
            localHashMap = new HashMap<String, ActivityInfo>();
            Iterator<ResolveInfo> localIterator1 = localList.iterator();
            while (localIterator1.hasNext()) {
                ResolveInfo resolveInfo = (ResolveInfo) localIterator1.next();
                ActivityInfo localActivityInfo2 = resolveInfo.activityInfo;
                String str = localActivityInfo2.applicationInfo.processName;
                if (str.contains("bluetooth"))
                    localHashMap.put(str, localActivityInfo2);
            }
        } catch (Exception localException) {
            Toast.makeText(activity,"蓝牙不支持错误!",Toast.LENGTH_SHORT).show();
        }
        if (localHashMap.size() == 0)
            Toast.makeText(activity,"蓝牙不支持错误!",Toast.LENGTH_SHORT).show();
        ActivityInfo localActivityInfo1 = (ActivityInfo) localHashMap
                .get("com.android.bluetooth");
        if (localActivityInfo1 == null) {
            localActivityInfo1 = (ActivityInfo) localHashMap
                    .get("com.mediatek.bluetooth");
        }
        if (localActivityInfo1 == null) {
            Iterator<ActivityInfo> localIterator2 = localHashMap.values().iterator();
            if (localIterator2.hasNext())
                localActivityInfo1 = (ActivityInfo) localIterator2.next();
        }
        if (localActivityInfo1 != null) {
            localIntent.setComponent(new ComponentName(
                    localActivityInfo1.packageName, localActivityInfo1.name));
            activity.startActivityForResult(localIntent, 4098);
            return;
        }
        Toast.makeText(activity,"蓝牙不支持错误!",Toast.LENGTH_SHORT).show();
    }
    /************************蓝牙 操作 end ************************/
}
