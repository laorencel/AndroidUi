package com.laorencel.uilibrary.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.laorencel.uilibrary.util.kt.TipUtil
import com.laorencel.uilibrary.util.kt.isEmpty

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
            TipUtil.showSnackbar(this.view, content)
        }
    }

    override fun showSnackbar(stringId: Int) {
        TipUtil.showSnackbar(this.view, resources.getString(stringId))
    }

    override fun showToast(stringId: Int) {
        TipUtil.showToast(context, resources.getString(stringId))
    }

    override fun showToast(content: String?) {
        if (!content.isNullOrEmpty()) {
            TipUtil.showToast(context, content)
        }
    }

    //上次加载弹窗message内容
    private val lastProgressMessage: String? = null

    override fun showProgress(show: Boolean) {
        showProgress(show, "", true)
    }

    override fun showProgress(show: Boolean, message: String?, cancelable: Boolean) {
        if (show) {
            if (!isEmpty(lastProgressMessage) && !lastProgressMessage.equals(message)) {
                //2次弹窗message不一样，销毁重新创建
                destroyProgress()
            }
            if (progressDialog == null) {
                progressDialog = ProgressDialog(context)
                progressDialog!!.setCancelable(cancelable) //设置是否可以通过点击Back键取消
                progressDialog!!.setCanceledOnTouchOutside(cancelable) //设置在点击Dialog外是否取消Dialog进度条
                progressDialog!!.setMessage(if (!isEmpty(message)) message else "加载中")
            }
            //            Logger.d("progressDialog!!.isShowing()" + (progressDialog!!.isShowing()));
            if (progressDialog != null && !progressDialog!!.isShowing) {
                progressDialog!!.show()
            }
        } else {
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
        }
    }

    protected fun destroyProgress() {
        if (progressDialog != null) {
            if (progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
            progressDialog = null
        }
    }

    override fun onDestroy() {
        destroyProgress()
        super.onDestroy()
    }
}