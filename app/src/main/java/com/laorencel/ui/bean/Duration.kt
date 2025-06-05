package com.laorencel.ui.bean

import com.laorencel.uilibrary.util.kt.DateUtil
import com.laorencel.uilibrary.util.kt.isEmpty

data class Duration(
    var start: String? = null,
    var end: String? = null,
) {

    /**
     * 根据start end时间判断当前时间是否在这个当前时间段内
     * @return
     */
    fun isInDuration(): Boolean {
        if (!isEmpty(this.start) && !isEmpty(this.end)) {
            return DateUtil.isInTimePeriod(this.start, this.end)
        }
        return false
    }
}