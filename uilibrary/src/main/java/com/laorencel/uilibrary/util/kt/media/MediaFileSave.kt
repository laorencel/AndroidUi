package com.laorencel.uilibrary.util.kt.media

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files

/**
 * 保存文件，兼容Android Q以上文件分区管理，
 *
 * @param context  context
 * @param filePath 源文件路径
 * @return 保存后文件路径
 */
fun mediaFileSave(context: Context, filePath: String): String? {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        saveFileBeforeQ(context, filePath)
    } else {
        saveFileAfterQ(context, filePath)
    }
}

private fun saveFileBeforeQ(context: Context, filePath: String): String? {
    var picDir: File? = null
    picDir = if (MediaFile.isImageFileType(filePath)) {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
    } else if (MediaFile.isVideoFileType(filePath)) {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
    } else if (MediaFile.isAudioFileType(filePath)) {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
    } else {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    }
    val tempFile = File(filePath)
    val destFile = File(picDir, context.packageName + File.separator + tempFile.name)
    var ins: FileInputStream? = null
    var ous: BufferedOutputStream? = null
    try {
        ins = FileInputStream(tempFile)
        ous = BufferedOutputStream(FileOutputStream(destFile))
        var nread = 0L
        val buf = ByteArray(1024)
        var n: Int
        while ((ins.read(buf).also { n = it }) > 0) {
            ous.write(buf, 0, n)
            nread += n.toLong()
        }
        MediaScannerConnection.scanFile(
            context,
            arrayOf<String>(destFile.absolutePath),  //                    new String[]{"video/*"},
            arrayOf<String?>(MediaFile.getMimeTypeForFile(destFile.absolutePath))
        ) { path: String, uri: Uri ->
            println(
                "saveFileBeforeQ: $path $uri"
            )
        }
        return destFile.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    } finally {
        try {
            ins?.close()
            ous?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

@RequiresApi(api = Build.VERSION_CODES.Q)
private fun saveFileAfterQ(context: Context, filePath: String): String? {
    try {
        val contentResolver = context.contentResolver
        val tempFile = File(filePath)
        val destFileDir: String

        var contentUri: Uri? = null
        var environmentDir: String? = null
        val isMediaMounted = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        if (MediaFile.isImageFileType(tempFile.absolutePath)) {
            environmentDir = Environment.DIRECTORY_DCIM
            println("isImageFileType")
            contentUri = if (isMediaMounted) {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            }
        } else if (MediaFile.isVideoFileType(tempFile.absolutePath)) {
            environmentDir = Environment.DIRECTORY_DCIM
            println("isVideoFileType")
            contentUri = if (isMediaMounted) {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Video.Media.INTERNAL_CONTENT_URI
            }
        } else if (MediaFile.isAudioFileType(tempFile.absolutePath)) {
            environmentDir = Environment.DIRECTORY_MUSIC
            println("isAudioFileType")
            contentUri = if (isMediaMounted) {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI
            }
        } else {
            environmentDir = Environment.DIRECTORY_DOWNLOADS
            println("isFilesFileType")
            contentUri = if (isMediaMounted) {
                MediaStore.Downloads.EXTERNAL_CONTENT_URI
                //                    contentUri = MediaStore.Files.getContentUri("external");
            } else {
                MediaStore.Downloads.INTERNAL_CONTENT_URI
                //                    contentUri = MediaStore.Files.getContentUri("internal");
            }
        }
        destFileDir = environmentDir + File.separator + context.packageName
        val contentValues =
            getFileContentValues(tempFile, destFileDir, System.currentTimeMillis())
        val uri = contentResolver.insert(contentUri!!, contentValues)
        copyFileAfterQ(context, contentResolver, tempFile, uri)
        contentValues.clear()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
        }
        context.contentResolver.update(uri!!, contentValues, null, null)
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))

        val destFile = File(
            Environment.getExternalStoragePublicDirectory(environmentDir),
            context.packageName + File.separator + tempFile.name
        )
        return destFile.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

@Throws(IOException::class)
private fun copyFileAfterQ(
    context: Context,
    localContentResolver: ContentResolver,
    tempFile: File,
    localUri: Uri?
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
        context.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.Q
    ) {
        //拷贝文件到相册的uri,android10及以上得这么干，否则不会显示。可以参考ScreenMediaRecorder的save方法
        val os = localContentResolver.openOutputStream(localUri!!)
        Files.copy(tempFile.toPath(), os)
        os!!.close()
        //            tempFile.delete();
    }
}

/**
 * 获取文件的contentValue
 */
private fun getFileContentValues(
    paramFile: File,
    destFileDir: String,
    timestamp: Long
): ContentValues {
    println(
        "getFileContentValues  getAbsolutePath:" + paramFile.absolutePath + " destFileDir:" + destFileDir
    )
    val contentValues = ContentValues()

    if (MediaFile.isImageFileType(paramFile.absolutePath)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, destFileDir)
        }
        contentValues.put(MediaStore.Images.Media.TITLE, paramFile.name)
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, paramFile.name)
        contentValues.put(
            MediaStore.Images.Media.MIME_TYPE,
            MediaFile.getMimeTypeForFile(paramFile.absolutePath)
        )
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
        contentValues.put(MediaStore.Images.Media.DATE_MODIFIED, timestamp)
        contentValues.put(MediaStore.Images.Media.DATE_ADDED, timestamp)
    } else if (MediaFile.isVideoFileType(paramFile.absolutePath)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, destFileDir)
        }
        contentValues.put(MediaStore.Video.Media.TITLE, paramFile.name)
        contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, paramFile.name)
        contentValues.put(
            MediaStore.Video.Media.MIME_TYPE,
            MediaFile.getMimeTypeForFile(paramFile.absolutePath)
        )
        contentValues.put(MediaStore.Video.Media.DATE_TAKEN, timestamp)
        contentValues.put(MediaStore.Video.Media.DATE_MODIFIED, timestamp)
        contentValues.put(MediaStore.Video.Media.DATE_ADDED, timestamp)
        contentValues.put(MediaStore.Video.Media.SIZE, paramFile.length())
    } else if (MediaFile.isAudioFileType(paramFile.absolutePath)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Audio.Media.RELATIVE_PATH, destFileDir)
            contentValues.put(MediaStore.Audio.Media.DATE_TAKEN, timestamp)
        }
        contentValues.put(MediaStore.Audio.Media.TITLE, paramFile.name)
        contentValues.put(MediaStore.Audio.Media.DISPLAY_NAME, paramFile.name)
        contentValues.put(
            MediaStore.Audio.Media.MIME_TYPE,
            MediaFile.getMimeTypeForFile(paramFile.absolutePath)
        )
        contentValues.put(MediaStore.Audio.Media.DATE_MODIFIED, timestamp)
        contentValues.put(MediaStore.Audio.Media.DATE_ADDED, timestamp)
        contentValues.put(MediaStore.Audio.Media.SIZE, paramFile.length())
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Downloads.RELATIVE_PATH, destFileDir)
            contentValues.put(MediaStore.Downloads.DATE_TAKEN, timestamp)
        }
        contentValues.put(MediaStore.Downloads.TITLE, paramFile.name)
        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, paramFile.name)
        contentValues.put(
            MediaStore.Downloads.MIME_TYPE,
            MediaFile.getMimeTypeForFile(paramFile.absolutePath)
        )
        contentValues.put(MediaStore.Downloads.DATE_MODIFIED, timestamp)
        contentValues.put(MediaStore.Downloads.DATE_ADDED, timestamp)
        contentValues.put(MediaStore.Downloads.SIZE, paramFile.length())
    }

    return contentValues
}