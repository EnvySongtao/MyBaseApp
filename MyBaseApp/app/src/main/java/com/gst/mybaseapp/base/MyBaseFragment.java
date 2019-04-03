package com.gst.mybaseapp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.jessyan.autosize.AutoSize;

/**
 * author: GuoSongtao on 2019/3/27 17:46
 * email: 157010607@qq.com
 */
public class MyBaseFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //由于某些原因, 屏幕旋转后 Fragment 的重建, 会导致框架对 Fragment 的自定义适配参数失去效果
        //所以如果您的 Fragment 允许屏幕旋转, 则请在 onCreateView 手动调用一次 AutoSize.autoConvertDensity()
        //如果您的 Fragment 不允许屏幕旋转, 则可以将下面调用 AutoSize.autoConvertDensity() 的代码删除掉
        AutoSize.autoConvertDensity(getActivity(), 320, true);//以宽为标准，且宽长度设为320dp
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
