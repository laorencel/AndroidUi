package com.laorencel.ui.bean

data class Address(
    var id: String? = null,

    var name: String? = null,//地址名称;

    var detailAddress: String? = null,//详细地址

    var province: String? = null,

    var city: String? = null,

    var area: String? = null,

    var provinceCode: String? = null,

    var cityCode: String? = null,

    var areaCode: String? = null,

    var type: Int = 0,//AddrType.SHIPPING_ADDR;//地址类型 1：用户收货地址，2：团长自提点地址，

    var lat: Double = 0.0,//纬度;

    var lng: Double = 0.0,//经度;

    var isDefault: Boolean? = false, //是否为默认地址，0：否；1：是;
) {
}