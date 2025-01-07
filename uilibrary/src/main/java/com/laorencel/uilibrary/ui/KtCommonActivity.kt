package com.laorencel.uilibrary.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.laorencel.uilibrary.R
import com.laorencel.uilibrary.databinding.ActivityBaseKtCommonBinding
import com.laorencel.uilibrary.util.StatusBarUtil
import com.laorencel.uilibrary.widget.state.State
import com.laorencel.uilibrary.widget.state.bean.StateItem

/**
 * 通用Activity，适用不需要下拉刷新及上拉加载更多功能的界面。
 * 已实现基础布局，包含toolbar和bottomAppBar以及stateLayout,只需实现layoutID()方法返回布局文件
 */
abstract class KtCommonActivity<VDB : ViewDataBinding, VM : KtViewModel> : KtActivity<VDB, VM>() {

    protected lateinit var baseBinding: ActivityBaseKtCommonBinding

    //页面底部（添加在bottomAppbar內，stateLayout下面，固定在底部）
    protected var footerBinding: ViewDataBinding? = null

    /**
     * 页面底部布局资源layoutID（添加在bottomAppbar內，stateLayout下面，固定在底部）
     */
    protected open fun footerLayoutID(): Int {
        return -1
    }

    override fun createView(savedInstanceState: Bundle?) {
        super.createView(savedInstanceState)


        //        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);//写法一
//        viewDataBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.activity_main,null,false);//写法二（主要用于Fragment和Adapter)
//        viewDataBinding = ActivityMainBinding.inflate(getLayoutInflater());//写法三
//        setContentView(viewDataBinding.getRoot());//写法二和写法三需要setContentView
        baseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_kt_common)
        baseBinding.root.fitsSystemWindows = rootFitsSystemWindows()

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
                contentBinding.root.minimumHeight
                baseBinding.llContent.addView(contentBinding.root)

                baseBinding.nestedScrollView.post {
                    //设置contentBinding的最小高度为nestedScrollView的高度，
                    // 这样contentBinding如果设置背景色，不会出现跟baseBinding的背景色不一致情况
//                    val width = baseBinding.nestedScrollView.width
                    val height = baseBinding.nestedScrollView.height
                    contentBinding.root.minimumHeight = height
                    baseBinding.stateLayout.minimumHeight = height
                }
            }
        }


        //页面底部（添加在bottomAppbar內，stateLayout下面，固定在底部）
        if (footerLayoutID() != -1) {
            footerBinding = DataBindingUtil.inflate<ViewDataBinding>(
                layoutInflater,
                footerLayoutID(),
                null,
                false
            )
            if (null != footerBinding && null != baseBinding) {
                val params = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                footerBinding!!.root.layoutParams = params
                baseBinding.llBottom.addView(footerBinding!!.root)
                baseBinding.llBottom.visibility = View.VISIBLE


                if (null != contentBinding) {
                    val navigationBarHeight = StatusBarUtil.getNavigationBarHeight(this)
//                    Log.e("llBottom.getHeight", "$navigationBarHeight hasNavigationBar")
                    footerBinding!!.root.setPadding(
                        footerBinding!!.root.paddingLeft,
                        footerBinding!!.root.paddingTop,
                        footerBinding!!.root.paddingRight,
                        footerBinding!!.root.paddingBottom + navigationBarHeight
                    )
                }
            }
        } else {
            val navigationBarHeight = StatusBarUtil.getNavigationBarHeight(this)
//            Log.e("navigationBarHeight", "navigationBarHeight:" + navigationBarHeight)
            baseBinding.root.setPadding(
                baseBinding.root.paddingLeft,
                baseBinding.root.paddingTop,
                baseBinding.root.paddingRight,
                baseBinding.root.paddingBottom + navigationBarHeight
            )
            baseBinding.llBottom.visibility = View.GONE
        }

        if (null != baseBinding) {
            setToolbar(baseBinding.toolbar)

            baseBinding.stateLayout.setOnStateClickListener { view, state ->
                onStateClick(
                    view,
                    state
                )
            }
        }
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