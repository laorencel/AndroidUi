package com.laorencel.uilibrary.util.kt

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.laorencel.uilibrary.R
import com.laorencel.uilibrary.util.kt.log.logE
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        autoCloseSeconds: Long? = 0
    ): Boolean? = suspendCoroutine { coroutine ->
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
                if (null != autoCloseSeconds && autoCloseSeconds > 0) {
                    launch {
                        delay(autoCloseSeconds * 1000L)
                        if (null != dialog && dialog.isShowing) {
                            dialog.dismiss()
                            coroutine.resume(null)
                        }
                    }
                }
            } else {
                coroutine.resume(null)
            }
        }

    }

    suspend fun showInputDialog(
        title: String? = null,
        placeholder: String? = null,
        confirmText: String? = null,
        cancelText: String? = null,
        inputType: Int = InputType.TYPE_NULL,
        autoCloseSeconds: Long? = 0
    ): String? = suspendCoroutine { coroutine ->
        mainScope.launch {
            val currentActivity: Activity? = ActivityManager.getCurrentActivity()
            if (null != currentActivity && !currentActivity.isFinishing) {
                var dialog: AlertDialog? = null;
                val editText = EditText(currentActivity)
                editText.hint = placeholder
                editText.inputType = inputType
                val builder = MaterialAlertDialogBuilder(currentActivity)
                    .setTitle(title)
                    .setView(editText)
                    .setCancelable(false)
                    .setPositiveButton(
                        if (isEmpty(confirmText)) currentActivity.resources.getString(
                            R.string.confirm
                        ) else confirmText
                    ) { dialog, which -> coroutine.resume(editText.text.toString()) }
                    .setNegativeButton(
                        if (isEmpty(cancelText)) currentActivity.resources.getString(
                            R.string.cancel
                        ) else cancelText
                    ) { dialog, which -> coroutine.resume(null) }

                dialog = builder.show()

                if (null != autoCloseSeconds && autoCloseSeconds > 0) {
                    launch {
                        delay(autoCloseSeconds * 1000L)
                        if (null != dialog && dialog.isShowing) {
                            dialog.dismiss()
                            coroutine.resume(null)
                        }
                    }
                }
            } else {
                coroutine.resume(null)
            }
        }

    }

    private var progressDialog: ProgressDialog? = null;

    //上次加载弹窗message内容
    private var lastProgressMessage: String? = null
    fun showProgressDialog(
        show: Boolean,
        message: String? = null,
        cancelable: Boolean = true,
        progress: Int? = null,
        maxProgress: Int? = null,
    ) {
        mainScope.launch {
            if (show) {
                if (!isEmpty(lastProgressMessage) && !lastProgressMessage.equals(message)) {
                    //2次弹窗message不一样，销毁重新创建
                    destroyProgressDialog()
                }
                lastProgressMessage = message
                val currentActivity: Activity? = ActivityManager.getCurrentActivity()

                if (progressDialog == null) {
                    if (null != currentActivity && !currentActivity.isFinishing) {
                        progressDialog = ProgressDialog(currentActivity)
                        progressDialog!!.setCancelable(cancelable) //设置是否可以通过点击Back键取消
                        progressDialog!!.setCanceledOnTouchOutside(cancelable) //设置在点击Dialog外是否取消Dialog进度条
                        progressDialog!!.setTitle(if (!isEmpty(message)) message else "加载中")
//                        progressDialog!!.setMessage(if (!isEmpty(message)) message else "加载中")
                    }
                }
                if (null != progress && null != maxProgress) {
                    progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                    // 这里设置的是是否显示进度,设为false才是显示的哦！
                    progressDialog!!.isIndeterminate = false
                    progressDialog!!.max = maxProgress
                    progressDialog!!.progress = progress
//                if (progress >= maxProgress) {
//                    destroyProgress()
//                }
                }
                //            Logger.d("progressDialog!!.isShowing()" + (progressDialog!!.isShowing()));
                if (progressDialog != null && !progressDialog!!.isShowing()) {
                    progressDialog!!.show()
                }
            } else {
                destroyProgressDialog()
            }
        }
    }

    fun destroyProgressDialog() {
        mainScope.launch {
            if (progressDialog != null) {
                if (progressDialog!!.isShowing()) {
                    progressDialog!!.dismiss()
                }
                progressDialog = null
            }
        }
    }


    private var autoProgressDialogJob: Job? = null
    fun cancelAutoProgressDialog() {
        if (null != autoProgressDialogJob && autoProgressDialogJob!!.isActive) {
            autoProgressDialogJob!!.cancel()
            autoProgressDialogJob = null
        }
        showProgressDialog(false)
    }

    /**
     * 自增长进度条，一般用于网络请求进度加载（不知道进度）
     * @param autoClose 达到maxProgress后是否自动关闭弹窗
     */
    fun showAutoProgressDialog(
        message: String,
        step: Int,
        maxProgress: Int,
        intervalMillis: Long,
        autoClose: Boolean = false
    ) {
        //自增长进度条，一般用于网络请求进度加载（不知道进度）
        cancelAutoProgressDialog()
        autoProgressDialogJob = mainScope.launch(Dispatchers.IO) {
            var currentProgress = 0
            withContext(Dispatchers.Main) {
                showProgressDialog(true, message, false, currentProgress, maxProgress)
            }

            repeat((maxProgress / step)) {
                delay(intervalMillis)
                currentProgress += step
                if (!autoClose) {
                    //如果不是自动关闭弹窗，currentProgress达到maxProgress减1
                    if (currentProgress >= maxProgress) {
                        currentProgress = maxProgress - 1
                    }
                }
                withContext(Dispatchers.Main) {
                    if (currentProgress >= maxProgress) {
                        showProgressDialog(false)
                    } else {
                        showProgressDialog(true, message, false, currentProgress, maxProgress)
                    }
                }
            }
        }
    }
}