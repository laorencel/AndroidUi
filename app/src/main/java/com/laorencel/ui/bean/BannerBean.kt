package com.laorencel.ui.bean

import com.google.gson.annotations.SerializedName

data class BannerBean(
    var type: Int = 0, //0:图片;1:视频

    val url: String? = null, //'图片',

    val desc: String? = null, //'文字描述',

    val link: String? = null, //'跳转链接（关联），可为空，前段自定义',

    @SerializedName(value = "link_desc", alternate = ["linkDesc"])
    val linkDesc: String? = null, //'链接简述',
)
