package com.gst.mybaseapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gst.mybaseapp.been.ShortUrl;
import com.gst.mybaseapp.config.AppConfig;
import com.gst.mybaseapp.net.AccountNetManager;
import com.gst.mybaseapp.net.AccountNetManagerImpl;
import com.gst.mybaseapp.utils.DeviceUtil;
import com.gst.mybaseapp.utils.JsonUtil;
import com.gst.mybaseapp.utils.NetHelper;
import com.gst.mybaseapp.net.interfaces.NetHelperInterface;
import com.gst.mybaseapp.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private String customerId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testNetwork();
        Log.i(TAG, "onCreate: ");
    }

    private void testNetwork() {
        TextView tv_show = findViewById(R.id.tv_show);
        TextView tv_show1 = findViewById(R.id.tv_show1);
        TextView tv_show2 = findViewById(R.id.tv_show2);
        TextView tv_show3 = findViewById(R.id.tv_show3);
        tv_show.setText("queryGet");
        tv_show1.setText("getAppversion");
        tv_show2.setText("login");
        tv_show3.setText("getFxFldata");

        tv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://api.weibo.com/2/short_url/shorten.json?source=1481074835&url_long=https://user-gold-cdn.xitu.io/2017/11/13/15fb45a27df11fea?w=320&h=565&f=gif&s=1496645
                NetHelper.getInstance().queryGet("https://api.weibo.com/2/short_url/shorten.json?source=1481074835&url_long=https://user-gold-cdn.xitu.io/2017/11/13/15fb45a27df11fea?w=320&h=565&f=gif&s=1496645", new NetHelperInterface.OnResponseListener() {
                    @Override
                    public void onResponse(int status, boolean isSuccess, String result, String msg) {
                        Log.i("MainActivity", "result=" + result);
                        try {
                            JSONObject object = new JSONObject(result);
                            String arrayStr = object.optString("urls");
                            List<ShortUrl> shortUrls = JsonUtil.parseList(arrayStr, ShortUrl.class);
                            Log.i("MainActivity", "url_short=" + shortUrls.get(0).getUrl_short());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        tv_show1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountNetManagerImpl accountNetManager = new AccountNetManager();
                accountNetManager.getAppversion(new NetHelperInterface.OnResponseListener() {
                    @Override
                    public void onResponse(int status, boolean isSuccess, String result, String msg) {
                        Log.i("MainActivity", "result=" + result);
                    }
                });
            }
        });

        tv_show2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountNetManagerImpl accountNetManager = new AccountNetManager();
                AppConfig.IP=DeviceUtil.getLocalIpAddress();
                accountNetManager.login("13219205186", "q1234567", new NetHelperInterface.OnResponseListener() {
                    @Override
                    public void onResponse(int status, boolean isSuccess, String result, String msg) {
                        Log.i("MainActivity", "result=" + result);
                        try {
                            JSONObject returnMap = new JSONObject(result);
                            JSONObject returns = returnMap.getJSONObject("returns");
                            JSONObject customer = returns.getJSONObject("customer");
                            customerId = StringUtils.parseObject(customer, "id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


        tv_show3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(customerId)) return;
                AccountNetManagerImpl accountNetManager = new AccountNetManager();
                accountNetManager.getFxFldata(customerId, "", new NetHelperInterface.OnResponseListener() {
                    @Override
                    public void onResponse(int status, boolean isSuccess, String result, String msg) {
                        Log.i("MainActivity", "result=" + result);
                    }
                });
            }
        });
    }

    private final static String TAG = "MainActivity";

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart:");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState: ");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState: ");
    }


}
