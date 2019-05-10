package com.gst.mybaseapp.net.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.gst.mybaseapp.R;
import com.gst.mybaseapp.base.AppConfig;
import com.gst.mybaseapp.customView.LoadingView;
import com.gst.mybaseapp.utils.BitmapUtils;
import com.gst.mybaseapp.utils.DownloadUtil;
import com.gst.mybaseapp.utils.ToastUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URLConnection;

/**
 * author: GuoSongtao on 2019/4/12 11:24
 * email: 157010607@qq.com
 */
public class JSProtocolInterface {
    private SoftReference<Context> contextSoftReference;
    private SoftReference<WebView> webViewSoftReference;

    public JSProtocolInterface(Context context, WebView webView) {
        this.contextSoftReference = new SoftReference<>(context);
        this.webViewSoftReference = new SoftReference<>(webView);
    }

    /**
     * 尝试使用应用宝下载app
     *
     * @param url
     * @param pkgName       应用包名，必须在应用宝上过线 不然会报网络异常
     * @param isPackageName 是包名还是url
     */
    @JavascriptInterface
    public void getHuiZhangGuiQrCode(String url, String pkgName, boolean isPackageName) {
        if (isPackageName && !TextUtils.isEmpty(pkgName)) {
            if (contextSoftReference.get() != null) {
                Context context = contextSoftReference.get();
                if (DownloadUtil.isAvilible(context, "com.tencent.android.qqdownloader")) {
                    // 市场存在
                    DownloadUtil.launchAppDetail(context, pkgName, "com.tencent.android.qqdownloader");
                } else {
                    Uri uri = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=" + pkgName);
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(it);
                }
            }
        } else {
            if (webViewSoftReference.get() != null)
                webViewSoftReference.get().loadUrl(url);
        }

    }


    /**
     * 保存二维码到相册
     */
    @JavascriptInterface
    public void saveImg(String picUrl, String saveName) {
        Activity context = null;
        try {
            context = (Activity) contextSoftReference.get();
        } catch (Exception e) {
        }
        if (TextUtils.isEmpty(picUrl) || context == null) return;

        LoadingView.show(context, context, "正在下载二维码……");
        Bitmap bitmap = null;
        try {
            java.net.URL iconUrl = new java.net.URL(picUrl);
            URLConnection connection = iconUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            //关于下面一句, 参考: http://my.oschina.net/u/133352/blog/96582
            connection.setRequestProperty("Accept-Encoding", "identity");
            int length = httpURLConnection.getContentLength();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, length);
            bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            bufferedInputStream.close();
            inputStream.close();
//            File file = Glide.with(context).asFile()
//                    .load(picUrl)
//                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .get();
//            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            if (bitmap != null) {
                BitmapUtils.saveImageToGallery(context, bitmap, saveName);
                ToastUtil.show(saveName + "下载成功");
                MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, saveName + ".jpg", saveName);
            }
        } catch (SecurityException e) {
            //没有存储权限
            ToastUtil.show("没有权限存储");
        } catch (Exception e) {
            ToastUtil.show("下载失败");
            e.printStackTrace();
        } finally {
            if (bitmap != null) bitmap.recycle();
            LoadingView.dismiss();
        }
    }
}
