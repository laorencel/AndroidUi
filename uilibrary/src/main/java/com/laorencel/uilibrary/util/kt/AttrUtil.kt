package com.laorencel.uilibrary.util.kt

import android.content.Context
import android.util.TypedValue

object AttrUtil {

    /**
     * 获取Android的主题属性
     */
    fun getAttrData(context: Context, attr: Int, resolveRefs: Boolean = true): Int {
        val typedValue = TypedValue()
        // 第三个参数true，实际上我看不太懂是什么作用。
        // 猜测：传入的id可能是一个引用，是否要继续解析该引用。
        // 文档中这么描述：
        /*
           If true, resource references will be walked; if
                             false, <var>outValue</var> may be a
                             TYPE_REFERENCE.  In either case, it will never
                             be a TYPE_ATTRIBUTE.
           */
//        context.theme.resolveAttribute(R.attr.colorBackground, typedValue, true)
        context.theme.resolveAttribute(attr, typedValue, resolveRefs)
        return typedValue.data
    }
}