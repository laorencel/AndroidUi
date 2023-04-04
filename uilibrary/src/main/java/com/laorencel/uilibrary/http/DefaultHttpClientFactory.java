package com.laorencel.uilibrary.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.laorencel.uilibrary.http.adapter.IntegerTypeAdapter;
import com.laorencel.uilibrary.http.adapter.NullOnEmptyTypeAdapterFactory;
import com.laorencel.uilibrary.http.adapter.StringTypeAdapter;
import com.laorencel.uilibrary.http.converter.DefaultGsonConverterFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 默认的网络请求工厂，作为一个实例，实际项目中可以按此编写
 */
public class DefaultHttpClientFactory {

    private DefaultHttpClientFactory() {
    }

    private static class Builder {
        private static final DefaultHttpClientFactory INSTANCE = new DefaultHttpClientFactory();
    }

    public static DefaultHttpClientFactory get() {
        return DefaultHttpClientFactory.Builder.INSTANCE;
    }

    //实现多个api存储，以baseUrl为key
    private Map<String, Object> clientMap = new HashMap<>();

    private Gson gson;
    public Object getHttpClient(String key) {
        Object api = clientMap.get(key);
        return api;
    }

    public <T> T create(Class<T> apiClass, String baseUrl) {
        List<Converter.Factory> factories = new ArrayList<>();
        //GsonConverterFactory gson转换
        Gson gson = getGson();
        factories.add(DefaultGsonConverterFactory.create(gson));
//        factories.add(GsonConverterFactory.create());
        //ScalarsConverterFactory 支持转为字符串 也就是说处理类型为String
        factories.add(ScalarsConverterFactory.create());
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        return create(apiClass, baseUrl, factories, interceptors);
    }

    public <T> T create(Class<T> apiClass, String baseUrl, List<Converter.Factory> factories, List<Interceptor> interceptors) {
        Object obj = clientMap.get(baseUrl);

        if (null != obj) {
            return (T) obj;
        }

        T api = RetrofitClient.get().createRetrofit(baseUrl, factories, interceptors).create(apiClass);
        clientMap.put(baseUrl, api);
        return api;
    }

    private Gson getGson() {
        if (null == gson){
            gson = new GsonBuilder()
                    .setLenient()
//                .registerTypeAdapter(long.class, LongTypeAdapter)
//                .registerTypeAdapter(Long.class, LongTypeAdapter)
                    .registerTypeAdapter(int.class, new IntegerTypeAdapter())
                    .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
                    .registerTypeAdapter(String.class, new StringTypeAdapter())
                    .registerTypeAdapterFactory(new NullOnEmptyTypeAdapterFactory())
                    .create();
        }
        return gson;
    }
}
