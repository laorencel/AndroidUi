package com.laorencel.uilibrary.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.laorencel.uilibrary.manager.UiWindowManager
import com.laorencel.uilibrary.util.kt.ActivityManager
import com.laorencel.uilibrary.util.kt.TipUtil
import com.laorencel.uilibrary.util.kt.isEmpty

/**
 * 通用Activity，适用完全自定义布局界面。
 */
abstract class KtAppUiActivity : AppCompatActivity(), KtAppUi {

    private var progressDialog: ProgressDialog? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityManager.setCurrentActivity(this)

        val uiWindowManager = UiWindowManager()
        uiWindowManager.applyEdgeToEdge(window, isEdgeToEdgeEnabled())
        uiWindowManager.setDefaultNightMode(defaultNightMode())

        createView(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        ActivityManager.setCurrentActivity(this)
    }

    protected abstract fun createView(savedInstanceState: Bundle?)

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

    /**
     * 是否设置EdgeToEdge
     *
     * @return true or false
     */
    protected open fun isEdgeToEdgeEnabled(): Boolean {
        return true
    }

    /**
     * 设置夜间模式
     *
     * @return 默认跟随系统 AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
     */
    protected open fun defaultNightMode(): Int {
        return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }


    protected open fun setToolbar(toolbar: Toolbar?) {
        if (null != toolbar) {
            setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener { view: View? ->
                onBackPressed()
            }
            //        toolbar.setNavigationIcon(R.drawable.ic_navigation);
            supportActionBar?.setDisplayHomeAsUpEnabled(true) //添加默认的返回图标
            supportActionBar?.setHomeButtonEnabled(true) //设置返回键可用
        }
    }

    protected open fun showToolbar(show: Boolean) {
        if (show) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
//        baseCommonBinding.appbarLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    override fun showSnackbar(content: String?) {
        if (!content.isNullOrEmpty()) {
            TipUtil.showSnackbar(content)
        }
    }

    override fun showSnackbar(stringId: Int) {
        TipUtil.showSnackbar(resources.getString(stringId), this.window?.decorView)
    }

    override fun showToast(stringId: Int) {
        TipUtil.showToast(resources.getString(stringId), this)
    }

    override fun showToast(content: String?) {
        if (!content.isNullOrEmpty()) {
            TipUtil.showToast(content, this)
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
                progressDialog = ProgressDialog(this)
                progressDialog!!.setCancelable(cancelable) //设置是否可以通过点击Back键取消
                progressDialog!!.setCanceledOnTouchOutside(cancelable) //设置在点击Dialog外是否取消Dialog进度条
                progressDialog!!.setMessage(if (!isEmpty(message)) message else "加载中")
            }
            //            Logger.d("progressDialog!!.isShowing()" + (progressDialog!!.isShowing()));
            if (progressDialog != null && !progressDialog!!.isShowing()) {
                progressDialog!!.show()
            }
        } else {
            if (progressDialog != null && progressDialog!!.isShowing()) {
                progressDialog!!.dismiss()
            }
        }
    }

    protected fun destroyProgress() {
        if (progressDialog != null) {
            if (progressDialog!!.isShowing()) {
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