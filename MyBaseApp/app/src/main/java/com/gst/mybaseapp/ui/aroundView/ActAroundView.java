package com.gst.mybaseapp.ui.aroundView;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.gst.mybaseapp.R;
import com.gst.mybaseapp.base.BaseActivity;
import com.zph.glpanorama.GLPanorama;

/**
 * author: GuoSongtao on 2019/6/17 16:49
 * email: 157010607@qq.com
 */
public class ActAroundView extends BaseActivity {
    GLPanorama mGLPanorama;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_around_view);
        //初始化全景控件
        mGLPanorama = (GLPanorama) findViewById(R.id.mGLPanorama);
        //传入你的全景图
        mGLPanorama.setGLPanorama(R.drawable.imggugong);
    }
}
