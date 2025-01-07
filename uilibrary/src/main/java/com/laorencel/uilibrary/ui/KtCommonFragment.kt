package com.laorencel.uilibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.laorencel.uilibrary.R
import com.laorencel.uilibrary.databinding.FragmentBaseKtCommonBinding
import com.laorencel.uilibrary.widget.state.State
import com.laorencel.uilibrary.widget.state.StateLayout.OnStateClickListener
import com.laorencel.uilibrary.widget.state.bean.StateItem

/**
 * 通用Fragment，适用不需要下拉刷新及上拉加载更多功能的界面。
 * 已实现基础布局，包含headerLayout和footerLayout以及stateLayout,只需实现layoutID()方法返回布局文件
 */
abstract class KtCommonFragment<VDB : ViewDataBinding, VM : KtViewModel> : KtFragment<VDB, VM>() {

    protected lateinit var baseBinding: FragmentBaseKtCommonBinding

    //页面头部（添加在appbarLayout內，toolbar下面，stateLayout上面）
    protected var headerBinding: ViewDataBinding? = null

    //页面底部（添加在bottomAppbar內，stateLayout下面，固定在底部）
    protected var footerBinding: ViewDataBinding? = null

    /**
     * 页面头部布局资源layoutID(添加在appbarLayout內，toolbar下面，stateLayout上面)
     */
    protected fun headerLayoutID(): Int {
        return -1
    }

    /**
     * 页面底部布局资源layoutID(添加在bottomAppbar內，stateLayout下面，固定在底部)
     */
    protected fun footerLayoutID(): Int {
        return -1
    }

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        return super.createView(inflater, container, savedInstanceState);
        if (null == baseBinding) {
            baseBinding =
                DataBindingUtil.inflate(
                    inflater,
                    R.layout.fragment_base_kt_common,
                    container,
                    false
                )
            baseBinding.rlRoot.fitsSystemWindows = rootFitsSystemWindows()
            if (layoutID() != -1) {
                contentBinding = DataBindingUtil.inflate(layoutInflater, layoutID(), null, false)
                if (null != contentBinding && null != baseBinding) {
                    //这里的LayoutParams要看contentBinding是加载在哪个父组件里面，相应的获取RelativeLayout还是其他类型。
                    val params = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    contentBinding.root.layoutParams = params
                    baseBinding.llContent.addView(contentBinding.root)
                    baseBinding.nestedScrollView.post {
                        val height: Int = baseBinding.nestedScrollView.height
                        contentBinding.root.minimumHeight = height
                        baseBinding.stateLayout.minimumHeight = height
                    }
                }
            }

            //页面头部（添加在appbarLayout內，toolbar下面，stateLayout上面）
            if (headerLayoutID() != -1) {
                headerBinding =
                    DataBindingUtil.inflate(layoutInflater, headerLayoutID(), null, false)
                if (null != headerBinding && null != baseBinding) {
                    val params = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    headerBinding!!.root.layoutParams = params
                    baseBinding.topLayout.addView(headerBinding!!.root)
                }
            }
            //页面底部（添加在bottomAppbar內，stateLayout下面，固定在底部）
            if (footerLayoutID() != -1) {
                footerBinding =
                    DataBindingUtil.inflate(layoutInflater, footerLayoutID(), null, false)
                if (null != footerBinding && null != baseBinding) {
                    val params = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    footerBinding!!.root.layoutParams = params
                    baseBinding.bottomLayout.addView(footerBinding!!.root)
                    baseBinding.bottomLayout.visibility = View.VISIBLE
                }
            } else {
                baseBinding.bottomLayout.visibility = View.GONE
            }
        } else {
            val parent = baseBinding.root.parent as ViewGroup
            parent?.removeView(baseBinding.root)
        }

        if (null != baseBinding) {
            baseBinding.stateLayout.setOnStateClickListener(OnStateClickListener { view, state ->
                onStateClick(
                    view,
                    state
                )
            })
        }

        return baseBinding.getRoot()
    }

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