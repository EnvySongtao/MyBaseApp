package com.gst.mybaseapp.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gst.mybaseapp.R;
import com.gst.mybaseapp.base.AppConfig;
import com.gst.mybaseapp.base.BaseActivity;
import com.gst.mybaseapp.been.ShortUrl;
import com.gst.mybaseapp.customView.LoadingView;
import com.gst.mybaseapp.database.DataBaseManager;
import com.gst.mybaseapp.database.been.City;
import com.gst.mybaseapp.net.AccountNetManager;
import com.gst.mybaseapp.net.AccountNetManagerImpl;
import com.gst.mybaseapp.net.interfaces.NetHelperInterface;
import com.gst.mybaseapp.net.webview.JSProtocolInterface;
import com.gst.mybaseapp.net.webview.JsInterface;
import com.gst.mybaseapp.ui.aroundView.ActAroundView;
import com.gst.mybaseapp.utils.AnimationUtil;
import com.gst.mybaseapp.utils.DeviceUtil;
import com.gst.mybaseapp.utils.FileUtil;
import com.gst.mybaseapp.utils.JsonUtil;
import com.gst.mybaseapp.utils.NetHelper;
import com.gst.mybaseapp.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity {
    private final static String TAG = "MainActivity";

    private String customerId = "";
    List<String> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        testNetwork();
//        testAnim();
//        testDataBase();
//        testWebView();
        testReadTxt();
        Log.i(TAG, "onCreate: ");
    }

    /**
     * 读取文件中的数据
     */
    private void testReadTxt() {
        setContentView(R.layout.act_btn_text_layout);

        final EditText et1 = (EditText) findViewById(R.id.et1);
        final EditText et2 = (EditText) findViewById(R.id.et2);
        Button btn1 = (Button) findViewById(R.id.btn1);
        Button btn2 = (Button) findViewById(R.id.btn2);
        Button btn3 = (Button) findViewById(R.id.btn3);
        Button btn4 = (Button) findViewById(R.id.btn4);
        final TextView tv_result = (TextView) findViewById(R.id.tv_result);

        final StringBuilder stringBuilder = new StringBuilder();
        btn1.setText("getString");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = 0, len = 4;
                Integer.parseInt(et1.getText().toString().trim());
                try {
                    start = Integer.parseInt(et1.getText().toString().trim());
                    len = Integer.parseInt(et2.getText().toString().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return;
                }
                stringBuilder.delete(0, stringBuilder.length());
                stringBuilder.append(FileUtil.getString(AppConfig.ASSET_LOAD_PATH + "800sentences7000words.txt", start, len));
                tv_result.setText(stringBuilder);
            }
        });

        btn2.setText("getStringByLines");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int start = 0, len = 4;
                try {
                    start = Integer.parseInt(et1.getText().toString().trim());
                    len = Integer.parseInt(et2.getText().toString().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return;
                }
                stringBuilder.delete(0, stringBuilder.length());
                stringBuilder.append(FileUtil.getStringByLines(AppConfig.ASSET_LOAD_PATH + "800sentences7000words.txt", start, len));
                tv_result.setText(stringBuilder);
            }
        });

        btn3.setOnClickListener((v)->{
            Intent intent=new Intent(MainActivity.this, ActAroundView.class);
            startActivity(intent);
        });
    }

    /**
     * webView test
     */
    private void testWebView() {
        setContentView(R.layout.act_web_view);
        WebView webView = (WebView) findViewById(R.id.wv_show);

        findViewById(R.id.input_et).setVisibility(View.GONE);
        findViewById(R.id.tv_title1).setVisibility(View.GONE);
        findViewById(R.id.tv_title2).setVisibility(View.GONE);
        findViewById(R.id.btn_invoke_js).setVisibility(View.GONE);
        findViewById(R.id.btn_invoke_js2).setVisibility(View.GONE);
//        webView.loadUrl(AppConfig.ASSET_LOAD_PATH + "protocol.html");
        webView.loadUrl("https://www.baidu.com/");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setSaveEnabled(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.addJavascriptInterface(new JSProtocolInterface(this, webView), "openAppView");

//        webView.loadUrl(AppConfig.ASSET_LOAD_PATH + "testVM.html");

//        webView.loadUrl(AppConfig.ASSET_LOAD_PATH + "javaScriptTest.html");
//
//        //在js中调用本地java方法
//        webView.getSettings().setJavaScriptEnabled(true);
//        //添加客户端支持
//        webView.setWebChromeClient(new WebChromeClient());
//        webView.addJavascriptInterface(new JsInterface(this), "AndroidWebView");
//
//        //添加客户端支持
//        webView.setWebChromeClient(new WebChromeClient());
//
//        //App调用JS代码
//        findViewById(R.id.btn_invoke_js).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String msg = ((EditText) findViewById(R.id.input_et)).getText().toString();
//                //调用js中的函数：showInfoFromApp(msg)
////                webView.loadUrl("javascript:showInfoFromApp('" + msg + "')");
//                Log.i(TAG, "javascript:showInfoFromApp(\"" + msg + "\")");
//                webView.loadUrl("javascript:showInfoFromApp(\"" + msg + "\")");
//            }
//        });
//        //App调用JS代码
//        findViewById(R.id.btn_invoke_js2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //调用js中的函数：showInfoFromApp()
//                webView.loadUrl("javascript:showInfoFromApp()");
//            }
//        });
    }


    /**
     * data base test
     */
    private void testDataBase() {
        setContentView(R.layout.activity_main);
        TextView tv_show = (TextView) findViewById(R.id.tv_show);
        TextView tv_show1 = (TextView) findViewById(R.id.tv_show1);
        TextView tv_show2 = (TextView) findViewById(R.id.tv_show2);
        TextView tv_show3 = (TextView) findViewById(R.id.tv_show3);
        TextView tv_show4 = (TextView) findViewById(R.id.tv_show4);
        tv_show.setText("getAllProvices");
        tv_show1.setText("updateProvicesAndAreas");
        tv_show2.setText("getAreaCode(\"四川\", \"遂宁\")");
        tv_show3.setText("getAllLoginNames");
        tv_show4.setText("getDataBaseInfo");

        tv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = DataBaseManager.getInstance().getAllProvices();
                Log.i(TAG, "onClick: getAllProvices.size() = " + list.size());
            }
        });
        tv_show1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list == null) list = DataBaseManager.getInstance().getAllProvices();
                Map<String, List<String>> areasMap = new HashMap<>();
                DataBaseManager.getInstance().updateProvicesAndAreas(areasMap, list);
                Log.i(TAG, "onClick:areasMap.get(\"四川\").size() = " + (areasMap.get("四川") == null ? 0 : areasMap.get("四川").size()));
            }
        });

        tv_show2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                City city = DataBaseManager.getInstance().getAreaCode("四川", "遂宁");
                Log.i(TAG, "onClick:  city:" + city);
            }
        });


        tv_show3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseManager.getInstance().clearAllLoginNames();
                DataBaseManager.getInstance().saveBusername("aaaddd", "123456");
                List<String> loginNames = DataBaseManager.getInstance().getAllLoginNames();
                Log.i(TAG, "onClick: getAllLoginNames.size() = " + loginNames.size());
            }
        });

        tv_show4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String infoString = DataBaseManager.getInstance().getDataBaseInfo();
                Log.i(TAG, "onClick: infoString = " + infoString);
            }
        });
    }

    /**
     * animations test
     */
    private void testAnim() {
        setContentView(R.layout.activity_main);
        TextView tv_show = (TextView) findViewById(R.id.tv_show);
        TextView tv_show1 = (TextView) findViewById(R.id.tv_show1);
        TextView tv_show2 = (TextView) findViewById(R.id.tv_show2);
        TextView tv_show3 = (TextView) findViewById(R.id.tv_show3);
        TextView tv_show4 = (TextView) findViewById(R.id.tv_show4);
//        tv_show.setText("executeAllAnimations");
//        tv_show.setText("squenceAnimations");
//        tv_show.setText("textChangeAnimator");
//        tv_show.setText("textChangeEvaluator");
        tv_show.setText("textChangeTimeAnimator");
//        tv_show1.setText("translateX");
//        tv_show1.setText("squenceAnimByRes");
//        tv_show1.setText("testPathObjectAnim");
        tv_show1.setText("propertyValuesHolder");
//        tv_show2.setText("leftIn");
        tv_show2.setText("animatorSet");
        tv_show3.setText("waitingOut");
        tv_show4.setText("push_bottom_out");

        tv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AnimationUtil.squenceAnimations(v);
//                AnimationUtil.textChangeAnimator((TextView) v);
//                AnimationUtil.textChangeEvaluator((TextView) v);
                AnimationUtil.textChangeTimeAnimator((TextView) v);
            }
        });
//        tv_show1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AnimationUtil.squenceAnimByRes(v);
//            }
//        });
// tv_show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AnimationUtil.executeAllAnimations(v, 0.2F, 1F);
//            }
//        });

        tv_show1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AnimationUtil.translateX(v);
//                AnimationUtil.translateX(v, null);
//                AnimationUtil.testTranslationXObjectAnim(v);
                AnimationUtil.propertyValuesHolder(v);
            }
        });

        tv_show2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AnimationUtil.leftIn(v);
                AnimationUtil.animatorSet(v);
            }
        });


        tv_show3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.waitingOut(v);
            }
        });

        tv_show4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.push_bottom_out(v);
            }
        });
    }


    /**
     * network framework
     */
    private void testNetwork() {
        setContentView(R.layout.activity_main);
        TextView tv_show = (TextView) findViewById(R.id.tv_show);
        TextView tv_show1 = (TextView) findViewById(R.id.tv_show1);
        TextView tv_show2 = (TextView) findViewById(R.id.tv_show2);
        TextView tv_show3 = (TextView) findViewById(R.id.tv_show3);
        TextView tv_show4 = (TextView) findViewById(R.id.tv_show4);
        tv_show.setText("queryGet");
        tv_show1.setText("getAppversion");
        tv_show2.setText("login");
        tv_show3.setText("getFxFldata");
        tv_show4.setText("loadingView");

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
                AppConfig.IP = DeviceUtil.getLocalIpAddress();
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

        tv_show4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingView.show(MainActivity.this, MainActivity.this, "傻眼了吧......");
            }
        });
    }

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
        new Intent(this, ActTest.class);
    }


}
