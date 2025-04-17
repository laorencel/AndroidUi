package com.laorencel.uilibrary.util.kt

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import kotlin.concurrent.Volatile


object ScreenUtil {

    /**
     * 屏幕宽度
     * @return the width of screen, in pixel
     */
    fun getScreenWidth(): Int {
        val wm = ActivityManager.getCurrentActivity()!!
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        return point.x
    }

    /***
     * 获取屏幕的高度，全面屏和非全面屏
     * @return
     */
    fun getScreenHeight(): Int {
        if (!isAllScreenDevice()) {
            return getHeight()
        }
        return getRealHeight()
    }

    const val PORTRAIT: Int = 0
    const val LANDSCAPE: Int = 1

    @Volatile
    var mHasCheckAllScreen: Boolean = false

    @Volatile
    var mIsAllScreenDevice: Boolean = false

    @Volatile
    var mRealSizes: Array<Point?> = arrayOfNulls(2)

    private fun getRealHeight(context: Context? = null): Int {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return getHeight()
        }

        var orientation = context?.resources?.configuration?.orientation
            ?: ActivityManager.getCurrentActivity()!!.resources.configuration.orientation
        orientation = if (orientation == Configuration.ORIENTATION_PORTRAIT) PORTRAIT else LANDSCAPE

        if (mRealSizes[orientation] == null) {
            val windowManager = if (context != null
            ) context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            else ActivityManager.getCurrentActivity()!!
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager?
            if (windowManager == null) {
                return getHeight()
            }

            val display = windowManager.defaultDisplay
            val point = Point()
            display.getRealSize(point)
            mRealSizes[orientation] = point
        }
        return mRealSizes[orientation]!!.y
    }

    private fun getHeight(): Int {
        return ActivityManager.getCurrentActivity()?.resources?.displayMetrics?.heightPixels
            ?: 0
    }

    /***
     * 获取当前手机是否是全面屏
     * @return
     */
    fun isAllScreenDevice(): Boolean {
        if (mHasCheckAllScreen) {
            return mIsAllScreenDevice
        }
        mHasCheckAllScreen = true
        mIsAllScreenDevice = false
        // API小于21时，没有全面屏
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false
        }
        val windowManager = ActivityManager.getCurrentActivity()?.getSystemService(
            Context.WINDOW_SERVICE
        ) as WindowManager
        if (windowManager != null) {
            val display = windowManager.defaultDisplay
            val point = Point()
            display.getRealSize(point)
            val width: Float
            val height: Float
            if (point.x < point.y) {
                width = point.x.toFloat()
                height = point.y.toFloat()
            } else {
                width = point.y.toFloat()
                height = point.x.toFloat()
            }
            if (height / width >= 1.97f) {
                mIsAllScreenDevice = true
            }
        }
        return mIsAllScreenDevice
    }
}