package com.laorencel.uilibrary.util.kt.log

import android.util.Log
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.flattener.ClassicFlattener
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy
import com.laorencel.uilibrary.util.kt.DateUtil
import com.laorencel.uilibrary.util.kt.isEmpty

data class YLogConfig(
    val tag: String = "YLog",
    val writeToFile: Boolean = false,
    val filePath: String,
    val fileNamePrefix: String = "YLog_",
    val fileMaxSize: Long = 10 * 1024 * 1024L,
    val fileMaxBackupCount: Int = 10,
    val fileCleanMillis: Long = 10L * 24L * 60L * 60L * 1000L,
) {

}

object YLog {

    var TAG = "YLog"
    var enable: Boolean = true
    var isInit: Boolean = false
        private set

    //是否暂停
    private var isPause: Boolean = false

    //缓存暂停时的msg
    private var pauseMsgList: MutableList<String> = mutableListOf()

    fun initLog(config: YLogConfig) {
        initLog(true, config)
    }

    fun initLog(enableLog: Boolean, config: YLogConfig) {
        enable = enableLog
        TAG = config.tag.ifEmpty { "YLog" }

        val logConfig = LogConfiguration.Builder()
            .tag(config.tag)
            .logLevel(LogLevel.ALL)
            .build()

        var filePrinter: FilePrinter? = null
        if (config.writeToFile) {
            filePrinter =
//                FilePrinter.Builder(getExternalFilesDir(context)) // Specify the directory path of log file(s) 指定日志文件的目录路径
                FilePrinter.Builder(config.filePath) // Specify the directory path of log file(s) 指定日志文件的目录路径
                    //                .fileNameGenerator(new DateFileNameGenerator()) //自定义文件名称 默认值:ChangelessFileNameGenerator(“日志”)
                    .fileNameGenerator(LogFileNameGenerator(config.fileNamePrefix)) //自定义文件名称 默认值:ChangelessFileNameGenerator(“日志”)
                    //                    .backupStrategy(new FileSizeBackupStrategy2(10 * 1024 * 1024, 100)) //单个日志文件的大小默认:FileSizeBackupStrategy(1024 * 1024)
                    .backupStrategy(
                        FileSizeBackupStrategy(
                            (20 * 1024 * 1024).toLong(),
                            100
                        )
                    ) //单个日志文件的大小默认:FileSizeBackupStrategy(1024 * 1024)
                    .cleanStrategy(FileLastModifiedCleanStrategy(10L * 24L * 60L * 60L * 1000L)) //日志文件存活时间，单位毫秒
                    //                .flattener(new DefaultFlattener()) //自定义flattener，控制打印格式
                    .flattener(ClassicFlattener()) //自定义flattener，控制打印格式
                    .build()
        }

        val androidPrinter = AndroidPrinter(true)
        if (null != filePrinter) {
            XLog.init(logConfig, androidPrinter, filePrinter)
        } else {
            XLog.init(logConfig, androidPrinter)
        }
        isInit = true
        XLog.enableStackTrace(2)
    }


    fun d(msg: String?) {
        if (enable && !msg.isNullOrEmpty()) {
            if (!isPause) {
                if (isInit) {
                    XLog.d(msg)
                } else {
                    Log.d(TAG, msg)
                }
                //            Log.d(TAG, msg);
            } else {
                pauseMsgList.add(
                    DateUtil.millisToString(
                        System.currentTimeMillis(),
                        DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS
                    ) + ":d:" + msg
                )
            }
        }
    }

    fun e(msg: String?) {
        if (enable && !msg.isNullOrEmpty()) {
            if (!isPause) {
                if (isInit) {
                    XLog.e(msg)
                } else {
                    Log.d(TAG, msg)
                }
            } else {
                pauseMsgList.add(
                    DateUtil.millisToString(
                        System.currentTimeMillis(),
                        DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS
                    ) + ":e:" + msg
                )
            }
        }
    }

    fun pause() {
        isPause = true
    }

    fun resume() {
        isPause = false
        if (!isEmpty(pauseMsgList)) {
            pauseMsgList.forEach { msg: String ->
                d("pauseMsg:$msg")
            }
        }
        pauseMsgList.clear()
    }
}

fun logD(msg: String?) {
    YLog.d(msg)
}

fun logE(msg: String?) {
    YLog.e(msg)
}