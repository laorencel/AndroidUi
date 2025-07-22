package com.laorencel.ui.http.interceptor

import com.laorencel.ui.config.AppConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiRequestHeaderInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {


//        Request request = chain.request();
        //处理业务逻辑，可以对header统一处理，涉及到header加密的也在此处理
        val builder = chain.request().newBuilder()
        builder.addHeader("Request-Source", AppConfig.REQUEST_SOURCE)
        builder.addHeader("Device-Version", AppConfig.DEVICE_VERSION)
        builder.addHeader("Device-Type", AppConfig.DEVICE_TYPE)


        //        if (!EmptyUtil.isEmpty(Account.instance.getToken())) {
//            builder.addHeader("token", Account.instance.getToken());
//        }
        return chain.proceed(builder.build())
    }
}