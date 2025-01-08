package com.laorencel.uilibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.laorencel.uilibrary.util.ClassUtil

/**
 * 通用Fragment
 * 没有实现任何布局，如果需要切换页面状态，须实现switchState方法
 */
abstract class KtFragment<VDB : ViewDataBinding, VM : KtViewModel> : KtAppUiFragment() {
    lateinit var contentBinding: VDB;
    lateinit var viewModel: VM;

    protected abstract fun layoutID(): Int

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (null == contentBinding) {
            contentBinding = DataBindingUtil.inflate(inflater, layoutID(), container, false)
            contentBinding.root.fitsSystemWindows = rootFitsSystemWindows()
        } else {
            val parent = contentBinding.root.parent as ViewGroup
            parent?.removeView(contentBinding.root)
        }
        return contentBinding.root
    }

    /**
     * onActivityCreated() 方法现已弃用。
     * 与 Fragment 视图有关的代码应在 onViewCreated()（在 onActivityCreated() 之前调用）中执行，而其他初始化代码应在 onCreate() 中执行。
     * 如需专门在 Activity 的 onCreate() 完成时接收回调，应在 onAttach() 中的 Activity 的 Lifecycle 上注册 LifeCycleObserver，并在收到 onCreate() 回调后将其移除。
     *
     * @param view               The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!this::viewModel.isInitialized) {
            viewModel = createViewModel() ?: (KtViewModel() as VM)
        }
    }

    protected open fun createViewModel(): VM? {
        val viewModelClass = ClassUtil.getViewModel<VM>(this, KtViewModel::class.java)
        if (null != viewModelClass) {
            return ViewModelProvider(this)[viewModelClass]
        }
        return null
    }
}