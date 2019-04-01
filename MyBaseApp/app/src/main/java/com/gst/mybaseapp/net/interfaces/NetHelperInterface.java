package com.gst.mybaseapp.net.interfaces;

import java.io.File;
import java.util.Map;

public class NetHelperInterface {

    /**
     * APP中网络的统一回调接口
     */
    public interface OnResponseListener {
        void onResponse(int status, boolean isSuccess, String result, String msg);
    }

    /**
     * 请求返回句柄类
     *
     * @author chentangzheng
     */
    public interface IReq {
        /**
         * 取消当前请求
         */
        void cancel();
    }


    public interface HttpHelperImpl {

        /**
         * post请求
         */
        IReq queryPost2(String url, String param, OnResponseListener listener);

        /**
         * post请求
         */
        String queryPost3(String url, File param, String id, String customerId, String channel, OnResponseListener listener);

        /**
         * post请求
         */
        IReq queryPost(String url, Map<String, Object> param, OnResponseListener listener);

        /**
         * get请求
         */
        IReq queryGet(String url, OnResponseListener listener);
    }
}
