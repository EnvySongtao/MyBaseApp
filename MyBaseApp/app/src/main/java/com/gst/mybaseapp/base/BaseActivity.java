package com.gst.mybaseapp.base;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gst.mybaseapp.base.users.User;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;

/**
 * github 地址
 * https://github.com/JessYanCoding/AndroidAutoSize/blob/master/demo/src/main/java/me/jessyan/autosize/demo/FragmentHost.java
 * author: GuoSongtao on 2019/3/27 17:43
 * email: 157010607@qq.com
 */
public class BaseActivity extends AppCompatActivity {
    protected SharedPreferences mSp;
    protected User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AutoSize.autoConvertDensity(this, 320, true);//以宽为标准，且宽长度设为320dp
        super.onCreate(savedInstanceState);
        mSp = MyApp.getSharedPreferences();
        mUser = User.getInstance(this);
        if (actNeedPermissionsAll != null || !actNeedPermissionsAll.isEmpty()) checkPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyComponentsManager.getInstance().setCurrentActivity(this);
    }

    protected void sendMessage(Handler handler, int what, Object obj) {
        Message msg = Message.obtain();
        msg.obj = obj;
        msg.what = what;
        if (handler != null) handler.sendMessage(msg);
    }

    protected void sendMessageDelay(Handler handler, int what, Object obj, int delayTime) {
        Message msg = Message.obtain();
        msg.obj = obj;
        msg.what = what;
        if (handler != null) handler.sendMessageDelayed(msg, delayTime);
    }

    public SharedPreferences getSp() {
        return mSp;
    }

    /*************2019/4/3   今日头条适配 start ****************/
    public void stop(View view) {
        AutoSizeConfig.getInstance().stop(this);
    }

    public void restart(View view) {
        AutoSizeConfig.getInstance().restart();
    }
    /*************2019/4/3   今日头条适配 start ****************/


    /*************2019/4/3   申请权限 start ****************/
    /**
     * 当编译targetSDKVersion < 23时使用
     * ContextCompat.checkSelfPermission and Context.checkSelfPermission将会不起作用
     * 需要使用
     * PermissionChecker.checkSelfPermission
     * <p>
     * 同样targetSDKVersion >= 23时PermissionChecker.checkSelfPermission也会无效，
     * 需要使用ContextCompat.checkSelfPermission
     */
    protected List<String> actNeedPermissionsAll = new ArrayList<>();//当前页面需要的所有权限
    protected List<String> currentDeniedPermissions = new ArrayList<>();//当前时间段不确定以及需要的权限
    public static final int PERMISSION_REQUEST_COODE = 0X0210;
    public static final int PERMISSION_REQUEST_COODE_AGAIN = 0X0211;

    /**
     * 操作过程中 判断并请求没有的权限
     *
     * @param permissions
     * @return
     */
    protected boolean hasDeniedPermissionAndRequest(String... permissions) {
        return hasDeniedPermission(true, permissions);
    }

    /**
     * 操作过程中请求权限，
     *
     * @param request     false只判断不请求 true判断并请求没有的权限
     * @param permissions 权限数组
     * @return
     */
    protected boolean hasDeniedPermission(boolean request, String... permissions) {
        boolean hasDeniedPermission = false;
        List<String> curentActNeedPermissionsNew = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                hasDeniedPermission = true;
                curentActNeedPermissionsNew.add(permission);
            }
        }
        if (request && !curentActNeedPermissionsNew.isEmpty()) {
            String[] permissionStrs = curentActNeedPermissionsNew.toArray(new String[curentActNeedPermissionsNew.size()]);
            ActivityCompat.requestPermissions(this, permissionStrs, PERMISSION_REQUEST_COODE_AGAIN);
        }
        return hasDeniedPermission;
    }

    /**
     * 进入页面请求权限
     */
    private void checkPermissions() {
        if (actNeedPermissionsAll == null || actNeedPermissionsAll.isEmpty())
            initCurentActNeedPermissionsAll();

        //不是6.0以前的旧版本不需要检查权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;

        if (actNeedPermissionsAll != null && !actNeedPermissionsAll.isEmpty()) {
            List<String> curentActNeedPermissionsNew = new ArrayList<>();
            for (String permissionStr : actNeedPermissionsAll) {
                if (ActivityCompat.checkSelfPermission(this, permissionStr) != PackageManager.PERMISSION_GRANTED) {
                    curentActNeedPermissionsNew.add(permissionStr);
                }
            }

            if (!curentActNeedPermissionsNew.isEmpty()) {
                String[] permissionStrs = curentActNeedPermissionsNew.toArray(new String[curentActNeedPermissionsNew.size()]);
                ActivityCompat.requestPermissions(this, permissionStrs, PERMISSION_REQUEST_COODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，第一个参数是当前Acitivity/Fragment，回调方法写在当前Activity/Framgent。
        currentDeniedPermissions = new ArrayList<>();
        List<String> grantedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            //申请权限失败
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                currentDeniedPermissions.add(permissions[i]);
            } else {
                grantedPermissions.add(permissions[i]);
            }
        }
        if (currentDeniedPermissions != null && !currentDeniedPermissions.isEmpty()) {
            onDeniedPermissionsResult(currentDeniedPermissions, requestCode);
        }

        if (grantedPermissions != null && !grantedPermissions.isEmpty()) {
            onGrantedPermissionsResult(grantedPermissions, requestCode);
        }
    }

    /**
     * 初始化权限  注意使用  actNeedPermissionsAll
     */
    protected void initCurentActNeedPermissionsAll() {

    }

    /**
     * 请求权限成功返回失败
     *
     * @param grantedPermissions
     */
    protected void onGrantedPermissionsResult(List<String> grantedPermissions, int requestCode) {
    }


    /**
     * 请求权限失败返回失败
     *
     * @param deniedPermissions
     */
    protected void onDeniedPermissionsResult(List<String> deniedPermissions, int requestCode) {
    }
    /*************2019/4/3   申请权限 end ****************/
}
