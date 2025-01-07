package com.laorencel.uilibrary.util.kt

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object DateUtil {

    //格式化
    const val FORMAT_YYYY_MM_DD_HH_MM_SS: String = "yyyy-MM-dd HH:mm:ss"

    const val FORMAT_YYYY_MM_DD_HH_MM: String = "yyyy-MM-dd HH:mm"

    const val FORMAT_YYYY: String = "yyyy"

    const val FORMAT_YYYY_MM: String = "yyyy-MM"

    const val FORMAT_YYYY_MM_DD: String = "yyyy-MM-dd"

    const val FORMAT_MM_DD_HH_MM_SS: String = "MM-dd HH:mm:ss"

    const val FORMAT_MM_DD_HH_MM: String = "MM-dd HH:mm"

    //中文 年月日
    const val FORMAT_YYYY_MM_DD_HH_MM_SS_ZN: String = "yyyy年MM月dd日 HH:mm:ss"

    const val FORMAT_YYYY_MM_DD_HH_MM_ZN: String = "yyyy年MM月dd日 HH:mm"

    const val FORMAT_YYYY_ZN: String = "yyyy年"

    const val FORMAT_YYYY_MM_ZN: String = "yyyy年MM月"

    const val FORMAT_YYYY_MM_DD_ZN: String = "yyyy年MM月dd日"

    const val FORMAT_MM_DD_HH_MM_SS_ZN: String = "MM月dd日 HH:mm:ss"

    const val FORMAT_MM_DD_HH_MM_ZN: String = "MM月dd日 HH:mm"

    //时间（不含日期）
    const val FORMAT_MM_DD: String = "MM-dd"

    const val FORMAT_HH_MM_SS: String = "HH:mm:ss"

    const val FORMAT_HH_MM: String = "HH:mm"

    /**
     * 时间戳格式化
     *
     * @param millis 时间戳
     * @param format 格式化，如：yyyy-MM-dd HH:mm:ss
     */
    fun millisToString(millis: Long, format: String?): String {
        val date = Date(millis)
        val sdf = SimpleDateFormat(format)
        return sdf.format(date)
    }

    fun millisToString(millis: Long): String {
        val date = Date(millis)
        val sdf = SimpleDateFormat(FORMAT_YYYY_MM_DD_HH_MM_SS)
        return sdf.format(date)
    }

    fun millisToDate(millis: Long): Date {
        return Date(millis)
    }

    fun StringToDate(dateStr: String?, format: String?): Date? {
        //String和DateFormat样式需一样，不然无法解析
        val df: DateFormat = SimpleDateFormat(format)
        var date: Date? = null
        try {
            date = df.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    fun StringToMillis(dateStr: String?, format: String?): Long {
        //String和DateFormat样式需一样，不然无法解析
        val df: DateFormat = SimpleDateFormat(format)
        var date: Date? = null
        try {
            date = df.parse(dateStr)
            if (null != date) {
                return date.time
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }

    fun DateToString(date: Date?, format: String?): String {
        val sdf = SimpleDateFormat(format)
        return sdf.format(date)
    }

    fun timeToDate(time: String?): Date {
        // 时间转为日期，时间格式必须是'HH:mm'或者'HH:mm:ss'
        val startCalendar = Calendar.getInstance()
        if (null != time) {
            val startTime = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val startHour = startTime[0]
            val startMin = startTime[1]
            if (startTime.size > 2) {
                val startSecond = startTime[2]
                startCalendar[Calendar.SECOND] = startSecond.toInt()
            }
            startCalendar[Calendar.HOUR_OF_DAY] = startHour.toInt()
            startCalendar[Calendar.MINUTE] = startMin.toInt()
        }
        return startCalendar.time
    }

    /**
     * 是否为同一个时间
     * @param date1
     * @param date2
     * @param field Calendar.field
     * @return
     */
    fun isSameDate(date1: Date?, date2: Date?, field: Int): Boolean {
        if (isEmpty(date1) || isEmpty(date2)) {
            return false
        }
        val calendar1 = Calendar.getInstance()
        val calendar2 = Calendar.getInstance()
        calendar1.time = date1
        calendar2.time = date2
        when (field) {
            Calendar.YEAR -> return calendar1[Calendar.YEAR] == calendar2[Calendar.YEAR]
            Calendar.MONTH -> return (calendar1[Calendar.YEAR] == calendar2[Calendar.YEAR]
                    && calendar1[Calendar.MONTH] == calendar2[Calendar.MONTH])

            Calendar.DATE -> return calendar1[Calendar.YEAR] == calendar2[Calendar.YEAR] && calendar1[Calendar.MONTH] == calendar2[Calendar.MONTH] && calendar1[Calendar.DATE] == calendar2[Calendar.DATE]
            Calendar.HOUR_OF_DAY -> return calendar1[Calendar.YEAR] == calendar2[Calendar.YEAR] && calendar1[Calendar.MONTH] == calendar2[Calendar.MONTH] && calendar1[Calendar.DATE] == calendar2[Calendar.DATE] && calendar1[Calendar.HOUR_OF_DAY] == calendar2[Calendar.HOUR_OF_DAY]
            Calendar.MINUTE -> return calendar1[Calendar.YEAR] == calendar2[Calendar.YEAR] && calendar1[Calendar.MONTH] == calendar2[Calendar.MONTH] && calendar1[Calendar.DATE] == calendar2[Calendar.DATE] && calendar1[Calendar.HOUR_OF_DAY] == calendar2[Calendar.HOUR_OF_DAY] && calendar1[Calendar.MINUTE] == calendar2[Calendar.MINUTE]
            Calendar.SECOND -> return calendar1[Calendar.YEAR] == calendar2[Calendar.YEAR] && calendar1[Calendar.MONTH] == calendar2[Calendar.MONTH] && calendar1[Calendar.DATE] == calendar2[Calendar.DATE] && calendar1[Calendar.HOUR_OF_DAY] == calendar2[Calendar.HOUR_OF_DAY] && calendar1[Calendar.MINUTE] == calendar2[Calendar.MINUTE] && calendar1[Calendar.SECOND] == calendar2[Calendar.SECOND]
            Calendar.MILLISECOND -> return calendar1[Calendar.YEAR] == calendar2[Calendar.YEAR] && calendar1[Calendar.MONTH] == calendar2[Calendar.MONTH] && calendar1[Calendar.DATE] == calendar2[Calendar.DATE] && calendar1[Calendar.HOUR_OF_DAY] == calendar2[Calendar.HOUR_OF_DAY] && calendar1[Calendar.MINUTE] == calendar2[Calendar.MINUTE] && calendar1[Calendar.SECOND] == calendar2[Calendar.SECOND] && calendar1[Calendar.MILLISECOND] == calendar2[Calendar.MILLISECOND]
        }
        return false
    }

    /**
     * 日期增加或减少
     *
     * @param date   日期
     * @param amount 加减数量
     * @param field  Calendar.field
     * @return
     */
    fun add(date: Date?, amount: Int, field: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(field, amount)
        return calendar.time
    }

    fun now(): Date {
        return Date()
    }

    fun isInTimePeriod(startTime: String?, endTime: String?): Boolean {
        //startTime:'08:00',endTime:'20:00';
        //当前时间是否在某个时间段内，比如8:00~20:00
        if (!isEmpty(startTime) && !isEmpty(endTime)) {
            val now = Date()
            val start = timeToDate(startTime)
            var end = timeToDate(endTime)
            var startOfDay = timeToDate("00:00:00")
            val endOfDay = timeToDate("23:59:59")
            if (end.before(start)) {
                //如果不是同一天，end和startOfDay日期向后推1天，然后根据当前日期判断
                end = add(end, 1, Calendar.DATE)
                startOfDay = add(startOfDay, 1, Calendar.DATE)
            }
            return if (isSameDate(start, end, Calendar.DATE)) {
                //开始时间和结束时间在同一天内，如：8:00~20:00
                start.before(now) && now.before(end)
            } else {
                //如果不是同一天，举例：20:00~8:00，
                // 当前日期需要在20：00之后并且24点之前；或者8:00之前并且0点之后。
                ((start.before(now) && now.before(endOfDay))
                        || (now.before(end) && startOfDay.before(start)))
            }
        }
        return false
    }
}