package com.gst.mybaseapp.utils;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gst.mybaseapp.net.framework.OkHttp3Helper.JSON;

public class JsonUtil {

    private static Gson mGson = new Gson();

    /**
     * 不允许外部 new
     */
    private JsonUtil() {
    }

    public static <T> List<T> parseList(String jsonStr, Class<T> classOfT) {
        List<T> arr = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonStr);
            if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    T item = mGson.fromJson(array.optString(0), classOfT);
                    arr.add(item);
                }
            }
        } catch (Exception e) {
            Log.e("JsonUtil", "转化成list失败:" + jsonStr);
            e.printStackTrace();
        }
        return arr;
    }

    public <T> T parseObject(String jsonStr, Class<T> classOfT) {
        T item = null;
        try {
            item = mGson.fromJson(jsonStr, classOfT);
        } catch (Exception e) {
            Log.e("JsonUtil", "转化成list失败:" + jsonStr);
            e.printStackTrace();
        }
        return item;
    }


    public static String toJson(Map map) {

//        map.put("VER", "66");

        return mGson.toJson(map);

    }
}
