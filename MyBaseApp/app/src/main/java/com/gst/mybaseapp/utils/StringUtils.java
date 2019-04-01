package com.gst.mybaseapp.utils;

import android.text.TextUtils;

import org.json.JSONObject;

public class StringUtils {
    private static final StringUtils ourInstance = new StringUtils();

    public static StringUtils getInstance() {
        return ourInstance;
    }

    private StringUtils() {
    }

    /**
     * 忽略大小写的 尾部比较
     * @param compareStr 被比较的String
     * @param endStr 结束的String
     * @return
     */
    public static boolean endWithIngoreCase(String compareStr,String endStr){
        if(TextUtils.isEmpty(compareStr)&&TextUtils.isEmpty(endStr)) return false;

        if(compareStr.length()<endStr.length()) return false;

        String urlLast4=compareStr.substring(compareStr.length()-endStr.length());
        if(!endStr.equalsIgnoreCase(urlLast4)) return false;

        return true;
    }


    /**
     * 忽略大小写的 开始比较
     * @param compareStr 被比较的String
     * @param startStr 开始的String
     * @return
     */
    public static boolean startWithIngoreCase(String compareStr,String startStr){
        if(TextUtils.isEmpty(compareStr)&&TextUtils.isEmpty(startStr)) return false;

        if(compareStr.length()<startStr.length()) return false;

        String urlLast4=compareStr.substring(0,startStr.length());
        if(!startStr.equalsIgnoreCase(urlLast4)) return false;
        return true;
    }

    /**
     * 解析数据
     *
     * @param jsonObject
     * @param property
     * @return 默认返回空字符串，
     */
    public static String parseObject(JSONObject jsonObject, String property) {
        return parseObject(jsonObject, property, "");
    }

    /**
     * 解析数据
     *
     * @param jsonObject
     * @param property
     * @return 默认返回 defaultResult，
     */
    public static String parseObject(JSONObject jsonObject, String property, String defaultResult) {
        String resultString = defaultResult;
        try {
            if (jsonObject != null && jsonObject.has(property) && !jsonObject.isNull(property))
                resultString = jsonObject.getString(property);
        } catch (Exception e) {
        }
        return resultString;
    }
}
