package com.laorencel.ui

import com.laorencel.ui.bean.Device
import com.laorencel.ui.test.kt.frag.FragTitle
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
//        val jsonText = "{\"device_id\":13290463092737,\"device_no\":\"ZQ1751081607\",\"device_name\":\"蒸汽机\",\"device_type\":0,\"heat_type\":0,\"store_id\":9191292933,\"opening\":true,\"opening_hours\":{\"end\":\"23:59\",\"start\":\"00:00\"},\"address\":{\"id\":219,\"name\":\"鲜食荟园区\",\"province\":\"安徽省\",\"city\":\"合肥市\",\"area\":\"蜀山区\",\"province_code\":0,\"city_code\":0,\"area_code\":340104,\"detail_address\":\"蜀山区高新区浮山路33号\",\"lng\":117.150765,\"lat\":31.828649,\"consignee_mobile\":\"\",\"consignee_name\":\"\"},\"merchant_id\":9190768641,\"merchant_name\":\"鲜食荟园区-张平2\",\"merchant_avatar\":\"\",\"store_name\":\"鲜食荟园区-张平2\",\"clean_time\":\"2025-06-30 09:18:20\",\"banners\":[{\"url\":\"https:\\/\\/cili-app.oss-cn-beijing.aliyuncs.com\\/ttq\\/files\\/20250416\\/67ff77f69f1e3.jpg\",\"type\":0},{\"url\":\"https:\\/\\/cili-app.oss-cn-beijing.aliyuncs.com\\/ttq\\/files\\/20250416\\/67ff78025af82.png\",\"type\":0}]}"
//        val jsonText = "{\"device_id\":12851179483137,\"device_no\":\"AD1744797567\",\"device_name\":\"广告机\",\"device_type\":\"1\",\"banners\":[{\"url\":\"https:\\/\\/cili-app.oss-cn-beijing.aliyuncs.com\\/ttq\\/files\\/20250416\\/67ff77f69f1e3.jpg\",\"type\":0},{\"url\":\"https:\\/\\/cili-app.oss-cn-beijing.aliyuncs.com\\/ttq\\/files\\/20250416\\/67ff78025af82.png\",\"type\":0}],\"address\":{\"id\":219,\"name\":\"鲜食荟园区\",\"province\":\"安徽省\",\"city\":\"合肥市\",\"area\":\"蜀山区\",\"province_code\":0,\"city_code\":0,\"area_code\":340104,\"detail_address\":\"蜀山区高新区浮山路33号\",\"lng\":117.150765,\"lat\":31.828649,\"consignee_mobile\":\"\",\"consignee_name\":\"\"},\"banner_interval\":3,\"device_code\":\"AD1744797567_ads_end\",\"bind_device\":true}"
        val jsonText = "{\"deviceId\":\"13152108220417\",\"deviceNo\":\"WB1749102415\",\"deviceName\":\"微波炉售餐机\",\"deviceType\":1,\"heatType\":0,\"banners\":[{\"url\":\"https:\\/\\/cili-app.oss-cn-beijing.aliyuncs.com\\/ttq\\/files\\/20250416\\/67ff77f69f1e3.jpg\",\"type\":0},{\"url\":\"https:\\/\\/cili-app.oss-cn-beijing.aliyuncs.com\\/ttq\\/files\\/20250416\\/67ff78025af82.png\",\"type\":0}],\"bannerInterval\":0,\"deviceCode\":\"WB1749102415_ads_end\",\"bindDevice\":false,\"address\":{\"id\":219,\"name\":\"鲜食荟园区\",\"province\":\"安徽省\",\"city\":\"合肥市\",\"area\":\"蜀山区\",\"province_code\":0,\"city_code\":0,\"area_code\":340104,\"detailAddress\":\"蜀山区高新区浮山路33号\",\"lng\":117.150765,\"lat\":31.828649,\"consignee_mobile\":\"\",\"consignee_name\":\"\"}}"
//        val device = Device()
//        device.banners = listOf(BannerBean(url = "https:\\/\\/cili-app.oss-cn-beijing.aliyuncs.com\\/ttg\\/files\\/20250416\\/67ff73bb7ed57.jpg"))
//        val json = GsonUtil.toJson(device)
        val device1 = GsonUtil.fromJson(jsonText, Device::class.java)
        println("device $device1")
    }

    @Test
    fun bannerTest1() {
        val jsonText = "[{\"checked\":false,\"tag\":\"tag2\",\"title\":2},{\"checked\":false,\"tag\":\"tag3\",\"title\":\"title3\"}]"
//        val device = Device()
//        device.banners = listOf(BannerBean(url = "https:\\/\\/cili-app.oss-cn-beijing.aliyuncs.com\\/ttg\\/files\\/20250416\\/67ff73bb7ed57.jpg"))
//        val json = GsonUtil.toJson(device)
        val device1 = GsonUtil.fromJsonList(jsonText, FragTitle::class.java)
        println("device $device1")
    }
}