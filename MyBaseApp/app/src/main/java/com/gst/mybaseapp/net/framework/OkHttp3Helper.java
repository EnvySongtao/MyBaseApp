package com.gst.mybaseapp.net.framework;


import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gst.mybaseapp.base.AppConfig;
import com.gst.mybaseapp.net.MD5;
import com.gst.mybaseapp.net.TestDES;
import com.gst.mybaseapp.utils.NetHelper;
import com.gst.mybaseapp.net.interfaces.NetHelperInterface;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttp3Helper implements NetHelperInterface.HttpHelperImpl {
    OkHttpClient okHttpClient;

    public OkHttp3Helper() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //网络日志打印出返回body
        if (AppConfig.IS_DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        builder.connectTimeout(NetHelper.CENNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(NetHelper.CENNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .cookieJar(new MyOkHttp3CookieJar());

        okHttpClient = builder.build();
    }

    @Override
    public NetHelperInterface.IReq queryPost2(String url, String param, NetHelperInterface.OnResponseListener listener) {
        return null;
    }

    @Override
    public String queryPost3(String url, File param, String id, String customerId, String channel, NetHelperInterface.OnResponseListener listener) {
        return null;
    }

    @Override
    public NetHelperInterface.IReq queryPost(String url, Map<String, Object> param, final NetHelperInterface.OnResponseListener listener) {
//        return queryDingShuaPost(url,param,listener);
        return queryBaoYingPost(url, param, listener);
    }

    //https://api.weibo.com/2/short_url/shorten.json?source=1481074835&url_long=
    @Override
    public NetHelperInterface.IReq queryGet(String url, final NetHelperInterface.OnResponseListener listener) {
        //1.创建OkHttpClient对象
        //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
        Request request = new Request.Builder().url(url).method("GET", null).build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        OkRequest okRequest = new OkRequest(call);
        //4.请求加入调度，重写回调方法
        //同步请求调用Call的execute()方法，异步请求调用call.enqueue()方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("queryGet", e.getMessage());
                if (listener != null) {
                    listener.onResponse(0x002, false, e.getMessage(), e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                StringBuffer bodySB = new StringBuffer();
                bodySB.append(response.body().string());//注意response.body()只能string()一次
                Log.i("queryGet", bodySB.toString());
                if (listener != null) {
                    listener.onResponse(0x001, true, bodySB.toString(), "");
                }
            }
        });
        return okRequest;
    }


    //https://api.weibo.com/2/short_url/shorten.json?source=1481074835&url_long=
    public String queryGet2(String url, final NetHelperInterface.OnResponseListener listener) {
        //1.创建OkHttpClient对象
        //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
        Request request = new Request.Builder().url(url).method("GET", null).build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        OkRequest okRequest = new OkRequest(call);
        //4.请求加入调度，重写回调方法
        //同步请求调用Call的execute()方法，异步请求调用call.enqueue()方法
        StringBuffer bodySB = new StringBuffer();
        try {
            bodySB.append(call.execute().body().string());//注意response.body()只能string()一次
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("queryGet", bodySB.toString());
        return bodySB.toString();
    }


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/*");

    /**
     * 保赢请求将请求参数放在了request的body中
     * 获取APP版本时没加密
     * 其他请求加密了
     *
     * @param url
     * @param param
     * @param listener
     * @return
     */
    public NetHelperInterface.IReq queryBaoYingPost(String url, Map<String, Object> param, final NetHelperInterface.OnResponseListener listener) {

        JSONObject jsonObject = new JSONObject(param);
        String result= jsonObject.toString();

        Map<String, String> requestMap = new HashMap<>();
        String requsetMessage = "";
        try {
            requsetMessage = TestDES.encode("tiananapp", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestMap.put("requestMessage", requsetMessage.replaceAll("\n", ""));
        MD5 md5 = new MD5();
        String sign = md5.getMD5ofStr("tiananapp" + result).toUpperCase();//MD5
        requestMap.put("sign", sign);
        String jsonStr = new JSONObject(requestMap).toString();
        //volley 需要转换成了byte[]再传输  OKhttp直接传入JSONString就好了
//        byte[] jsonByte=null;
//        try {
//            jsonByte=jsonStr.getBytes("UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Log.e("queryBaoYing jsonStr=", jsonStr);

        return getOkHttpClientIReq(listener, request);
    }


    /**
     * okhttp frombody表单使用
     *
     * @param url
     * @param param
     * @param listener
     * @return
     */
    public NetHelperInterface.IReq queryPostFormBody(String url, Map<String, Object> param, final NetHelperInterface.OnResponseListener listener) {

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            if (entry.getValue() instanceof Map) {
                JSONObject jsonObject = new JSONObject((Map) entry.getValue());
                formBodyBuilder.add(entry.getKey(), jsonObject.toString());
            }
            formBodyBuilder.add(entry.getKey(), entry.getValue().toString());
        }
        Request request = new Request.Builder()
                .url(url)
                .post(formBodyBuilder.build())
                .build();

        return getOkHttpClientIReq(listener, request);
    }


    /**
     * https://www.jianshu.com/p/1133389c1f75
     * okhttp frombody表单使用
     *
     * @param url
     * @param param
     * @param listener
     * @return
     */
    public NetHelperInterface.IReq queryPostMultipartBody(String url, Map<String, Object> param, Map<String, File> fileMap, final NetHelperInterface.OnResponseListener listener) {

        MultipartBody.Builder mutiBodyBuilder = new MultipartBody.Builder();
        //添加图片
        for (Map.Entry<String, File> fileEntry : fileMap.entrySet()) {
            File file = fileEntry.getValue();
            RequestBody fileBody = RequestBody.create(MEDIA_TYPE_IMAGE, file);
            mutiBodyBuilder.addFormDataPart(fileEntry.getKey(), fileEntry.getValue().getName(), fileBody);
        }

        //添加信息
        JSONObject jsonObject = new JSONObject(param);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        mutiBodyBuilder.addPart(body);

        Request request = new Request.Builder()
                .url(url)
                .post(mutiBodyBuilder.build())
                .build();

        return getOkHttpClientIReq(listener, request);
    }

    /**
     * https://www.jianshu.com/p/1133389c1f75
     * okhttp frombody表单使用
     *
     * @param url
     * @param param
     * @param listener
     * @return
     */
    public NetHelperInterface.IReq queryPostMultipartBody2(String url, Map<String, Object> param, Map<String, File> fileMap, final NetHelperInterface.OnResponseListener listener) {

        MultipartBody.Builder mutiBodyBuilder = new MultipartBody.Builder();

        //添加图片
        for (Map.Entry<String, File> fileEntry : fileMap.entrySet()) {
            File file = fileEntry.getValue();
            RequestBody body = RequestBody.create(MEDIA_TYPE_IMAGE, file);
            mutiBodyBuilder.addFormDataPart(fileEntry.getKey(), fileEntry.getValue().getName(), body);
        }

        //添加信息
        if (param != null && !param.isEmpty()) {
            for (String key : param.keySet()) {
                String keyValueString;
                if (param.get(key) instanceof Map) {
                    JSONObject jsonObject = new JSONObject((Map) param.get(key));
                    keyValueString=jsonObject.toString();
                }else{
                    keyValueString=param.get(key).toString();
                }
                mutiBodyBuilder.addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(JSON, keyValueString));
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .post(mutiBodyBuilder.build())
                .build();

        return getOkHttpClientIReq(listener, request);
    }



    /*********************************** OKhttp 断点下载 start ***********************************/
    /**
     * 下载网络文件
     * @param url
     * @param listener
     * @return
     */
    public NetHelperInterface.IReq downloadFile(String url,int startsPoint, final NetHelperInterface.OnResponseListener listener) {

        Request request=new Request.Builder().url(url)
                .addHeader("RANGE","bytes="+startsPoint+"-")
                .build();

        // 重写ResponseBody监听请求
        Interceptor interceptor=new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse=chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new DownloadResponseBody(originalResponse,startsPoint,downloadListener))
                        .build();
            }
        };
        OkHttpClient dlOkhttp = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())// 绕开证书
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())// 绕开证书
                .build();

        Call call = dlOkhttp.newCall(request);
        OkRequest okRequest = new OkRequest(call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                long length = response.body().contentLength();
                if (length == 0){
                    // 说明文件已经下载完，直接跳转安装就好
                    downloadListener.complete(String.valueOf(getFile().getAbsoluteFile()));
                    return;
                }
                downloadListener.start(length+startsPoint);
                // 保存文件到本地
                InputStream is = null;
                RandomAccessFile randomAccessFile = null;
                BufferedInputStream bis = null;

                byte[] buff = new byte[2048];
                int len = 0;
                try {
                    is = response.body().byteStream();
                    bis  =new BufferedInputStream(is);

                    File file = getFile();
                    // 随机访问文件，可以指定断点续传的起始位置
                    randomAccessFile =  new RandomAccessFile(file, "rwd");
                    randomAccessFile.seek (startsPoint);
                    while ((len = bis.read(buff)) != -1) {
                        randomAccessFile.write(buff, 0, len);
                    }

                    // 下载完成
                    downloadListener.complete(String.valueOf(file.getAbsoluteFile()));
                } catch (Exception e) {
                    e.printStackTrace();
                    downloadListener.loadfail(e.getMessage());
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (bis != null){
                            bis.close();
                        }
                        if (randomAccessFile != null) {
                            randomAccessFile.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        return okRequest;
    }

    private DownloadResponseBody.DownloadListener downloadListener=new DownloadResponseBody.DownloadListener() {
        @Override
        public void start(long max) {

        }

        @Override
        public void loading(int progress) {

        }

        @Override
        public void complete(String path) {

        }

        @Override
        public void fail(int code, String message) {

        }

        @Override
        public void loadfail(String message) {

        }
    };

    private File getFile() {
        String root = Environment.getExternalStorageDirectory().getPath();
        File file = new File(root,"updateDemo.apk");
        return file;
    }

    private long getFileStart(){
        String root = Environment.getExternalStorageDirectory().getPath();
        File file = new File(root,"updateDemo.apk");
        return file.length();
    }
    /*********************************** OKhttp 断点下载 end ***********************************/

    @NonNull
    private NetHelperInterface.IReq getOkHttpClientIReq(NetHelperInterface.OnResponseListener listener, Request request) {
        Call call = okHttpClient.newCall(request);
        OkRequest okRequest = new OkRequest(call);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("queryPost", e.getMessage());
                if (listener != null) {
                    listener.onResponse(0x002, false, e.getMessage(), e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                StringBuffer bodySB = new StringBuffer();
                bodySB.append(response.body().string());//注意response.body()只能string()一次
                Log.i("queryPost", bodySB.toString());
                if (listener != null) {
                    listener.onResponse(0x001, true, bodySB.toString(), "");
                }
            }
        });
        return okRequest;
    }


}
