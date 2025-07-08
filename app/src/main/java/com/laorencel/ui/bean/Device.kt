package com.laorencel.ui.bean

import com.google.gson.annotations.SerializedName


data class Device(
    @SerializedName(value = "device_id", alternate = ["deviceId","DeviceId"])
    var deviceId: String? = null,

    @SerializedName(value = "device_name", alternate = ["deviceName"])
    var deviceName: String? = null,

    @SerializedName(value = "device_no", alternate = ["deviceNo"])
    var deviceNo: String? = null,

    //是否已绑定设备
    @SerializedName(value = "bind_device", alternate = ["bindDevice"])
    var bindDevice: Boolean = false,

    //deviceCode:deviceNo+'_ads_end'//广告二维码
    @SerializedName(value = "device_code", alternate = ["deviceCode"])
    var deviceCode: String? = null,

    //0:工作餐
    @SerializedName(value = "device_type", alternate = ["deviceType"])
    var deviceType: Int = 0,

    @SerializedName(value = "store_id", alternate = ["storeId"])
    var storeId: String? = null,

    @SerializedName(value = "store_name", alternate = ["storeName"])
    var storeName: String? = null,

    @SerializedName(value = "store_avatar", alternate = ["storeAvatar"])
    var storeAvatar: String? = null,

    //所属商户id
    @SerializedName(value = "merchant_id", alternate = ["merchantId"])
    var merchantId: String? = null,

    @SerializedName(value = "merchant_name", alternate = ["merchantName"])
    var merchantName: String? = null,

    @SerializedName(value = "merchant_avatar", alternate = ["merchantAvatar"])
    var merchantAvatar: String? = null,

    @SerializedName(value = "clean_time", alternate = ["cleanTime"])
    var cleanTime: String? = null,

    @SerializedName(value = "address")
    var address: Address? = null,

    //banner轮训间隔（秒）
    @SerializedName(value = "banner_interval", alternate = ["bannerInterval"])
    var bannerInterval:Int? = 6,
    //banner列表 [{type:0  //0:图片;1:视频，url:'图片链接',desc:'文字描述',link:'跳转链接（关联），可为空，前段自定义',linkDesc:'链接简述',linkImages:'[]'}]
    @SerializedName(value = "banners")
    var banners: List<BannerBean>? = null,
    //物流方式
    // delivery = {type: DeliveryType.SELF_PICK, scope: DeliveryScope.TO_C, extends: null};
    //     Map delivery = {};
    //是否营业（注意判断是否在营业中需要结合openingHours来判断）
    @SerializedName(value = "opening")
    var opening: Boolean = false,
    //营业时间
    @SerializedName(value = "opening_hours", alternate = ["openingHours"])
    var openingHours: Duration? = null,//{start:'00:00',end:'23:59'};
) {

    fun isInOpening(): Boolean {
        if (this.opening && null != this.openingHours) {
            return openingHours!!.isInDuration()
        }
        return this.opening
    }

    fun getAddressName(): String? {
            return address?.name
    }

    fun getAddressDetail(): String? {
            return address?.detailAddress
    }
}