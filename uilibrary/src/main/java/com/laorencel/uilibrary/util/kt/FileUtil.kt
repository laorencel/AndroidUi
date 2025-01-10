package com.laorencel.uilibrary.util.kt

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

fun getFilesDir(context: Context?): String? {
    if (null != context) {
        return context.filesDir.absolutePath
    } else {
        return ActivityManager.getCurrentActivity()?.let {
            return it.filesDir.absolutePath
        }
    }
}

fun getCacheDir(context: Context?): String? {
    if (null != context) {
        return context.cacheDir.absolutePath
    } else {
        return ActivityManager.getCurrentActivity()?.let {
            return it.cacheDir.absolutePath
        }
    }
}

fun getFileInFilesDir(context: Context?, fileName: String?): File? {
    if (isEmpty(fileName)) {
        return null
    }
    val rootPath = getFilesDir(context)
    return rootPath?.let {
        val filePath = it + File.separator + fileName
        getFile(filePath)
    }
}

fun getFileInCacheDir(context: Context?, fileName: String?): File? {
    if (isEmpty(fileName)) {
        return null
    }
    val rootPath = getCacheDir(context)
    return rootPath?.let {
        val filePath = it + File.separator + fileName
        return getFile(filePath)
    }
}

fun getFile(filePath: String?): File? {
    if (isEmpty(filePath)) {
        return null
    }
    try {
        val file = File(filePath!!)
        if (!file.exists()) {
            val mkdirs = file.parentFile?.mkdirs()
            if (mkdirs == true) {
                val createNewFile = file.createNewFile()
                if (createNewFile) {
                    return file
                }
            }
            return null
        } else {
            return file
        }
    } catch (e: Exception) {
        Log.e("FileUtil", "getFile e:$e")
        return null
    }
}

suspend fun writeToFileInFilesDir(
    context: Context?,
    fileName: String?,
    content: String?,
    append: Boolean = false
): String? =
    withContext(Dispatchers.IO) {
        val file = getFileInFilesDir(context, fileName)
        file?.let {
            writeToFile(it.absolutePath, content, append)
        }
    }

suspend fun writeToFileInCacheDir(
    context: Context?,
    fileName: String?,
    content: String?,
    append: Boolean = false
): String? =
    withContext(Dispatchers.IO) {
        val file = getFileInCacheDir(context, fileName)
        file?.let {
            writeToFile(it.absolutePath, content, append)
        }
    }

/**
 * 将文本写入文件，如果是外部路径，需要申请权限
 *
 * @param filePath 文件路径，如：../somePath/test.txt
 * @param content  文本数据
 * @param append  是否在原文件上添加
 * @return 文件路径
 */
suspend fun writeToFile(filePath: String?, content: String?, append: Boolean = false): String? =
    withContext(Dispatchers.IO) {
        if (isEmpty(filePath)) {
            return@withContext null
        }
        if (isEmpty(content)) {
            return@withContext filePath
        }
        try {
            val file = getFile(filePath)
            if (null != file) {
                val osw =
                    OutputStreamWriter(FileOutputStream(filePath, append), StandardCharsets.UTF_8)
                osw.write(content)
                osw.close()
//                Log.e("FileUtil", "writeToFile complete")
                return@withContext filePath
            } else {
                return@withContext null
            }
        } catch (e: Exception) {
            Log.e("FileUtil", "writeToFile e:$e")
        }
        return@withContext null
    }


suspend fun readFromFileInFilesDir(
    context: Context?,
    fileName: String?,
): String =
    withContext(Dispatchers.IO) {
        val file = getFileInFilesDir(context, fileName)
        file?.let {
            readFromFile(it.absolutePath)
        } ?: ""
    }

suspend fun readFromFileInCacheDir(
    context: Context?,
    fileName: String?,
): String =
    withContext(Dispatchers.IO) {
        val file = getFileInCacheDir(context, fileName)
        file?.let {
            readFromFile(it.absolutePath)
        } ?: ""
    }

/**
 * 从文件中读取文本数据，如果是外部路径，需要申请权限
 *
 * @param filePath 文件路径，如：../somePath/test.txt
 * @return 文本数据
 */
suspend fun readFromFile(filePath: String): String = withContext(Dispatchers.IO) {
    val content = StringBuilder()

    try {
        val file = File(filePath)
        if (!file.exists()) {
            return@withContext content.toString()
        }
        val inStream: InputStream = FileInputStream(file)
        val inputReader = InputStreamReader(inStream, StandardCharsets.UTF_8)
        val buffReader = BufferedReader(inputReader)
        var line: String? = ""
        //分行读取
        while ((buffReader.readLine().also { line = it }) != null) {
            content.append(line)
        }
        inStream.close() //关闭输入流
        return@withContext content.toString()
    } catch (e: java.lang.Exception) {
        Log.e("FileUtil", "readFromFile e:$e")
    }
    return@withContext content.toString()
}