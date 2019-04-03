package com.gst.mybaseapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gst.mybaseapp.base.BaseActivity;

/**
 * author: GuoSongtao on 2019/4/3 09:46
 * email: 157010607@qq.com
 */
public class ActTest extends BaseActivity {
    public static final String TAG_MOTHED_NAME = "mothedName";

    public static final String[] TESTMETHODNAME =
            {"loadingView", "loadingView"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        String mothedName = getIntent().getStringExtra(TAG_MOTHED_NAME);
        int index = 0;//给个默认值
        for (int i = 0; i < TESTMETHODNAME.length; i++) {
            if (TESTMETHODNAME[i].equals(mothedName)) {
                index = i;
                break;
            }
        }

        switch (index) {
            case 0:
                break;
        }
    }
}
