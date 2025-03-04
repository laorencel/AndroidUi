package com.laorencel.uilibrary.util.kt

// 类型名称    字节空间        应用场景
// byte        1Byte       字节数据
// short       2Byte       短整数
// int         4Byte       普通整数
// long        8Byte       长整数
// float       4Byte       浮点数
// double      8Byte       双精度浮点数
// char        2Byte       一个字符
// boolean     1Byte       逻辑变量(true,flase)


//位运算 Kotlin的位运算符只能对Int和Long两种数据类型起作用。
//shl(bits) – 左移位 (Java’s <<)
//shr(bits) – 右移位 (Java’s >>)
//ushr(bits) – 无符号右移位 (Java’s >>>)
//and(bits) – 与  &
//or(bits) – 或   ||
//xor(bits) – 异或
//inv() – 反向

//参考：https://www.cnblogs.com/hopeofthevillage/p/12917113.html
object ByteTransform {


    /**
     * 将int转为的byte数组
     * @param n int
     * @param bigEndian true：高字节在前，低字节在后（大端）
     * @return byte[]
     */
    fun intToByteArray(n: Int, bigEndian: Boolean = true): ByteArray {
        val b = ByteArray(4)
        b[3] = (n and 0xff).toByte()
        b[2] = (n shr 8 and 0xff).toByte()
        b[1] = (n shr 16 and 0xff).toByte()
        b[0] = (n shr 24 and 0xff).toByte()
        if (!bigEndian) {
            b.reverse()
        }
        return b
    }


    /**
     * byte数组到int的转换
     * @param bytes
     * @param bigEndian true：高字节在前，低字节在后（大端）
     * @return
     */
    fun byteArrayToInt(array: ByteArray, bigEndian: Boolean = true): Int {
        val int1 = array[3].toInt() and 0xff
        val int2 = (array[2].toInt() and 0xff) shl 8
        val int3 = (array[1].toInt() and 0xff) shl 16
        val int4 = (array[0].toInt() and 0xff) shl 24
        if (bigEndian) {
            return int1 or int2 or int3 or int4
        } else {
            return int4 or int3 or int2 or int1
        }

    }

    /**
     * 将short转为高字节在前，低字节在后的byte数组（大端）
     * @param n short
     * @param bigEndian true：高字节在前，低字节在后（大端）
     * @return byte[]
     */
    fun shortToByteArray(n: Short, bigEndian: Boolean = true): ByteArray {
        val b = ByteArray(2)
        b[1] = (n.toInt() and 0xff).toByte()
        b[0] = (n.toInt() shr 8 and 0xff).toByte()
        if (!bigEndian) {
            b.reverse()
        }
        return b
    }


    /**
     * 读取大端byte数组为short
     * @param b
     * @param bigEndian true：高字节在前，低字节在后（大端）
     * @return
     */
    fun byteArrayToShort(b: ByteArray, bigEndian: Boolean = true): Short {
        return if (bigEndian) {
            ((b[0].toInt() shl 8) or (b[1].toInt() and 0xff)).toShort()
        } else {
            ((b[1].toInt() shl 8) or (b[0].toInt() and 0xff)).toShort()
        }
    }

    /**
     * long类型转byte[]
     * @param n
     * @param bigEndian true：高字节在前，低字节在后（大端）
     * @return
     */
    fun longToByteArray(n: Long, bigEndian: Boolean = true): ByteArray {
        val b = ByteArray(8)
        b[7] = (n and 0xffL).toByte()
        b[6] = (n shr 8 and 0xffL).toByte()
        b[5] = (n shr 16 and 0xffL).toByte()
        b[4] = (n shr 24 and 0xffL).toByte()
        b[3] = (n shr 32 and 0xffL).toByte()
        b[2] = (n shr 40 and 0xffL).toByte()
        b[1] = (n shr 48 and 0xffL).toByte()
        b[0] = (n shr 56 and 0xffL).toByte()
        if (!bigEndian) {
            b.reverse()
        }
        return b
    }

    /**
     * byte[]转long类型
     * @param array
     * @param bigEndian true：高字节在前，低字节在后（大端）
     * @return
     */
    fun byteArrayToLong(array: ByteArray, bigEndian: Boolean = true): Long {
        return if (bigEndian) {
            (((array[0].toLong() and 0xffL) shl 56)
                    or ((array[1].toLong() and 0xffL) shl 48)
                    or ((array[2].toLong() and 0xffL) shl 40)
                    or ((array[3].toLong() and 0xffL) shl 32)
                    or ((array[4].toLong() and 0xffL) shl 24)
                    or ((array[5].toLong() and 0xffL) shl 16)
                    or ((array[6].toLong() and 0xffL) shl 8)
                    or ((array[7].toLong() and 0xffL) shl 0))

        } else {
            (((array[0].toLong() and 0xffL) shl 0)
                    or ((array[1].toLong() and 0xffL) shl 8)
                    or ((array[2].toLong() and 0xffL) shl 16)
                    or ((array[3].toLong() and 0xffL) shl 24)
                    or ((array[4].toLong() and 0xffL) shl 32)
                    or ((array[5].toLong() and 0xffL) shl 40)
                    or ((array[6].toLong() and 0xffL) shl 48)
                    or ((array[7].toLong() and 0xffL) shl 56))
        }
    }


    ///////////////////////////////////////////////////////////////////////////


    /**
     * 简洁写法 16进制字符串转成byte数组
     * @param hex 16进制字符串，支持大小写
     * @return byte数组
     */
    fun hexToByteArray(hex: String): ByteArray {
        val bytes = ByteArray(hex.length / 2)
        for (i in bytes.indices) {
            bytes[i] = Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16).toByte();
        }
        return bytes
    }


    /**
     * 简洁写法 byte数组转成16进制字符串
     * @param bytes byte数组
     * @return 16进制字符串
     */
    fun byteArrayToHex(bytes: ByteArray): String {
        val builder = StringBuilder()
        for (b in bytes) {
            builder.append(String.format("%02X", b));
        }
        return builder.toString();
    }

    /**
     * byte数组转成short数组
     * byte占用1个字节
     * short占用2个字节
     */
    fun byteArrayToShortArray(data: ByteArray, bigEndian: Boolean = true): ShortArray {
        val array = ShortArray(data.size / 2)
//        for (i in sdata.indices) sdata[i] = ModbusUtils.toShort(data[i * 2], data[i * 2 + 1])
        val arrayToShort = ByteArray(2)
        for (i in array.indices) {
            arrayToShort[0] = data[i * 2]
            arrayToShort[1] = data[i * 2 + 1]
            array[i] = byteArrayToShort(arrayToShort,bigEndian)
        }
        return array
    }

    fun shortArrayToByteArray(data: ShortArray, bigEndian: Boolean = true): ByteArray {
        val byteArray = ByteArray(data.size * 2)
        for (i in data.indices) {
            val array = shortToByteArray(data[i],bigEndian)
            byteArray[i * 2] = array[0]
            byteArray[i * 2 + 1] = array[1]
        }
        return byteArray
    }

    fun byteToBoolean(data: Byte): Boolean {
        return data.toInt() == 0x01
    }

    fun byteArrayToBooleanArray(data: ByteArray): BooleanArray {
        return data.map { byteToBoolean(it) }.toBooleanArray()
    }

    fun booleanToByte(data: Boolean): Byte {
        return if (data) 0x01 else 0x00
    }

    fun booleanArrayToByteArray(data: BooleanArray): ByteArray {
        return data.map { booleanToByte(it) }.toByteArray()
    }
}