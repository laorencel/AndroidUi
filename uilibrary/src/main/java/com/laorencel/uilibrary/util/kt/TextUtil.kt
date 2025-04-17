package com.laorencel.uilibrary.util.kt

import java.text.DecimalFormat
import kotlin.math.ceil
import kotlin.math.floor

object TextUtil {
    //参考
    //    private void handleNumberFormat() {
    //        //DecimalFormat 类主要靠 # 和 0 两种占位符号来指定数字长度。0 表示如果位数不足则以 0 填充，# 表示只要有可能就把数字拉上这个位置。
    //        //1、小数部分 #代表最多有几位，0代表必须有且只能有几位
    //        //.00 表示最终结果得有两位小数，没有，我给你加上；多了，就四舍五入第三个小数
    //        //.## 标示最终结果最多有两位小数 一位或者没有都可以 多了同样四舍五入第三位
    //        //2、整数部分 0 和 #
    //        //当整数部分为0时 比如 0.1 #此时认为整数部分不存在，所以不写0 认为没有至少也得一位，写上0
    //        //这跟上面第一部分的表现是一致的：# 有就写，没有就不写 ；0 必须有 没有补0
    //        //3、整数部分有多位时： 2 20 200
    //        //由上面的结果可以看出 0和#对整数部分多位时的处理是一致的 就是有几位写多少位
    //        //这跟上面两部分的表现是不一致的 在有多位时，0和#都没有匹配位数，而是有多少写多少
    //        //————————————————
    //        double pi = 3.1415927;//圆周率
    //        //取一位整数
    //        logE("handleNumberFormat", (new DecimalFormat("0").format(pi)));//3
    //        //取一位整数和两位小数
    //        logE("handleNumberFormat", (new DecimalFormat("0.00").format(pi)));//3.14
    //        //取两位整数和三位小数，整数不足部分以0填补。
    //        logE("handleNumberFormat", (new DecimalFormat("00.000").format(pi)));// 03.142
    //        //取所有整数部分
    //        logE("handleNumberFormat", (new DecimalFormat("#").format(pi)));//3
    //        //以百分比方式计数，并取两位小数
    //        logE("handleNumberFormat", (new DecimalFormat("#.##%").format(pi)));//314.16%
    //        long c = 299792458;//光速
    //        //显示为科学计数法，并取五位小数
    //        logE("handleNumberFormat", (new DecimalFormat("#.#####E0").format(c)));//2.99792E8
    //        //显示为两位整数的科学计数法，并取四位小数
    //        logE("handleNumberFormat", (new DecimalFormat("00.####E0").format(c)));//29.9792E7
    //        //每三位以逗号进行分隔。
    //        logE("handleNumberFormat", (new DecimalFormat(",###").format(c)));//299,792,458
    //        //将格式嵌入文本
    //        logE("handleNumberFormat", (new DecimalFormat("光速大小为每秒,###米。").format(c)));
    //    }
    fun stringValue(obj: Any?): String {
        if (obj == null) {
            return ""
        }
        var value = ""
        value = if (obj is String) {
            obj as String
        } else {
            obj.toString()
        }
        return value
    }

    fun stringValue(obj: Any?, defaultValue: String?): String? {
        return stringValue(obj, defaultValue, 0)
    }

    /**
     * 返回String
     *
     * @param obj          Object
     * @param defaultValue 默认值
     * @param digits       如果是数字类型，截取小数点后几位
     * @return String
     */
    fun stringValue(obj: Any?, defaultValue: String?, digits: Int): String? {
        var value: String? = stringValue(obj)
        if (null != obj && obj is Number) {
            val digitsPattern = StringBuilder("#")
            if (digits > 0) {
                digitsPattern.append(".")
                for (i in 0 until digits) {
                    digitsPattern.append("0")
                }
            }
            val format = DecimalFormat(digitsPattern.toString())
            value = format.format(obj)
        }
        if (null == value || value.length == 0) {
            value = defaultValue
        }
        return value
    }

    fun maxLengthString(text: String?, maxLength: Int): String {
        return maxLengthString(text, maxLength, "start", "...")
    }

    /**
     * 截取文本
     *
     * @param text             原始文本
     * @param maxLength        最大字数，如果是单数，且省略中间部分，则前少后多，如：maxLengthString(1811234567,7,'****','center')=>181...4567
     * @param ellipsis         省略号，默认:'...'
     * @param ellipsisPosition 省略号位置：start 前面,center 中间,end 结尾
     * @returns String
     */
    fun maxLengthString(
        text: String?,
        maxLength: Int,
        ellipsisPosition: String,
        ellipsis: String?
    ): String {
        var ellipsisPosition = ellipsisPosition
        var ellipsis = ellipsis
        if (null == text || text.length == 0) {
            return ""
        }
        if (maxLength > text.length) {
            return text
        } else {
            val builder = StringBuilder()
            if (isEmpty(ellipsisPosition)) {
                ellipsisPosition = "start"
            }
            if (isEmpty(ellipsis)) {
                ellipsis = "..."
            }
            if ("start" == ellipsisPosition) {
                builder.append(ellipsis).append(text.substring(text.length - maxLength))
            } else if ("center" == ellipsisPosition) {
                //Math.floor 对x进行下舍入。1.6=>1.0
                val start = text.substring(
                    0,
                    floor((maxLength / 2).toDouble()).toInt()
                )
                //Math.ceil 对x进行上舍入。1.4=>2.0
                val end = text.substring(
                    text.length - ceil((maxLength / 2).toDouble())
                        .toInt(), text.length
                )
                builder.append(start).append(ellipsis).append(end)
            } else if ("end" == ellipsisPosition) {
                builder.append(text.substring(0, maxLength)).append(ellipsis)
            }
            return builder.toString()
        }
    }

    fun reverse(text: String?): String {
        //反转文本，如"123"->"321"
        if (isEmpty(text)) {
            return ""
        }
        return text!!.reversed()
    }
}