package com.laorencel.ui.http

import com.laorencel.ui.config.AppConfig


object ApiUrl {

//    //服务器域名
//    //    public  final String API_ROOT = "https://api.gateway.c-ooo.com/";//测试服
//    const val API_ROOT: String = "https://api.gateway.cili-app.com/" //正式服
//
//    const val API_ROOT_PUBLIC: String = "https://public.api.cili-app.com/" //正式服
//
//    //websocket服务器域名
//    //    private  final String SOCKET_URL = "wss://wss.c-ooo.com/wss/DeviceWebSocket";//测试服
//    const val SOCKET_URL: String = "wss://wss.cili-app.com/wss/DeviceWebSocket" //正式服

    //服务器域名
    //测试服
    const val API_ROOT_DEV: String = "https://api.gateway.c-ooo.com/" //测试服
    const val API_ROOT_PUBLIC_DEV: String = "https://upload.c-ooo.com/" //测试服

    //正式服
    const val API_ROOT_PROD: String = "https://api.gateway.cili-app.com/" //正式服
    const val API_ROOT_PUBLIC_PROD: String = "https://public.api.cili-app.com/" //正式服

    //websocket服务器域名
    const val SOCKET_URL_DEV: String = "wss://wss.c-ooo.com/wss/DeviceWebSocket" //测试服
    const val SOCKET_URL_PROD: String = "wss://wss.cili-app.com/wss/DeviceWebSocket" //正式服

//    val API_ROOT: String =
//        if (AppConfig.IS_SERVE_DEV) API_ROOT_DEV else API_ROOT_PROD //请求域名
//    val API_ROOT_PUBLIC: String =
//        if (AppConfig.IS_SERVE_DEV) API_ROOT_PUBLIC_DEV else API_ROOT_PUBLIC_PROD //公共请求域名
//    val SOCKET_ROOT: String =
//        if (AppConfig.IS_SERVE_DEV) SOCKET_URL_DEV else SOCKET_URL_PROD //socket域名

    val API_ROOT: String
        get() {
            return if (AppConfig.IS_SERVE_DEV) API_ROOT_DEV else API_ROOT_PROD //请求域名
        }

    val API_ROOT_PUBLIC: String
        get() {
            return if (AppConfig.IS_SERVE_DEV) API_ROOT_PUBLIC_DEV else API_ROOT_PUBLIC_PROD
        } //公共请求域名}

    val SOCKET_ROOT: String
        get() {
            return if (AppConfig.IS_SERVE_DEV) SOCKET_URL_DEV else SOCKET_URL_PROD //socket域名
        }

    private const val API: String = "api/v1/"
    const val WX_QRCODE_PATH: String = "https://admin.cili-app.com/uni-share/afk-xsh" //微信二维码跳转

    //app检查版本更新（使用蒲公英api）
    const val CHECK_UPDATE_BY_PGY: String = "https://www.pgyer.com/apiv2/app/check"

    const val INSTALL_BY_PGY: String = "https://www.pgyer.com/apiv2/app/install"

    //上位机获取该设备需要展示的信息
    const val GET_DEVICE_INFO: String =
        API + "device-center-service/device/get-device-show-info"

    //使用二维码（取代使用取货码取餐接口）
    const val USE_SCAN_CODE: String = API + "device-center-service/device/scan-code"

    //使用取货码取餐（废弃）
    const val GET_PICKUP_INFO_BY_CODE: String =
        API + "device-center-service/device/use-pickup-code"

    //更新系统参数设置
    const val UPDATE_SYSTEM_SETTING: String =
        API + "device-center-service/device/update-system-setting"

    //更新设备类型（小型机用）
    const val SET_DEVICE_TYPE: String =
        API + "device-center-service/device/set-device-type"

    //获取系统参数设置
    const val GET_SYSTEM_SETTING: String =
        API + "device-center-service/device/get-system-setting"

    //更新设备加水配置
    const val UPDATE_ADD_WATER_CONFIG: String =
        API + "device-center-service/device/setting-device-add-water-config"
    //获取设备加水配置
    const val GET_ADD_WATER_CONFIG: String =
        API + "device-center-service/device/get-device-add-water-config"

    //上传设备通讯信息
    const val UPDATE_DEVICE_MESSAGE: String =
        API + "device-center-service/device/get-device-message"

    //上传日志
    const val UPLOAD_LOG: String = API + "device-center-service/device/upload-log"

    //文件上传
    var UPLOAD_FILE: String = API_ROOT_PUBLIC + "admin/upload/upload-file"

    //app检查版本更新（使用服务器）
    const val CHECK_UPDATE_BY_SERVE: String = API + "order-center-service/app-version"

}