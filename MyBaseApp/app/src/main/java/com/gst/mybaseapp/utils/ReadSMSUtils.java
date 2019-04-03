package com.gst.mybaseapp.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.gst.mybaseapp.base.MyApp;

/**
 * 作者：GuoSongtao on 2016/9/19 10:26
 * 邮箱: 157010607@qq.com
 */
public class ReadSMSUtils {
    private static SmsObserver observer;
    private static String CODE_BEFORE_STR = "验证码为：";
    private static String CODE_AFTER_STR = "，";
    private static Context myCtx;//记录当前阅读短信组件的 Context

    /**
     * 注册短信监听
     *
     * @param mCtx
     * @param mHandler
     * @param textView
     */
    public static void registSMSRecevier(Context mCtx, Handler mHandler, TextView textView) {
        if (myCtx == null) myCtx = MyApp.getInstance();

        if (observer == null) observer = new SmsObserver(mHandler, textView);

        if (mCtx != null) {
            mCtx.getContentResolver().registerContentObserver(
                    Uri.parse("content://sms"), true, observer);
        }
    }

    /**
     * 取消短信监听注册
     *
     * @param mCtx
     */
    public static void unregistSMSRecevier(Context mCtx) {
        if (mCtx != null && observer != null) {
            observer.textView = null;
            mCtx.getContentResolver().unregisterContentObserver(observer);
        }
    }

    /**
     * content://sms/inbox 收件箱
     * content://sms/sent 已发送
     * content://sms/draft 草稿
     * content://sms/outbox 发件箱
     * content://sms/failed 发送失败
     * content://sms/queued 待发送列表
     */
    private static class SmsObserver extends ContentObserver {
        private TextView textView;//用于显示的TextView

        public SmsObserver(Handler handler, TextView textView) {
            super(handler);
            this.textView = textView;
        }

        @Override
        public void onChange(boolean selfChange) {
            // TODO Auto-generated method stub

            //不需要6.0以前的旧版本不需要检查权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String permission = Manifest.permission.READ_SMS;
                boolean hasCurrPermission = (ContextCompat.checkSelfPermission(myCtx, permission) == PackageManager.PERMISSION_GRANTED);
                if (!hasCurrPermission) {
                    //没有读取短信权限
                    return;
                }
            }

            //查询收件箱中的短信
            Cursor cursor = myCtx.getContentResolver().query(
                    Uri.parse("content://sms/inbox"), null, null, null, "date desc");

            if (cursor == null) {
                super.onChange(selfChange);
                return;
            }

            while (cursor.moveToNext()) {
                String body = cursor.getString(cursor.getColumnIndex("body"));
                StringBuilder sb = new StringBuilder();
                sb.append("address=" + cursor.getString(cursor.getColumnIndex("address")));
                sb.append(", body=" + body);
                sb.append(", date=" + cursor.getString(cursor.getColumnIndex("date")));

                Log.i("Observer", sb.toString());
                if (textView != null && !TextUtils.isEmpty(body)) {
                    if (body.contains(CODE_BEFORE_STR)) {
                        String[] substr = body.split(CODE_BEFORE_STR);
                        String[] result = substr[1].split(CODE_AFTER_STR);
                        if (!TextUtils.isEmpty(result[0])) {
                            textView.setText(result[0]);
                            break;
                        }
                    }
                }
            }
            cursor.close();
            super.onChange(selfChange);
        }
    }
}
