package com.laorencel.ui.http

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.laorencel.uilibrary.http.DefaultHttpClientFactory
import com.laorencel.uilibrary.http.adapter.IntegerTypeAdapter
import com.laorencel.uilibrary.http.adapter.NullOnEmptyTypeAdapterFactory
import com.laorencel.uilibrary.http.adapter.StringTypeAdapter
import com.laorencel.uilibrary.http.converter.DefaultGsonConverterFactory
import com.laorencel.uilibrary.util.kt.TipUtil
import com.laorencel.uilibrary.util.kt.isEmpty
import com.laorencel.uilibrary.util.kt.log.logE
import com.laorencel.ui.http.interceptor.ApiLoggingInterceptorLogger
import com.laorencel.ui.http.interceptor.ApiRequestHeaderInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.SocketException
import java.net.UnknownHostException

object ApiRequest {

    private lateinit var gson: Gson

    private lateinit var api: Api
    private lateinit var commonApi: CommonApi

    fun getApi(): Api {
        if (!this::api.isInitialized) {
            val factories: MutableList<Converter.Factory> = mutableListOf()
            //GsonConverterFactory gson转换
            val gson = getGson()
            factories.add(DefaultGsonConverterFactory.create(gson))
            //factories.add(ApiGsonConverter.create(gson));
//            factories.add(GsonConverterFactory.create());
            //ScalarsConverterFactory 支持转为字符串 也就是说处理类型为String
            factories.add(ScalarsConverterFactory.create())
            val interceptors: MutableList<Interceptor> = mutableListOf()
            interceptors.add(ApiRequestHeaderInterceptor())
            //interceptors.add(new ApiResponseInterceptor());
            interceptors.add(
                HttpLoggingInterceptor(ApiLoggingInterceptorLogger()).setLevel(
                    HttpLoggingInterceptor.Level.BODY
                )
            )

            api = DefaultHttpClientFactory.create<Api>(
                Api::class.java,
                ApiUrl.API_ROOT,
                factories,
                interceptors
            )
        }
        return api
    }

    fun getCommonApi(): CommonApi {
        if (!this::commonApi.isInitialized) {
            val factories: MutableList<Converter.Factory> = mutableListOf()
            //GsonConverterFactory gson转换
            //Gson gson = getGson();
            //factories.add(DefaultGsonConverterFactory.create(gson));
            //factories.add(ApiGsonConverter.create(gson));
            factories.add(GsonConverterFactory.create())
            //ScalarsConverterFactory 支持转为字符串 也就是说处理类型为String
            factories.add(ScalarsConverterFactory.create())
            val interceptors: MutableList<Interceptor> = mutableListOf()
            //interceptors.add(new ApiRequestHeaderInterceptor());
            //interceptors.add(new ApiResponseInterceptor());
            interceptors.add(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))

            commonApi =
                DefaultHttpClientFactory.create<CommonApi>(
                    CommonApi::class.java,
                    "https:www.baidu.com/",
                    factories,
                    interceptors
                )
        }
        return commonApi
    }

    fun getGson(): Gson {
        if (!this::gson.isInitialized) {
            //setFieldNamingPolicy 设置序列字段的命名策略(
            // IDENTITY,//原始数据不进行任何转换,如：userName->userName
            // UPPER_CAMEL_CASE,//将原始数据转为大写,如：userName->UserName
            // UPPER_CAMEL_CASE_WITH_SPACES,//将原始数据转为大写并间隔一个空格,如：userName->User Name
            // LOWER_CASE_WITH_UNDERSCORES,//将原始数据转为小写下划线,如：userName->user_name
            // LOWER_CASE_WITH_DASHES//将原始数据转为小写中划线，如：userName->user-name
            // )
            gson = GsonBuilder()
                .setLenient() //字段名从下划线格式修改为驼峰式
                .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                //                .registerTypeAdapter(long.class, LongTypeAdapter)
                //                .registerTypeAdapter(Long.class, LongTypeAdapter)
                .registerTypeAdapter(Int::class.javaPrimitiveType, IntegerTypeAdapter())
                .registerTypeAdapter(Int::class.java, IntegerTypeAdapter())
                .registerTypeAdapter(String::class.java, StringTypeAdapter())
                .registerTypeAdapterFactory(NullOnEmptyTypeAdapterFactory())
                .create()
//            gson = Gson()
        }
        return gson
    }
}

/**
 * 网络请求包装函数
 * errorMsg：如果是null，则不显示提示，如果是"",则优先显示请求返回message，否则显示errorMsg
 * successMsg：如果是null，则不显示提示，如果是"",则优先显示请求返回message，否则显示successMsg
 */
suspend fun <T> request(
    errorMsg: String? = null,
    successMsg: String? = null,
    requestFunc: suspend () -> ApiResult<T>?
): ApiResult<T> = withContext(Dispatchers.IO) {
    try {
        var result = requestFunc()
        if (null == result) {
            result = ApiResult.failure(errorMsg ?: "请求失败")
        }
        if (result.isSuccess()) {
            if (null != successMsg) {
                val message =
                    if (!isEmpty(successMsg)) successMsg else ApiResult.getMsg(result, successMsg)
                logE("request isSuccess message: $message")
                if (message.length < 7) {
                    TipUtil.showToast(message)
                } else {
                    TipUtil.showConfirmDialog(content = message, autoCloseSeconds = 8)
                }
            }
        } else {
            if (null != errorMsg) {
                val message =
                    if (!isEmpty(errorMsg)) errorMsg else ApiResult.getMsg(result, errorMsg)
                logE("request is not Success message: $message")
                if (message.length < 7) {
                    TipUtil.showToast(message)
                } else {
                    TipUtil.showConfirmDialog(content = message, autoCloseSeconds = 8)
                }
            }
        }

        result
    } catch (e: Exception) {
        var message: String? = null
        if (e is SocketException) {
            message = "网络异常，请检查网络重试"
        } else if (e is UnknownHostException) {
            message = "网络异常，请稍后重试..."
        } else if (e is IllegalStateException) {
            message = "非法状态异常，请稍后重试..."
        } else if (e is JsonSyntaxException) {
            //常见情况是：后台不给null，而是给“”，导致gson解析错误，所以不需要提示
            message = "数据解析异常，请稍后重试..."
        } else if (e is HttpException) {
            //服务器返回的code不为成功
            message = "请求异常，请稍后重试..."
        } else {
            if (null == errorMsg) {
                message = e?.message
            }
        }
        logE("request Exception e: ${Log.getStackTraceString(e)} message: $message")
        message = message ?: errorMsg
        if (null != errorMsg) {
            if (message!!.length < 7) {
                TipUtil.showToast(message)
            } else {
                TipUtil.showConfirmDialog(content = message, autoCloseSeconds = 8)
            }
        }
        ApiResult.failure(message ?: "请求失败")

    }
}