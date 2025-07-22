package com.laorencel.ui.http

import com.laorencel.uilibrary.util.kt.isEmpty

data class ApiResult<T>(
    var code: Int? = FAILURE,
    var message: String?,
    var data: T?,
    var meta: Map<String?, Any?>? = null
) {

    constructor() : this(FAILURE, null, null, null) {

    }

    companion object {
        const val SUCCESS = 200
        const val FAILURE = 500

        fun <T> success(
            data: T? = null,
            message: String? = null,
            code: Int? = SUCCESS,
        ): ApiResult<T> {
            val result: ApiResult<T> = ApiResult<T>()
            result.code = code
            result.message = message
            result.data = data
            return result
        }

        fun <T> failure(message: String?): ApiResult<T> {
            val result = ApiResult<T>()
            result.code = FAILURE
            result.message = message
            return result
        }

        fun <T> failure(
            message: String? = null,
            code: Int? = FAILURE,
            data: T? = null,
        ): ApiResult<T> {
            val result: ApiResult<T> = ApiResult<T>()
            result.code = code
            result.message = message
            result.data = data
            return result
        }

        fun isSuccess(result: ApiResult<*>?): Boolean {
            return result?.code == SUCCESS
        }

        /**
         * 获取接口返回信息msg
         *
         * @param apiResult  ApiResult
         * @param defaultMsg 默认信息，msg为空时返回
         * @return msg
         */
        fun getMsg(apiResult: ApiResult<*>?, defaultMsg: String? = null): String {
            if (!isEmpty(apiResult) && !isEmpty(apiResult?.message)) {
                return apiResult?.message!!
            }
            return defaultMsg ?: ""
        }
    }

    fun isSuccess(): Boolean {
        return this.code == SUCCESS
    }
}