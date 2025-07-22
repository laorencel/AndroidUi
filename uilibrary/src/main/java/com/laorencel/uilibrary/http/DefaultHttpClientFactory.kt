package com.laorencel.uilibrary.http

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.laorencel.uilibrary.http.RetrofitClient.createRetrofit
import com.laorencel.uilibrary.http.adapter.IntegerTypeAdapter
import com.laorencel.uilibrary.http.adapter.NullOnEmptyTypeAdapterFactory
import com.laorencel.uilibrary.http.adapter.StringTypeAdapter
import com.laorencel.uilibrary.http.converter.DefaultGsonConverterFactory
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * 默认的网络请求工厂，作为一个实例，实际项目中可以按此编写
 */
object DefaultHttpClientFactory {

    //实现多个api存储，以baseUrl为key
    private val clientMap: MutableMap<String, Any> = HashMap()

    private var gson: Gson? = null
        get() {
            if (null == field) {
                field = GsonBuilder()
                    .setLenient() //                .registerTypeAdapter(long.class, LongTypeAdapter)
                    //                .registerTypeAdapter(Long.class, LongTypeAdapter)
                    .registerTypeAdapter(Int::class.javaPrimitiveType, IntegerTypeAdapter())
                    .registerTypeAdapter(Int::class.java, IntegerTypeAdapter())
                    .registerTypeAdapter(String::class.java, StringTypeAdapter())
                    .registerTypeAdapterFactory(NullOnEmptyTypeAdapterFactory())
                    .create()
            }
            return field
        }

    fun getHttpClient(key: String): Any? {
        val api = clientMap[key]
        return api
    }

    fun <T> create(apiClass: Class<T>, baseUrl: String): T {
        val factories: MutableList<Converter.Factory> = ArrayList()
        //GsonConverterFactory gson转换
        val gson = gson
        factories.add(DefaultGsonConverterFactory.create(gson))
        //        factories.add(GsonConverterFactory.create());
        //ScalarsConverterFactory 支持转为字符串 也就是说处理类型为String
        factories.add(ScalarsConverterFactory.create())
        val interceptors: MutableList<Interceptor> = ArrayList()
        interceptors.add(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return create(apiClass, baseUrl, factories, interceptors)
    }

    fun <T> create(
        apiClass: Class<T>,
        baseUrl: String,
        factories: List<Converter.Factory>?,
        interceptors: List<Interceptor>?
    ): T {
        val obj = clientMap[apiClass.name + "_" + baseUrl]

        if (null != obj) {
            return obj as T
        }

        val api:T = createRetrofit(baseUrl, factories, interceptors, 30, 30, 30).create(apiClass)
        clientMap[apiClass.name + "_" + baseUrl] = api as Any
        return api
    }

}
