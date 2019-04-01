package com.gst.mybaseapp.net;

import android.os.Build;
import android.util.Log;

import com.gst.mybaseapp.config.AppConfig;
import com.gst.mybaseapp.net.framework.OkHttp3Helper;
import com.gst.mybaseapp.net.interfaces.NetHelperInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class AccountNetManager implements AccountNetManagerImpl {
    private NetHelperInterface.HttpHelperImpl netHelper;

    private Map<String, Object> perantMap;

    Map<String, Object> initDeviceInfos() {
        Map<String, Object> perantMap = new HashMap<>();
        perantMap.put("platType", "2");
        perantMap.put("deviceId", "35a751cd7d23");
        perantMap.put("deviceType", "2.0.0");
        perantMap.put("sysVsrsion", "8");
        perantMap.put("userToken", "userToken");
        perantMap.put("versionName", "2.0.0");
        return perantMap;
    }

    public AccountNetManager() {
        perantMap = initDeviceInfos();
        netHelper = new OkHttp3Helper();
    }

    @Override
    public AccountNetManagerImpl getAccountNetManager() {
        return this;
    }

    @Override
    public void getAppversion(NetHelperInterface.OnResponseListener listener) {
        HashMap<String, Object> perantMap2 = new HashMap<String, Object>();
        perantMap2.put("code", "android");
        perantMap.put("requestObject", perantMap2);
        netHelper.queryPost(AppConfig.WEB_HOST + "gfb/customer/getVersion.do", perantMap, listener);
    }

    @Override
    public void login(String mobile, String password, NetHelperInterface.OnResponseListener listener) {
        Map<String, Object> chirldMap = new HashMap<String, Object>();
        chirldMap.put("password", password);
        chirldMap.put("userName", mobile);
        perantMap.put("requestObject", chirldMap);

        netHelper.queryPost(AppConfig.WEB_HOST + "gfb/customer/login.do", perantMap, listener);
    }

    @Override
    public void getFxFldata(String customerId, String month, NetHelperInterface.OnResponseListener listener) {
        JSONObject chirldMap = new JSONObject();
        JSONObject chirldMaptitle = new JSONObject();
        JSONObject chirldMap2 = new JSONObject();
        JSONObject chirldMap3 = new JSONObject();
        Map<String, Object> head = new HashMap<String, Object>();
        Map<String, Object> body = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            chirldMap.put("loginflag", "");
            chirldMap.put("source", "Android");
            chirldMap.put("ip", AppConfig.IP);
            chirldMap.put("city", AppConfig.cityName);
            chirldMap.put("uuid", AppConfig.UUId);
            chirldMap.put("modelSpecification", Build.MODEL);
            chirldMap.put("channel", AppConfig.channel);
            chirldMap.put("setTitle", chirldMaptitle);
            chirldMap.put("function", "04");
            head.put("head", chirldMap);
            chirldMap2.put("customerId", customerId);
            chirldMap2.put("month", month);
            body.put("body", chirldMap2);
            map.putAll(body);
            map.putAll(head);
            chirldMap3.put("request", map);
            Log.d("YHHGGTTGGGGGG",chirldMap3.toString());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        String jsonKey = "";
        try {
            JSONObject jsonObject = new JSONObject(chirldMap3.toString());
            jsonKey = TestDES.aesEncrypt("t171420100302rsa", jsonObject.toString(), "t171420100302rsa");
            jsonKey = URLEncoder.encode(jsonKey, "utf-8");
            map.put("jsonKey", jsonKey);

        } catch (Exception e) {
            e.printStackTrace();
        }


        netHelper.queryPost(AppConfig.WEB_HOST + "customerPlatform/module_fuxin_fuxinGift/queryGiftList.do?jsonKey=" + jsonKey,
                map, listener);
    }
}
