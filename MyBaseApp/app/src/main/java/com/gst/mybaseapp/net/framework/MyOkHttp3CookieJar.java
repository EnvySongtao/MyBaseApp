package com.gst.mybaseapp.net.framework;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.gst.mybaseapp.base.MyApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyOkHttp3CookieJar implements CookieJar {
    static final String COOKIE_SP_NAME = "okhttp3_cookie_jar";
    SharedPreferences cookieSp;

    public MyOkHttp3CookieJar() {
        cookieSp = MyApp.getInstance().getSharedPreferences(COOKIE_SP_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookieSp != null) {
            SharedPreferences.Editor editor = cookieSp.edit();
            List<String> cookiesIndexs = new ArrayList<>();
            for (Cookie cookie : cookies) {
                String path_name = cookie.domain() + "-cookieSpace-" + cookie.path() + "-cookieSpace-" + cookie.name();
                editor.putString(path_name, cookie.value());
                cookiesIndexs.add(path_name);
            }
            Set<String> _indexs = new HashSet<>(cookiesIndexs);
            editor.putStringSet(url.host(), _indexs);
            editor.apply();
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = new ArrayList<>();
        Set<String> _indexs = cookieSp.getStringSet(url.host(), null);
        if (_indexs != null) {
            List<String> stringList = new ArrayList<>(_indexs);
            if (stringList.size() > 0) {
                for (String name : stringList) {
                    Cookie.Builder cookieBuiler = new Cookie.Builder();
                    String[] _names = name.split("-cookieSpace-");
                    if (_names.length >= 3) {
                        cookieBuiler.domain(_names[0]).path(_names[1]).name(_names[2]);
                        String value = cookieSp.getString(name, "");
                        cookieBuiler.value(value);
                        cookies.add(cookieBuiler.build());
                    }
                }
            }
        }
        //cookies 不能为null 否则请求会出错
        return cookies;
    }
}
