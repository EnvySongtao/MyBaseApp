package com.gst.mybaseapp.utils;

import com.gst.mybaseapp.net.interfaces.NetHelperInterface;
import com.gst.mybaseapp.net.framework.OkHttp3Helper;
import com.gst.mybaseapp.net.interfaces.NetHelperInterface.HttpHelperImpl;
import com.gst.mybaseapp.net.interfaces.NetHelperInterface.IReq;
import com.gst.mybaseapp.net.interfaces.NetHelperInterface.OnResponseListener;

import java.io.File;
import java.util.Map;

/**
 * 统一网络请求框架
 */
public class NetHelper implements HttpHelperImpl {
    public final static int CENNECT_TIME_OUT=40*1000;
    public final static int READ_TIME_OUT=40*1000;

    private static final NetHelper ourInstance = new NetHelper();
    private OkHttp3Helper okHttp3Helper = null;

    public static NetHelper getInstance() {
        return ourInstance;
    }

    private NetHelper() {
        okHttp3Helper = new OkHttp3Helper();
    }

    @Override
    public NetHelperInterface.IReq queryPost2(String url, String param, NetHelperInterface.OnResponseListener listener) {
        return okHttp3Helper.queryPost2(url, param, listener);
    }

    @Override
    public String queryPost3(String url, File param, String id, String customerId, String channel, OnResponseListener listener) {
        return okHttp3Helper.queryPost3(url, param, id, customerId, channel, listener);
    }

    @Override
    public IReq queryPost(String url, Map<String, Object> param, OnResponseListener listener) {
        return okHttp3Helper.queryPost(url, param, listener);
    }

    @Override
    public IReq queryGet(String url, OnResponseListener listener) {
        return okHttp3Helper.queryGet(url, listener);
    }

}

