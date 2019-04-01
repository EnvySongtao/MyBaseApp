package com.gst.mybaseapp.net.framework;

import com.gst.mybaseapp.net.interfaces.NetHelperInterface;

import okhttp3.Call;

public class OkRequest implements NetHelperInterface.IReq {
    private Call call;

    public OkRequest(Call call) {
        this.call = call;
    }

    @Override
    public void cancel() {
        if (call != null) call.cancel();
    }
}
