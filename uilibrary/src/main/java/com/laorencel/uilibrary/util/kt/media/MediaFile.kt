package com.laorencel.uilibrary.util.kt.media

import android.media.MediaMetadataRetriever
import android.util.Log
import java.io.IOException


object MediaFile {
    // Audio file types
    const val FILE_TYPE_MP3: Int = 1
    const val FILE_TYPE_M4A: Int = 2
    const val FILE_TYPE_WAV: Int = 3
    const val FILE_TYPE_AMR: Int = 4
    const val FILE_TYPE_AWB: Int = 5
    const val FILE_TYPE_WMA: Int = 6
    const val FILE_TYPE_OGG: Int = 7
    const val FILE_TYPE_AAC: Int = 8
    const val FILE_TYPE_MKA: Int = 9
    const val FILE_TYPE_FLAC: Int = 10
    private const val FIRST_AUDIO_FILE_TYPE = FILE_TYPE_MP3
    private const val LAST_AUDIO_FILE_TYPE = FILE_TYPE_FLAC

    // MIDI file types
    const val FILE_TYPE_MID: Int = 11
    const val FILE_TYPE_SMF: Int = 12
    const val FILE_TYPE_IMY: Int = 13
    private const val FIRST_MIDI_FILE_TYPE = FILE_TYPE_MID
    private const val LAST_MIDI_FILE_TYPE = FILE_TYPE_IMY

    // Video file types
    const val FILE_TYPE_MP4: Int = 21
    const val FILE_TYPE_M4V: Int = 22
    const val FILE_TYPE_3GPP: Int = 23
    const val FILE_TYPE_3GPP2: Int = 24
    const val FILE_TYPE_WMV: Int = 25
    const val FILE_TYPE_ASF: Int = 26
    const val FILE_TYPE_MKV: Int = 27
    const val FILE_TYPE_MP2TS: Int = 28
    const val FILE_TYPE_AVI: Int = 29
    const val FILE_TYPE_WEBM: Int = 30
    private const val FIRST_VIDEO_FILE_TYPE = FILE_TYPE_MP4
    private const val LAST_VIDEO_FILE_TYPE = FILE_TYPE_WEBM

    // More video file types
    const val FILE_TYPE_MP2PS: Int = 200
    private const val FIRST_VIDEO_FILE_TYPE2 = FILE_TYPE_MP2PS
    private const val LAST_VIDEO_FILE_TYPE2 = FILE_TYPE_MP2PS

    // Image file types
    const val FILE_TYPE_JPEG: Int = 31
    const val FILE_TYPE_GIF: Int = 32
    const val FILE_TYPE_PNG: Int = 33
    const val FILE_TYPE_BMP: Int = 34
    const val FILE_TYPE_WBMP: Int = 35
    const val FILE_TYPE_WEBP: Int = 36
    private const val FIRST_IMAGE_FILE_TYPE = FILE_TYPE_JPEG
    private const val LAST_IMAGE_FILE_TYPE = FILE_TYPE_WEBP

    // Playlist file types
    const val FILE_TYPE_M3U: Int = 41
    const val FILE_TYPE_PLS: Int = 42
    const val FILE_TYPE_WPL: Int = 43
    const val FILE_TYPE_HTTPLIVE: Int = 44
    private const val FIRST_PLAYLIST_FILE_TYPE = FILE_TYPE_M3U
    private const val LAST_PLAYLIST_FILE_TYPE = FILE_TYPE_HTTPLIVE

    // Drm file types
    const val FILE_TYPE_FL: Int = 51
    private const val FIRST_DRM_FILE_TYPE = FILE_TYPE_FL
    private const val LAST_DRM_FILE_TYPE = FILE_TYPE_FL

    // Other popular file types
    const val FILE_TYPE_TEXT: Int = 100
    const val FILE_TYPE_HTML: Int = 101
    const val FILE_TYPE_PDF: Int = 102
    const val FILE_TYPE_XML: Int = 103
    const val FILE_TYPE_MS_WORD: Int = 104
    const val FILE_TYPE_MS_EXCEL: Int = 105
    const val FILE_TYPE_MS_POWERPOINT: Int = 106
    const val FILE_TYPE_ZIP: Int = 107

    private val sFileTypeMap = HashMap<String, MediaFileType>()
    private val sMimeTypeMap = HashMap<String, Int>()

    fun addFileType(extension: String, fileType: Int, mimeType: String) {
        sFileTypeMap[extension] = MediaFileType(fileType, mimeType)
        sMimeTypeMap[mimeType] = fileType
    }

    init {
        addFileType("MP3", FILE_TYPE_MP3, "audio/mpeg")
        addFileType("MPGA", FILE_TYPE_MP3, "audio/mpeg")
        addFileType("M4A", FILE_TYPE_M4A, "audio/mp4")
        addFileType("WAV", FILE_TYPE_WAV, "audio/x-wav")
        addFileType("AMR", FILE_TYPE_AMR, "audio/amr")
        addFileType("AWB", FILE_TYPE_AWB, "audio/amr-wb")
        addFileType("WMA", FILE_TYPE_WMA, "audio/x-ms-wma")
        addFileType("OGG", FILE_TYPE_OGG, "audio/ogg")
        addFileType("OGG", FILE_TYPE_OGG, "application/ogg")
        addFileType("OGA", FILE_TYPE_OGG, "application/ogg")
        addFileType("AAC", FILE_TYPE_AAC, "audio/aac")
        addFileType("AAC", FILE_TYPE_AAC, "audio/aac-adts")
        addFileType("MKA", FILE_TYPE_MKA, "audio/x-matroska")

        addFileType("MID", FILE_TYPE_MID, "audio/midi")
        addFileType("MIDI", FILE_TYPE_MID, "audio/midi")
        addFileType("XMF", FILE_TYPE_MID, "audio/midi")
        addFileType("RTTTL", FILE_TYPE_MID, "audio/midi")
        addFileType("SMF", FILE_TYPE_SMF, "audio/sp-midi")
        addFileType("IMY", FILE_TYPE_IMY, "audio/imelody")
        addFileType("RTX", FILE_TYPE_MID, "audio/midi")
        addFileType("OTA", FILE_TYPE_MID, "audio/midi")
        addFileType("MXMF", FILE_TYPE_MID, "audio/midi")

        addFileType("MPEG", FILE_TYPE_MP4, "video/mpeg")
        addFileType("MPG", FILE_TYPE_MP4, "video/mpeg")
        addFileType("MP4", FILE_TYPE_MP4, "video/mp4")
        addFileType("M4V", FILE_TYPE_M4V, "video/mp4")
        addFileType("3GP", FILE_TYPE_3GPP, "video/3gpp")
        addFileType("3GPP", FILE_TYPE_3GPP, "video/3gpp")
        addFileType("3G2", FILE_TYPE_3GPP2, "video/3gpp2")
        addFileType("3GPP2", FILE_TYPE_3GPP2, "video/3gpp2")
        addFileType("MKV", FILE_TYPE_MKV, "video/x-matroska")
        addFileType("WEBM", FILE_TYPE_WEBM, "video/webm")
        addFileType("TS", FILE_TYPE_MP2TS, "video/mp2ts")
        addFileType("AVI", FILE_TYPE_AVI, "video/avi")
        addFileType("WMV", FILE_TYPE_WMV, "video/x-ms-wmv")
        addFileType("ASF", FILE_TYPE_ASF, "video/x-ms-asf")

        addFileType("JPG", FILE_TYPE_JPEG, "image/jpeg")
        addFileType("JPEG", FILE_TYPE_JPEG, "image/jpeg")
        addFileType("GIF", FILE_TYPE_GIF, "image/gif")
        addFileType("PNG", FILE_TYPE_PNG, "image/png")
        addFileType("BMP", FILE_TYPE_BMP, "image/x-ms-bmp")
        addFileType("WBMP", FILE_TYPE_WBMP, "image/vnd.wap.wbmp")
        addFileType("WEBP", FILE_TYPE_WEBP, "image/webp")

        addFileType("M3U", FILE_TYPE_M3U, "audio/x-mpegurl")
        addFileType("M3U", FILE_TYPE_M3U, "application/x-mpegurl")
        addFileType("PLS", FILE_TYPE_PLS, "audio/x-scpls")
        addFileType("WPL", FILE_TYPE_WPL, "application/vnd.ms-wpl")
        addFileType("M3U8", FILE_TYPE_HTTPLIVE, "application/vnd.apple.mpegurl")
        addFileType("M3U8", FILE_TYPE_HTTPLIVE, "audio/mpegurl")
        addFileType("M3U8", FILE_TYPE_HTTPLIVE, "audio/x-mpegurl")
        addFileType("FL", FILE_TYPE_FL, "application/x-android-drm-fl")

        addFileType("TXT", FILE_TYPE_TEXT, "text/plain")
        addFileType("HTM", FILE_TYPE_HTML, "text/html")
        addFileType("HTML", FILE_TYPE_HTML, "text/html")
        addFileType("PDF", FILE_TYPE_PDF, "application/pdf")
        addFileType("DOC", FILE_TYPE_MS_WORD, "application/msword")
        addFileType("XLS", FILE_TYPE_MS_EXCEL, "application/vnd.ms-excel")
        addFileType("PPT", FILE_TYPE_MS_POWERPOINT, "application/mspowerpoint")
        addFileType("FLAC", FILE_TYPE_FLAC, "audio/flac")
        addFileType("ZIP", FILE_TYPE_ZIP, "application/zip")
        addFileType("MPG", FILE_TYPE_MP2PS, "video/mp2p")
        addFileType("MPEG", FILE_TYPE_MP2PS, "video/mp2p")
    }

    /**
     * check is audio or not
     *
     * @param fileType file type integer value
     * @return if is audio type , return true;otherwise , return false
     */
    fun isAudioFileType(fileType: Int): Boolean {
        return ((fileType >= FIRST_AUDIO_FILE_TYPE &&
                fileType <= LAST_AUDIO_FILE_TYPE) ||
                (fileType >= FIRST_MIDI_FILE_TYPE &&
                        fileType <= LAST_MIDI_FILE_TYPE))
    }

    fun isAudioFileType(filePath: String): Boolean {
        val type = getFileType(filePath)
        if (null != type) {
            return isAudioFileType(type.fileType)
        }
        return false
    }

    /**
     * check is video or not
     *
     * @param fileType file type integer value
     * @return if is video type , return true ; otherwise , return false
     */
    fun isVideoFileType(fileType: Int): Boolean {
        return ((fileType >= FIRST_VIDEO_FILE_TYPE &&
                fileType <= LAST_VIDEO_FILE_TYPE)
                || (fileType >= FIRST_VIDEO_FILE_TYPE2 &&
                fileType <= LAST_VIDEO_FILE_TYPE2))
    }

    fun isVideoFileType(filePath: String): Boolean {
        val type = getFileType(filePath)
        if (null != type) {
            return isVideoFileType(type.fileType)
        }
        return false
    }

    /**
     * check is image or not
     *
     * @param fileType file type integer value
     * @return if is image type , return true ; otherwise , return false ;
     */
    fun isImageFileType(fileType: Int): Boolean {
        return (fileType >= FIRST_IMAGE_FILE_TYPE &&
                fileType <= LAST_IMAGE_FILE_TYPE)
    }

    fun isImageFileType(filePath: String): Boolean {
        val type = getFileType(filePath)
        if (null != type) {
            return isImageFileType(type.fileType)
        }
        return false
    }

    /**
     * check is playlist or not
     *
     * @param fileType file type integer value
     * @return if is playlist type , return true ; otherwise , return false ;
     */
    fun isPlayListFileType(fileType: Int): Boolean {
        return (fileType >= FIRST_PLAYLIST_FILE_TYPE &&
                fileType <= LAST_PLAYLIST_FILE_TYPE)
    }

    fun isPlayListFileType(filePath: String): Boolean {
        val type = getFileType(filePath)
        if (null != type) {
            return isPlayListFileType(type.fileType)
        }
        return false
    }

    /**
     * check is drm or not
     *
     * @param fileType file type integer value
     * @return if is drm type , return true ; otherwise , return false ;
     */
    fun isDrmFileType(fileType: Int): Boolean {
        return (fileType >= FIRST_DRM_FILE_TYPE &&
                fileType <= LAST_DRM_FILE_TYPE)
    }

    fun isDrmFileType(filePath: String): Boolean {
        val type = getFileType(filePath)
        if (null != type) {
            return isDrmFileType(type.fileType)
        }
        return false
    }

    /**
     * get file's extension by file' path
     *
     * @param path file's path
     * @return MediaFileType if the given file extension exist , or null
     */
    fun getFileType(path: String): MediaFileType? {
        val lastDot = path.lastIndexOf('.')
        if (lastDot < 0) return null
        return sFileTypeMap[path.substring(lastDot + 1).uppercase()]
    }

    /**
     * check the given mime type is mime type media or not
     *
     * @param mimeType mime type to check
     * @return if the given mime type is mime type media,return true ;otherwise , false
     */
    fun isMimeTypeMedia(mimeType: String): Boolean {
        val fileType = getFileTypeForMimeType(mimeType)
        return (isAudioFileType(fileType) || isVideoFileType(fileType)
                || isImageFileType(fileType) || isPlayListFileType(fileType))
    }

    /**
     * generates a title based on file name
     *
     * @param path file's path
     * @return file'name without extension
     */
    fun getFileTitle(path: String): String {
        // extract file name after last slash
        var path = path
        var lastSlash = path.lastIndexOf('/')
        if (lastSlash >= 0) {
            lastSlash++
            if (lastSlash < path.length) {
                path = path.substring(lastSlash)
            }
        }
        // truncate the file extension (if any)
        val lastDot = path.lastIndexOf('.')
        if (lastDot > 0) {
            path = path.substring(0, lastDot)
        }
        return path
    }

    /**
     * get mine type integer value
     *
     * @param mimeType mime type to get
     * @return return mime type value if exist ;or zero value if not exist
     */
    fun getFileTypeForMimeType(mimeType: String): Int {
        val value = sMimeTypeMap[mimeType]
        return (value ?: 0)
    }

    /**
     * get file's mime type base on path
     *
     * @param path file path
     * @return return mime type if exist , or null
     */
    fun getMimeTypeForFile(path: String): String? {
        val mediaFileType = getFileType(path)
        return (mediaFileType?.mimeType)
    }

    /**
     * 获取 视频 或 音频 时长
     * @param path 视频 或 音频 文件路径
     * @return 时长 毫秒值
     */
    fun getDuration(path: String?): Long {
        var duration: Long = 0
        val mmr = MediaMetadataRetriever()
        try {
            if (path != null) {
                mmr.setDataSource(path, HashMap())
                Log.d("getDuration time", "bbbbb")
            }
            val time = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            Log.d("getDuration time", time!!)
            duration = time.toLong()
        } catch (ex: Exception) {
            Log.d("getDuration ex", ex.toString())
            ex.printStackTrace()
        } finally {
            try {
                mmr.release()
            } catch (e: IOException) {
                Log.d("getDuration e", e.toString())
                e.printStackTrace()
            }
        }
        return duration
    }

    class MediaFileType internal constructor(val fileType: Int, val mimeType: String) {
        override fun toString(): String {
            return "MediaFileType{" +
                    "fileType=" + fileType +
                    ", mimeType='" + mimeType + '\'' +
                    '}'
        }
    }
}