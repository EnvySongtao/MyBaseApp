package com.gst.mybaseapp.base.users;

import android.content.Context;
import android.content.SharedPreferences;

import com.gst.mybaseapp.base.AppConfig;
import com.gst.mybaseapp.base.MyApp;
import com.gst.mybaseapp.net.AccountNetManager;
import com.gst.mybaseapp.net.AccountNetManagerImpl;

/**
 * author: GuoSongtao on 2019/4/3 10:53
 * email: 157010607@qq.com
 */
public abstract class User {
    /********************** User的属性信息 start *******************************/
    private String custid;
    /********************** User的属性信息 end *********************************/

    protected Context currContext;
    protected SharedPreferences mSp;
    protected SharedPreferences.Editor mEditor;
//    private AccountNetManagerImpl netManager;


    protected User(Context context) {
        currContext = context;
        mSp = MyApp.getSharedPreferences();
        mEditor = mSp.edit();
//        netManager = new AccountNetManager();
    }

    /**
     * 一般在登录后需要 重新设置一下
     *
     * @param context
     * @return
     */
    public static User getInstance(Context context) {
        SharedPreferences sp = MyApp.getSharedPreferences();
        String loginBsuerType = sp.getString(AppConfig.SP_LOGIN_BUSER_TYPE, "");//POS机型标识
        User user;
        if (UserTypes.NOCARD.equalsIgnoreCase(loginBsuerType)) {
            user = new NoCardUser(context);
        } else if (UserTypes.NORMAL.equalsIgnoreCase(loginBsuerType)) {
            user = new NormalUser(context);
        } else {
            user = new NormalUser(context);
        }
        return user;
    }

    public String getCustid() {
        return custid;
    }
}
