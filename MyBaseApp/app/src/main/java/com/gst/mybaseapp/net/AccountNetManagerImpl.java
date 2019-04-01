package com.gst.mybaseapp.net;

import com.gst.mybaseapp.net.interfaces.NetHelperInterface.OnResponseListener;

public interface AccountNetManagerImpl {

    /**
     * 获取文网络请求管理器
     *
     * @return
     */
    AccountNetManagerImpl getAccountNetManager();

    /**
     * 获取APP的版本
     *
     * @param listener
     */
    void getAppversion(OnResponseListener listener);


    /**
     * 登录
     *
     * @param listener
     */
    void login(String mobile, String password, OnResponseListener listener);
    /**
     * 登录
     *
     * @param listener
     */
    void getFxFldata(String customerId,String month, OnResponseListener listener);
}
