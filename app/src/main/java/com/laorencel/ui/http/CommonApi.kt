package com.laorencel.ui.http

import com.laorencel.ui.http.ApiRequest
import com.laorencel.ui.http.ApiResult
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface CommonApi {
    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ApiRequest.getCommonApi()
        }
    }

    /**
     * app检查版本更新 详见 https://www.pgyer.com/doc/view/api
     *
     * @param url 路径：https://www.pgyer.com/apiv2/app/check
     * @param map _api_key	String	(必填) API Key，请见 鉴权说明
     * appKey	String	(必填) 见 appKey
     * buildVersion	String	(选填) 使用 App 本身的 Build 版本号，Android 对应字段为 versionname, iOS 对应字段为 version
     * buildBuildVersion	Integer	(选填) 使用蒲公英生成的自增 Build 版本号
     * channelKey	String	(选填) 渠道 KEY
     * @return
     */
    @FormUrlEncoded
    @POST
    suspend fun checkUpdateByPgy(
        @Url url: String?,
        @FieldMap map: MutableMap<String, Any?>
    ): ApiResult<Map<String, Any?>>?


    /**
     * 下载文件
     * 如果下载大文件的一定要加上  @Streaming  注解
     *
     * @param url 文件的路径
     * @return 请求call
     */
    @GET
    suspend fun download(@Url url: String?): ResponseBody?

    //    /**
//     * 上传
//     * Multipart 这个注解代表多表单上传
//     * @param partList 表单信息
//     * @return .
//     */
//    @Multipart
//    @POST(ApiUrl.UPLOAD_FILE)
//    suspend fun multiUpload(@Part partList: List<MultipartBody.Part?>?): ApiResult<*>?
    @Multipart
    @POST
    suspend fun multiUpload(
        @Url url: String,
        @Part partList: List<MultipartBody.Part?>?
    ): ApiResult<*>?

    /**
     * 上传
     * Multipart 这个注解代表多表单上传
     * @param part 表单信息
     * @return .
     */
//    @Multipart
//    @POST(ApiUrl.UPLOAD_FILE)
//    suspend fun upload(@Part part: MultipartBody.Part?): ApiResult<Map<String,Any?>>?
    @Multipart
    @POST
    suspend fun upload(
        @Url url: String,
        @Part part: MultipartBody.Part?
    ): ApiResult<Map<String, Any?>>?

}