package com.laorencel.ui.config

import com.laorencel.ui.BuildConfig

object AppConfig {

    const val APP_NAME: String = "Integrate Machine"

    const val REQUEST_SOURCE: String = "integrate-app-qingsong"
    const val VERSION_CODE: Int = BuildConfig.VERSION_CODE

    const val DEVICE_VERSION: String = "v300"
    const val DEVICE_TYPE: String = "integrate"


    //使用测试服
    var IS_SERVE_DEV: Boolean = false


    //蒲公英分发平台apiKey（平台key）
    const val PGY_API_KEY: String = "b21e1562905a35e9f4a5d591223c90fb"

    //蒲公英分发平台appKey（应用key）
    const val PGY_APP_KEY: String = "ae5bfe0c40fbd8ec8ca5abef988ababf"

    const val DEFAULT_AVATAR: String =
        "https://cili-app.oss-cn-beijing.aliyuncs.com/ttq/images/20240415/661cd0db33d0d.png"

    const val DEFAULT_BANNER1: String =
        "https://cili-app.oss-cn-beijing.aliyuncs.com/ttq/images/20240913/66e400eb5727a.jpg"

    const val FILE_SCHEDULE_PRODUCE_LIST: String = "ScheduleProduceList.txt"

    const val PASSWORD_LOCAL: String = "951753"
}