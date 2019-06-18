package com.gst.envy.otherlibs.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * github 地址
 * https://github.com/JessYanCoding/AndroidAutoSize/blob/master/demo/src/main/java/me/jessyan/autosize/demo/FragmentHost.java
 * author: GuoSongtao on 2019/3/27 17:43
 * email: 157010607@qq.com
 */
public class BaseActivity extends AppCompatActivity {
    protected SharedPreferences mSp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
