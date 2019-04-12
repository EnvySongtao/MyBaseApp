package com.gst.mybaseapp.net.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.gst.mybaseapp.utils.DownloadUtil;

import java.lang.ref.SoftReference;

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
}
