package com.laorencel.ui

import com.laorencel.ui.bean.Device
import com.laorencel.uilibrary.util.kt.GsonUtil
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, (2 + 2).toLong())
    }


    @Test
    fun bannerTest() {
        val jsonText = "{\"device_id\":12851179483137,\"device_no\":\"AD1744797567\",\"device_name\":\"广告机\",\"device_type\":0,\"banners\":[{\"url\":\"https:\\/\\/cili-app.oss-cn-beijing.aliyuncs.com\\/ttq\\/files\\/20250416\\/67ff77f69f1e3.jpg\",\"type\":0},{\"url\":\"https:\\/\\/cili-app.oss-cn-beijing.aliyuncs.com\\/ttq\\/files\\/20250416\\/67ff78025af82.png\",\"type\":0}],\"banner_interval\":3,\"device_code\":\"AD1744797567_ads_end\",\"bind_device\":true}"
//        val device = Device()
//        device.banners = listOf(BannerBean(url = "https:\\/\\/cili-app.oss-cn-beijing.aliyuncs.com\\/ttg\\/files\\/20250416\\/67ff73bb7ed57.jpg"))
//        val json = GsonUtil.toJson(device)
        val device1 = GsonUtil.fromJson(jsonText, Device::class.java)
        println("device $device1")
    }
}