package com.gst.mybaseapp.net.framework;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/****
 * OKhttp 绕开证书
 */
public class SSLSocketClient {


    //获取这个SSLSocketFactory
    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //获取TrustManager
    private static TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
        return trustAllCerts;
    }

    //获取HostnameVerifier
    public static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        return hostnameVerifier;
    }
}

//然后再okhttp中配置
//            mHttpClient = new OkHttpClient().newBuilder()
//            .connectTimeout(15, TimeUnit.SECONDS)
//            .readTimeout(15, TimeUnit.SECONDS)
//            .writeTimeout(15, TimeUnit.SECONDS)
//            .addInterceptor(new LogInterceptor())
//            .addInterceptor(new TokenInterceptor())
//            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())//配置
//            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置
//            .build();
//
//如果你用的是retrofit，在retrofit中配置一下okhttp即可
//        retrofitAPI = new Retrofit.Builder()
//        .baseUrl(AppConfig.baseUrl)
//        .addConverterFactory(GsonConverterFactory.create())
//        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//        .client(mHttpClient)//配置okhttp
//        .build()
//        .create(RetrofitAPI.class);
//
//        ---------------------
//        作者：Anonymous-OS
//        来源：CSDN
//        原文：https://blog.csdn.net/u014752325/article/details/73185351
//        版权声明：本文为博主原创文章，转载请附上博文链接！