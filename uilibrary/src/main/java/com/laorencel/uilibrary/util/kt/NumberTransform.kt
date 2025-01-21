package com.laorencel.uilibrary.util.kt


/**
 * Number转换工具
 * 1、在Java中内置了几个方法来帮助我们进行各种进制的转换。如下图所示（以Integer整形为例，其他类型雷同）：
 * 二进制：Integer.toBinaryString(int i);
 * 八进制：Integer.toOctalString(int i);
 * 十六进制：Integer.toHexString(int i);
 * <p>
 * 2、其他进制转化为十进制：
 * 二进制：Integer.valueOf("0101",2).toString;
 * 八进制：Integer.valueOf("376",8).toString;
 * 十六进制：Integer.valueOf("FFFF",16).toString;
 * <p>
 * 3、使用Integer类中的parseInt()方法和valueOf()方法都可以将其他进制转化为10进制,
 * 不同的是parseInt()方法的返回值是int类型，而valueOf()返回值是Integer对象。
 * <p>
 * 4、java中可以直接声明二进制、八进制、十进制、十六进制
 * 例如：
 * 二级制： int bin = 0b1100010；//前缀 "0b"
 * 八进制： int oct = 0142；//前缀 "0"
 * 十进制： int dec = 98；
 * 十六进制： int hex = 0x62;//前缀 "0x"
 */
object NumberTransform {

    fun lengthString(string: String?, length: Int?, replace: String = ""): String {
        var value = string ?: ""
        if (null != length && length > value.length) {
            value = String.format("%${length}s", value).replace(" ", replace)
        }
        return value
    }

    fun toBinaryString(number: Number?, length: Int? = null): String {
        val value = number?.let { java.lang.Long.toBinaryString(number.toLong()) }
        return lengthString(value, length, "0")
    }

    fun toOctalString(number: Number?, length: Int? = null): String {
        val value = number?.let { java.lang.Long.toOctalString(number.toLong()) }
        return lengthString(value, length, "0")
    }

    fun toDecimalString(number: Number?, length: Int? = null): String {
        val value = number?.toString()
        return lengthString(value, length, "0")
    }

    fun toHexString(number: Number?, length: Int? = null): String {
        val value = number?.let { java.lang.Long.toHexString(number.toLong()) }
        return lengthString(value, length, "0")
    }

    fun binaryToDecimal(binary: String?): Long? {
        return stringToDecimal(binary, 2)
    }

    fun octalToDecimal(octal: String?): Long? {
        return stringToDecimal(octal, 8)
    }

    fun stringToDecimal(string: String?, radix: Int = 10): Long? {
        return try {
            string?.trim()?.let { java.lang.Long.valueOf(it, radix) }
        } catch (e: Exception) {
            null
        }
    }

    fun hexToDecimal(hex: String?): Long? {
        return stringToDecimal(hex, 16)
    }

}