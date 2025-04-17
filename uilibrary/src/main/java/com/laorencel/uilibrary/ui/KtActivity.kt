package com.laorencel.uilibrary.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.laorencel.uilibrary.bean.Pagination
import com.laorencel.uilibrary.ui.adapter.KtRecyclerViewAdapter
import com.laorencel.uilibrary.util.ClassUtil
import com.laorencel.uilibrary.util.StatusBarUtil
import com.laorencel.uilibrary.util.kt.isEmpty
import com.laorencel.uilibrary.util.kt.log.logE
import com.laorencel.uilibrary.widget.state.State

/**
 * 通用Activity
 * 没有实现任何布局，如果需要切换页面状态，须实现switchState方法
 */
abstract class KtActivity<VDB : ViewDataBinding, VM : KtViewModel> : KtAppUiActivity() {

    lateinit var contentBinding: VDB;
    lateinit var viewModel: VM;

    override fun createView(savedInstanceState: Bundle?) {
        //        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);//写法一
//        viewDataBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.activity_main,null,false);//写法二（主要用于Fragment和Adapter)
//        viewDataBinding = ActivityMainBinding.inflate(getLayoutInflater());//写法三
//        setContentView(viewDataBinding.getRoot());//写法二和写法三需要setContentView
        contentBinding = DataBindingUtil.setContentView(this, layoutID())
        contentBinding.root.fitsSystemWindows = rootFitsSystemWindows()

        val navigationBarHeight = StatusBarUtil.getNavigationBarHeight(this)
//            logE("navigationBarHeight navigationBarHeight:" + navigationBarHeight)
        contentBinding.root.setPadding(
            contentBinding.root.paddingLeft,
            contentBinding.root.paddingTop,
            contentBinding.root.paddingRight,
            contentBinding.root.paddingBottom + navigationBarHeight
        )

        viewModel = createViewModel() ?: (KtViewModel() as VM)

//        TipUtil.showConfirmDialog("tip","哈哈哈")
    }

    protected abstract fun layoutID(): Int

    protected open fun createViewModel(): VM? {
        val viewModelClass = ClassUtil.getViewModel<VM>(this, KtViewModel::class.java)
        if (null != viewModelClass) {
            return ViewModelProvider(this)[viewModelClass]
        }
        return null
    }

    /**
     * 通用设置列表数据，根据当前页数，数据是否为空，切换状态页面，如果不是第一页且数据为空，会将页数减一
     *
     * @param list       数据
     * @param adapter    BaseAdapter
     * @param pagination Pagination
     * @param switchState 如果数据为空时，是否切换状态页面，默认true
     * @param <T>        数据类型
    </T> */
    open fun <T> setListData(
        list: List<T>?,
        adapter: KtRecyclerViewAdapter<T>,
        pagination: Pagination,
        switchState: Boolean = true
    ) {
        if (switchState && isEmpty(list)) {
            if (pagination.isStartPage) {
                switchState(State.EMPTY)
            } else {
                switchState(State.CONTENT)
                pagination.minusPage()
            }
            return
        }
        switchState(State.CONTENT)
        if (pagination.isStartPage) {
            adapter.setListData(list)
        } else {
            adapter.addAll(list)
        }
    }

}