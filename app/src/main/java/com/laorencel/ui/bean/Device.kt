package com.laorencel.ui.bean


data class Device(
    var deviceId: String? = null,

    var deviceName: String? = null,

    var deviceNo: String? = null,

    //是否已绑定设备
    var bindDevice: Boolean = false,

    //deviceCode:deviceNo+'_ads_end'//广告二维码
    var deviceCode: String? = null,

    //0:工作餐
    var deviceType: Int = 0,

    var storeId: String? = null,

    var storeName: String? = null,

    var storeAvatar: String? = null,

    //所属商户id
    var merchantId: String? = null,

    var merchantName: String? = null,

    var merchantAvatar: String? = null,

    var cleanTime: String? = null,

    var address: Address? = null,

    //banner轮训间隔（秒）
    var bannerInterval:Int? = 6,
    //banner列表 [{type:0  //0:图片;1:视频，url:'图片链接',desc:'文字描述',link:'跳转链接（关联），可为空，前段自定义',linkDesc:'链接简述',linkImages:'[]'}]
    var banners: List<BannerBean>? = null,
    //物流方式
    // delivery = {type: DeliveryType.SELF_PICK, scope: DeliveryScope.TO_C, extends: null};
    //     Map delivery = {};
    //是否营业（注意判断是否在营业中需要结合openingHours来判断）
    var opening: Boolean = false,
    //营业时间
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