package com.laorencel.uilibrary.util.kt.log

import com.elvishew.xlog.printer.file.naming.FileNameGenerator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class LogFileNameGenerator(val namePrefix:String) : FileNameGenerator {
    companion object {
        private val mLocalDateFormat: ThreadLocal<SimpleDateFormat> =
            object : ThreadLocal<SimpleDateFormat>() {
                override fun initialValue(): SimpleDateFormat {
                    return SimpleDateFormat("yyyy-MM-dd", Locale.US)
                }
            }
    }

    override fun isFileNameChangeable(): Boolean {
        return true
    }

    override fun generateFileName(logLevel: Int, timestamp: Long): String {
        val sdf = mLocalDateFormat.get()
        sdf?.timeZone = TimeZone.getDefault()
        val builder = StringBuilder()
        if (namePrefix.isNotEmpty()){
            builder.append(namePrefix)
        }
        builder.append(sdf?.format(Date(timestamp))).append(".txt")
        return builder.toString()
//        return generateFileName(timestamp)
    }

}