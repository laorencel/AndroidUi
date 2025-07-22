package com.laorencel.ui.http

import com.laorencel.ui.bean.Device
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface Api {
    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ApiRequest.getApi()
        }
    }

    @GET(ApiUrl.GET_DEVICE_INFO)
    suspend fun getDevice(@Query("device_mac") deviceMac: String?): ApiResult<Device?>?


    @FormUrlEncoded
    @POST(ApiUrl.USE_SCAN_CODE)
    suspend fun useScanCode(@Field("id") id: String?,@Field("deviceId") deviceId: String?): ApiResult<Map<String, @JvmSuppressWildcards Any?>?>?

    @FormUrlEncoded
    @POST(ApiUrl.GET_PICKUP_INFO_BY_CODE)
    suspend fun getPickupInfoByCode(@Field("id") id: String?,@Field("deviceId") deviceId: String?): ApiResult<Map<String, @JvmSuppressWildcards Any?>?>?

    /**
     * 上传设备信息，用于替代socket通信
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(ApiUrl.UPDATE_DEVICE_MESSAGE)
    suspend fun updateDeviceMessage(@FieldMap map: Map<String,@JvmSuppressWildcards Any?>): ApiResult<*>?

    /**
     * 上传日志
     *
     * @param map {deviceId,name:"log.txt",url:"https://xxx"}
     * @return
     */
    @FormUrlEncoded
    @POST(ApiUrl.UPLOAD_LOG)
    suspend fun uploadLog(@FieldMap map: MutableMap<String,@JvmSuppressWildcards Any?>): ApiResult<*>?

    /**
     * 更新系统设置参数
     *
     * @param map device_mac 设备id
     * command_argument 设置参数列表
     * @return
     */
    @FormUrlEncoded
    @POST(ApiUrl.UPDATE_SYSTEM_SETTING)
    suspend fun updateSystemSetting(@FieldMap map: Map<String?,@JvmSuppressWildcards Any?>?): ApiResult<*>?

    /**
     * 更新系统设置参数
     *
     * @param map device_mac 设备id
     * command_argument 设置参数列表
     * @return
     */
    @FormUrlEncoded
    @POST(ApiUrl.SET_DEVICE_TYPE)
    suspend fun setDeviceType(@FieldMap map: Map<String?,@JvmSuppressWildcards Any?>?): ApiResult<*>?

    /**
     * 获取系统设置参数
     *
     * @param deviceMac 设备id
     * @return
     */
    @GET(ApiUrl.GET_SYSTEM_SETTING)
    suspend fun getSystemSetting(@Query("device_mac") deviceMac: String?): ApiResult<String?>?

    /**
     * 更新设备加水配置
     *
     * @param map deviceId 设备id
     * addWaterType 加水类型:1流量加水2计时加水
     * waterPerSecond 计时加水 每秒加水量（单位：毫升ml）
     * @return
     */
    @FormUrlEncoded
    @POST(ApiUrl.UPDATE_ADD_WATER_CONFIG)
    suspend fun updateAddWaterConfig(@FieldMap map: Map<String?,@JvmSuppressWildcards Any?>?): ApiResult<*>?


    /**
     * app检查版本更新 使用本身服务器
     * @param map version_code: 版本号 int,
     * platform: 平台："android","ios",
     * request_source: "",
     * @return
     */
    @GET(ApiUrl.CHECK_UPDATE_BY_SERVE)
    suspend fun checkUpdateByServe(@QueryMap map: MutableMap<String, @JvmSuppressWildcards Any?>): ApiResult<Map<String, @JvmSuppressWildcards Any?>>?

}