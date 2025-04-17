package com.laorencel.uilibrary.util.kt

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.laorencel.uilibrary.R
import com.laorencel.uilibrary.util.kt.log.logE
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object TipUtil {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logE("TipUtil e: ${Log.getStackTraceString(throwable)}")
    }

    private val mainScope = CoroutineScope(Dispatchers.Main + coroutineExceptionHandler)

    /**
     * 显示 Snackbar
     *
     */
    fun showSnackbar(content: String?, view: View? = null, duration: Int = Snackbar.LENGTH_LONG) {
        if (!content.isNullOrEmpty()) {
            mainScope.launch {
                if (null == view) {
                    val currentActivity: Activity? = ActivityManager.getCurrentActivity()
                    if (null != currentActivity && !currentActivity.isFinishing) {
                        Snackbar.make(
                            currentActivity.window.decorView,
                            content,
                            duration
                        ).show()
                    }
                } else {
                    Snackbar.make(view, content, duration).show()
                }
            }
        }
    }

    /**
     * 显示 Toast
     *
     */
    fun showToast(content: String?, context: Context? = null, duration: Int = Toast.LENGTH_LONG) {
        if (!content.isNullOrEmpty()) {
            mainScope.launch {
                if (null == context) {
                    val currentActivity: Activity? = ActivityManager.getCurrentActivity()
                    if (null != currentActivity && !currentActivity.isFinishing) {
                        Toast.makeText(currentActivity, content, Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, content, duration).show()
                }
            }
        }
    }

    /**
     * 显示 确认Dialog
     *
     */
    fun showConfirmDialog(
        title: String? = null,
        content: String? = null,
        confirmText: String? = null,
        cancelText: String? = null,
        cancelable: Boolean = false,
        onConfirmListener: DialogInterface.OnClickListener? = null,
        onCancelListener: DialogInterface.OnClickListener? = null,
        autoCloseSeconds: Long? = 0
    ) {
        mainScope.launch {
            val currentActivity: Activity? = ActivityManager.getCurrentActivity()
            if (null != currentActivity && !currentActivity.isFinishing) {
                var dialog: AlertDialog? = null;
                val builder = MaterialAlertDialogBuilder(currentActivity)
                    .setTitle(title)
                    .setMessage(content)
                    .setCancelable(cancelable)

                if (null != onConfirmListener) {
                    builder.setPositiveButton(
                        if (isEmpty(confirmText)) currentActivity.resources.getString(
                            R.string.confirm
                        ) else confirmText, onConfirmListener
                    )
                }
                if (null != onCancelListener) {
                    builder.setNegativeButton(
                        if (isEmpty(cancelText)) currentActivity.resources.getString(
                            R.string.cancel
                        ) else cancelText, onCancelListener
                    )
                }
                if (null == onConfirmListener && null == onCancelListener) {
                    builder.setCancelable(true)
                }

                dialog = builder.show()
                if (null != autoCloseSeconds && autoCloseSeconds > 0) {
                    launch {
                        delay(autoCloseSeconds * 1000L)
                        if (null != dialog && dialog.isShowing) {
                            dialog.dismiss()
                        }
                    }
                }
            }
        }
    }

    /**
     * 显示 确认Dialog 并返回结果:true/false
     *
     */
    suspend fun showConfirmDialogWithResult(
        title: String? = null,
        content: String? = null,
        confirmText: String? = null,
        cancelText: String? = null,
    ): Boolean = suspendCoroutine { coroutine ->
        mainScope.launch {
            val currentActivity: Activity? = ActivityManager.getCurrentActivity()
            if (null != currentActivity && !currentActivity.isFinishing) {
                var dialog: AlertDialog? = null;
                val builder = MaterialAlertDialogBuilder(currentActivity)
                    .setTitle(title)
                    .setMessage(content)
                    .setCancelable(false)
                    .setPositiveButton(
                        if (isEmpty(confirmText)) currentActivity.resources.getString(
                            R.string.confirm
                        ) else confirmText
                    ) { dialog, which -> coroutine.resume(true) }
                    .setNegativeButton(
                        if (isEmpty(cancelText)) currentActivity.resources.getString(
                            R.string.cancel
                        ) else cancelText
                    ) { dialog, which -> coroutine.resume(false) }

                dialog = builder.show()
            }
        }

    }
}