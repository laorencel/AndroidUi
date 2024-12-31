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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object TipUtil {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("TipUtil e:", "$throwable")
    }

    /**
     * 显示 Snackbar
     *
     */
    fun showSnackbar(view: View?, content: String?, duration: Int = Snackbar.LENGTH_LONG) {
        if (!content.isNullOrEmpty()) {
            CoroutineScope(Dispatchers.Main + coroutineExceptionHandler).launch {
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
    fun showToast(context: Context?, content: String?, duration: Int = Toast.LENGTH_LONG) {
        if (!content.isNullOrEmpty()) {
            CoroutineScope(Dispatchers.Main + coroutineExceptionHandler).launch {
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
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val currentActivity: Activity? = ActivityManager.getCurrentActivity()
            if (null != currentActivity && !currentActivity.isFinishing) {
                var dialog: AlertDialog? = null;
                withContext(Dispatchers.Main) {
                    val builder = MaterialAlertDialogBuilder(currentActivity)
                        .setTitle(title)
                        .setMessage(content)
                        .setCancelable(cancelable)
                        .setNegativeButton(
                            if (isEmpty(confirmText)) currentActivity.resources.getString(
                                R.string.confirm
                            ) else confirmText, onConfirmListener
                        )

                    if (null != onCancelListener) {
                        builder.setPositiveButton(
                            if (isEmpty(cancelText)) currentActivity.resources.getString(
                                R.string.cancel
                            ) else cancelText, onCancelListener
                        )
                    }
                    dialog = builder.show()
                }
                if (null != autoCloseSeconds && autoCloseSeconds > 0) {
                    delay(autoCloseSeconds * 1000L)
                    withContext(Dispatchers.Main) {
                        if (null != dialog && dialog!!.isShowing) {
                            dialog!!.dismiss()
                        }
                    }
                }
            }
        }
    }
}