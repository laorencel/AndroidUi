package com.laorencel.uilibrary.ui

import android.content.DialogInterface
import android.view.View
import com.laorencel.uilibrary.util.kt.ActivityManager
import com.laorencel.uilibrary.util.kt.TipUtil
import com.laorencel.uilibrary.widget.state.State
import com.laorencel.uilibrary.widget.state.bean.StateItem

/**
 * 通用Activity/Fragment常用操作。
 */
interface KtAppUi {

    /**
     * 下拉刷新是否可用
     * 主要用于一些可刷新加载更多组件包装
     *
     * @return boolean
     */
    fun refreshEnable(): Boolean {
        return false
    }

    /**
     * 加载更多是否可用
     *
     * @return boolean
     */
    fun loadMoreEnable(): Boolean {
        return false
    }

    /**
     * 自动刷新
     */
    fun autoRefresh() {
        onRefresh(null)
    }

    /**
     * 自动加载更多
     */
    fun autoLoadMore() {
        onLoadMore(null)
    }

    /**
     * 结束刷新
     */
    fun finishRefresh() {}

    /**
     * 结束上拉加载更多
     */
    fun finishLoadMore() {}

    /**
     * 结束上拉加载更多并通知组件没有更多数据了
     */
    fun finishLoadMoreWithNoMoreData() {
    }

    fun finishLoadMore(withMoreData: Boolean) {
        if (withMoreData) {
            finishLoadMore()
        } else {
            finishLoadMoreWithNoMoreData()
        }
    }

    /**
     * 刷新回调，接口请求在这里实现
     *
     * @param object Object
     */
    fun onRefresh(`object`: Any?) {
    }

    /**
     * 加载更多回调，接口请求在这里实现
     *
     * @param object Object
     */
    fun onLoadMore(`object`: Any?) {
    }


    /**
     * 状态页面切换
     *
     * @param state State状态
     */
    fun switchState(state: State?) {
        switchState(state, null)
    }

    /**
     * 状态页面切换
     *
     * @param state State状态
     * @param item  StateItem配置
     */
    fun switchState(state: State?, item: StateItem?) {
//        if (null != baseUiBinding)
//            baseUiBinding.stateLayout.switchState(state, item);
    }

    /**
     * 状态页面点击
     *
     * @param view  点击的View
     * @param state [State]
     */
    fun onStateClick(view: View?, state: State?) {
    }

    fun showSnackbar(content: String?) {
        if (!content.isNullOrEmpty()) {
            TipUtil.showSnackbar(content)
        }
    }

    fun showSnackbar(stringId: Int) {
        TipUtil.showSnackbar(
            ActivityManager.getCurrentActivity()?.resources?.getString(stringId)
        )
    }

    fun showToast(stringId: Int) {
        TipUtil.showToast(
            ActivityManager.getCurrentActivity()?.resources?.getString(stringId),
            ActivityManager.getCurrentActivity()
        )
    }

    fun showToast(content: String?) {
        TipUtil.showToast(
            content,
            ActivityManager.getCurrentActivity()
        )
    }

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
        TipUtil.showConfirmDialog(
            title,
            content,
            confirmText,
            cancelText,
            cancelable,
            onConfirmListener,
            onCancelListener,
            autoCloseSeconds
        )
    }


    fun showProgress(show: Boolean) {
        showProgress(show, "", true)
    }

    fun showProgress(
        show: Boolean,
        message: String?,
        cancelable: Boolean,
        progress: Int? = null,
        maxProgress: Int? = null
    )
}