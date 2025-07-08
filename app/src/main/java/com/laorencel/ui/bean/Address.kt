package com.laorencel.ui.bean

import com.google.gson.annotations.SerializedName

data class Address(
    var id: String? = null,

    var name: String? = null,//地址名称;

    @SerializedName(value = "detail_address")
    var detailAddress: String? = null,//详细地址

    var province: String? = null,

    var city: String? = null,

    var area: String? = null,

    @SerializedName(value = "province_code")
    var provinceCode: String? = null,

    @SerializedName(value = "city_code")
    var cityCode: String? = null,

    @SerializedName(value = "area_code")
    var areaCode: String? = null,

    var type: Int = 0,//AddrType.SHIPPING_ADDR;//地址类型 1：用户收货地址，2：团长自提点地址，

    var lat: Double = 0.0,//纬度;

    var lng: Double = 0.0,//经度;

    @SerializedName(value = "is_default",)
    var isDefault: Boolean? = false, //是否为默认地址，0：否；1：是;
) {
}