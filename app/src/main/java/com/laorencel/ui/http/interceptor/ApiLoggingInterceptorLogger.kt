package com.laorencel.ui.http.interceptor

import com.laorencel.uilibrary.util.kt.log.logD
import okhttp3.logging.HttpLoggingInterceptor

class ApiLoggingInterceptorLogger : HttpLoggingInterceptor.Logger {
    private val msg = StringBuilder()
    override fun log(message: String) {
        if (message.startsWith("-->")) {
            //请求
            if (message.startsWith("--> END")) {
                //end
                msg.append(message)
                logD(msg.toString())
            } else {
                //start
                msg.clear()
                msg.append("request ").append(message).append("\n")
            }
        } else if (message.startsWith("<--")) {
            //返回结果
            if (message.startsWith("<-- END")) {
                //end
                msg.append(message)
                logD(msg.toString())
            } else {
                //start
                msg.clear()
                msg.append("response ").append(message).append("\n")
            }
        } else {
            msg.append("\t").append(message).append("\n")
        }
    }
}