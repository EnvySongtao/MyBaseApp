package com.gst.mybaseapp.database;

import android.content.Context;

import com.gst.mybaseapp.database.been.City;

import java.util.List;
import java.util.Map;

/**
 * author: GuoSongtao on 2019/4/9 15:11
 * email: 157010607@qq.com
 */
public interface DataBaseManagerImpl {

    String getDataBaseInfo();
    /**
     * 获取所有的省名称
     *
     * @return
     */
    List<String> getAllProvices();


    /**
     * @param areaMap key为省份 值为对应的城市列表
     * @return
     */
    void updateProvicesAndAreas(Map<String, List<String>> areaMap, List<String> allProvices);

    /**
     * 根据省名和城市名获取城市信息
     *
     * @param proviceName
     * @param areaName
     * @return
     */
    City getAreaCode(String proviceName, String areaName);


    //获取所有登录账号名字
    List<String> getAllLoginNames();
    //保存登录名和密码
    void saveBusername(String bUsername, String password);
    //获取所有登录账号名字
    void clearAllLoginNames();
}
