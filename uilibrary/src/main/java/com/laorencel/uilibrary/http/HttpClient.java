package com.laorencel.uilibrary.http;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class HttpClient {
    private HttpClient() {
    }

    private static class Builder {
        private static final HttpClient INSTANCE = new HttpClient();
    }

    public static HttpClient get() {
        return Builder.INSTANCE;
    }

    private static final Long CONNECT_TIMEOUT = 10L, READ_TIMEOUT = 10L, WRITE_TIMEOUT = 10L;

    public Retrofit createRetrofit(String baseUrl, List<Converter.Factory> factories, List<Interceptor> interceptors) {
        //        new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
//                //支持RxJava
//                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
//                .client(createOkHttpClient())
//                .build();
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(baseUrl);
        if (null != factories && factories.size() > 0) {
            for (int i = 0; i < factories.size(); i++) {
                builder.addConverterFactory(factories.get(i));
            }
        }
        builder.client(createOkHttpClient(interceptors));
        return builder.build();
    }

    public OkHttpClient createOkHttpClient(List<Interceptor> interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (null != interceptors && interceptors.size() > 0) {
            for (int i = 0; i < interceptors.size(); i++) {
                builder.addInterceptor(interceptors.get(i));
            }
        }
        return builder
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
//        return new OkHttpClient.Builder()
//                //添加log拦截器
//                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) //okhttp默认的
//                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
//                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
//                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
//                .build();
    }
}
