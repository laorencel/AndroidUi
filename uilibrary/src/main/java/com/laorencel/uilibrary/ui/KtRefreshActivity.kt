package com.laorencel.uilibrary.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.laorencel.uilibrary.R
import com.laorencel.uilibrary.databinding.ActivityBaseKtRefreshBinding
import com.laorencel.uilibrary.util.StatusBarUtil
import com.laorencel.uilibrary.widget.state.State
import com.laorencel.uilibrary.widget.state.bean.StateItem
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 通用Activity，适用需要下拉刷新及上拉加载更多功能的界面。
 * 已实现基础布局，包含toolbar和bottomAppBar以及refreshLayout、stateLayout,只需实现layoutID()方法返回布局文件
 */
abstract class KtRefreshActivity<VDB : ViewDataBinding, VM : KtViewModel> : KtActivity<VDB, VM>() {
    protected lateinit var baseBinding: ActivityBaseKtRefreshBinding

    //页面头部（添加在appbarLayout內，toolbar下面，stateLayout上面）
    protected var headerBinding: ViewDataBinding? = null

    //页面底部（添加在bottomAppbar內，stateLayout下面，固定在底部）
    protected var footerBinding: ViewDataBinding? = null

    /**
     * 页面头部布局资源layoutID（添加在appbarLayout內，toolbar下面，stateLayout上面）
     */
    protected open fun headerLayoutID(): Int {
        return -1
    }

    /**
     * 页面底部布局资源layoutID（添加在bottomAppbar內，stateLayout下面，固定在底部）
     */
    protected open fun footerLayoutID(): Int {
        return -1
    }

    override fun createView(savedInstanceState: Bundle?) {
        //        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);//写法一
//        viewDataBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.activity_main,null,false);//写法二（主要用于Fragment和Adapter)
//        viewDataBinding = ActivityMainBinding.inflate(getLayoutInflater());//写法三
//        setContentView(viewDataBinding.getRoot());//写法二和写法三需要setContentView
        baseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_kt_refresh)
        baseBinding.clRoot.fitsSystemWindows = rootFitsSystemWindows()

        viewModel = createViewModel() ?: (KtViewModel() as VM)

        if (layoutID() != -1) {
            contentBinding = DataBindingUtil.inflate(layoutInflater, layoutID(), null, false)
            if (null != contentBinding && null != baseBinding) {
                //这里的LayoutParams要看contentBinding是加载在哪个父组件里面，相应的获取RelativeLayout还是其他类型。
                val params = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                contentBinding.root.layoutParams = params
                baseBinding.refreshLayout.addView(contentBinding.root)

                baseBinding.nestedScrollView.post {
//                    val width = baseBinding.nestedScrollView.width
                    val height = baseBinding.nestedScrollView.height
                    contentBinding.root.minimumHeight = height
                    baseBinding.stateLayout.minimumHeight = height
                }
            }
        }

        //页面头部（添加在appbarLayout內，toolbar下面，stateLayout上面）
        if (headerLayoutID() != -1) {
            headerBinding = DataBindingUtil.inflate(layoutInflater, headerLayoutID(), null, false)
            if (null != headerBinding && null != baseBinding) {
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                headerBinding!!.root.layoutParams = params
                baseBinding.appbarLayout.addView(headerBinding!!.root)
            }
        }
        //页面底部（添加在bottomAppbar內，stateLayout下面，固定在底部）
        if (footerLayoutID() != -1) {
            footerBinding = DataBindingUtil.inflate(layoutInflater, footerLayoutID(), null, false)
            if (null != footerBinding && null != baseBinding) {
                val params = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                footerBinding!!.root.layoutParams = params
                baseBinding.bottomAppbar.addView(footerBinding!!.root)
                baseBinding.bottomAppbar.visibility = View.VISIBLE

                val navigationBarHeight = StatusBarUtil.getNavigationBarHeight(this)
                if (null != baseBinding) {
                    baseBinding.refreshLayout.setPadding(
                        baseBinding.refreshLayout.paddingLeft,
                        baseBinding.refreshLayout.paddingTop,
                        baseBinding.refreshLayout.paddingRight,
                        baseBinding.refreshLayout.paddingBottom + navigationBarHeight
                    )
                }
            }
        } else {
            baseBinding.bottomAppbar.visibility = View.GONE
        }

        if (null != baseBinding) {
            setToolbar(baseBinding.toolbar)

//            Log.e(
//                "",
//                "refreshEnable():" + refreshEnable() + " loadMoreEnable():" + loadMoreEnable()
//            )
            baseBinding.refreshLayout.setEnableRefresh(refreshEnable())
            baseBinding.refreshLayout.setEnableLoadMore(loadMoreEnable())
            baseBinding.refreshLayout.setOnRefreshLoadMoreListener(object :
                OnRefreshLoadMoreListener {
                override fun onLoadMore(refreshLayout: RefreshLayout) {
                    this@KtRefreshActivity.onLoadMore(baseBinding.refreshLayout)
                }

                override fun onRefresh(refreshLayout: RefreshLayout) {
                    this@KtRefreshActivity.onRefresh(baseBinding.refreshLayout)
                }
            })

            baseBinding.stateLayout.setOnStateClickListener { view, state ->
                onStateClick(
                    view,
                    state
                )
            }
        }
    }

    override fun autoRefresh() {
        baseBinding.refreshLayout.autoRefresh()
    }

    override fun autoLoadMore() {
        baseBinding.refreshLayout.autoLoadMore()
    }

    override fun finishRefresh() {
        baseBinding.refreshLayout.finishRefresh()
    }

    override fun finishLoadMore() {
        baseBinding.refreshLayout.finishLoadMore()
    }

    override fun finishLoadMoreWithNoMoreData() {
        baseBinding.refreshLayout.finishLoadMoreWithNoMoreData()
    }

    /**
     * 状态页面切换
     *
     * @param state State状态
     * @param item  StateItem配置
     */
    override fun switchState(state: State?, item: StateItem?) {
        super.switchState(state, item)
        if (null != baseBinding) {
            if (state == State.CONTENT) {
                if (null != contentBinding) {
                    contentBinding.root.visibility = View.VISIBLE
                }
                baseBinding.stateLayout.visibility = View.GONE
            } else {
                if (null != contentBinding) {
                    contentBinding.root.visibility = View.GONE
                }
                baseBinding.stateLayout.visibility = View.VISIBLE
                baseBinding.stateLayout.switchState(state, item)
            }
        }
    }
}