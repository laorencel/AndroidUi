package com.laorencel.uilibrary.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.laorencel.uilibrary.util.kt.TipUtil
import com.laorencel.uilibrary.util.kt.isEmpty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 通用Fragment，适用完全自定义布局界面。
 */
abstract class KtAppUiFragment : Fragment(), KtAppUi {
    private var progressDialog: ProgressDialog? = null;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView(inflater, container, savedInstanceState)
    }

    abstract fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?

    /**
     * 设置根组件是否setFitsSystemWindows为true，setFitsSystemWindows为true时，
     * 系统会为该View设置一个paddingTop，值为statusbar的高度。
     * 用户在布局文件中设置的padding会被忽略
     * 可参考 https://www.jianshu.com/p/5cc3bd23be7b
     *
     * @return 默认true，因为本UI框架已经设置透明状态栏及全屏
     */
    protected open fun rootFitsSystemWindows(): Boolean {
        return false
    }

    override fun showSnackbar(content: String?) {
        if (!content.isNullOrEmpty()) {
            TipUtil.showSnackbar(content)
        }
    }

    override fun showSnackbar(stringId: Int) {
        TipUtil.showSnackbar(resources.getString(stringId), this.view)
    }

    override fun showToast(stringId: Int) {
        TipUtil.showToast(resources.getString(stringId))
    }

    override fun showToast(content: String?) {
        if (!content.isNullOrEmpty()) {
            TipUtil.showToast(content, context)
        }
    }

    //上次加载弹窗message内容
    private var lastProgressMessage: String? = null

    override fun showProgress(show: Boolean) {
        showProgress(show, "", true)
    }

    override fun showProgress(
        show: Boolean,
        message: String?,
        cancelable: Boolean,
        progress: Int?,
        maxProgress: Int?
    ) {
        if (show) {
            if (!isEmpty(lastProgressMessage) && !lastProgressMessage.equals(message)) {
                //2次弹窗message不一样，销毁重新创建
                destroyProgressDialog()
            }
            lastProgressMessage = message
            if (progressDialog == null) {
                progressDialog = ProgressDialog(context)
                progressDialog!!.setCancelable(cancelable) //设置是否可以通过点击Back键取消
                progressDialog!!.setCanceledOnTouchOutside(cancelable) //设置在点击Dialog外是否取消Dialog进度条
                progressDialog!!.setTitle(if (!isEmpty(message)) message else "加载中")
//                progressDialog!!.setMessage(if (!isEmpty(message)) message else "加载中")
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
            if (progressDialog != null && !progressDialog!!.isShowing) {
                progressDialog!!.show()
            }
        } else {
            destroyProgressDialog()
        }
    }

    protected fun destroyProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
            progressDialog = null
        }
    }


    private var autoProgressDialogJob: Job? = null
    fun cancelAutoProgressDialog() {
        if (null != autoProgressDialogJob && autoProgressDialogJob!!.isActive) {
            autoProgressDialogJob!!.cancel()
            autoProgressDialogJob = null
        }
        showProgress(false)
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
        autoProgressDialogJob = lifecycleScope.launch(Dispatchers.IO) {
            var currentProgress = 0
            withContext(Dispatchers.Main) {
                showProgress(true, message, false, currentProgress, maxProgress)
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
                        showProgress(false)
                    } else {
                        showProgress(true, message, false, currentProgress, maxProgress)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        destroyProgressDialog()
        cancelAutoProgressDialog()
        super.onDestroy()
    }
}