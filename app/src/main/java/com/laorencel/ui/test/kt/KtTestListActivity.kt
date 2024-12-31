package com.laorencel.ui.test.kt

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.laorencel.ui.R
import com.laorencel.ui.databinding.ActivityTestListBinding
import com.laorencel.uilibrary.ui.KtRefreshActivity
import com.laorencel.uilibrary.util.kt.isEmpty
import com.laorencel.uilibrary.widget.state.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KtTestListActivity : KtRefreshActivity<ActivityTestListBinding, KtTestListVM>() {
    private lateinit var adapter: KtTestListAdapter
    override fun layoutID(): Int {
        return R.layout.activity_test_list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutManager = LinearLayoutManager(this)
        contentBinding.rv.layoutManager = layoutManager
        adapter = KtTestListAdapter()
        contentBinding.rv.adapter = adapter

        onRefresh(null)
    }

    override fun refreshEnable(): Boolean {
        return true
    }

    override fun loadMoreEnable(): Boolean {
        return true
    }

    override fun onRefresh(`object`: Any?) {
        super.onRefresh(`object`)
        println("Thread aaa " + Thread.currentThread())
        lifecycleScope.launch {
            if (isEmpty(adapter.list)) {
                switchState(State.LOADING)
            }
            println("Thread bbb " + Thread.currentThread())
            val list = withContext(Dispatchers.IO) {
                println("Thread ccc " + Thread.currentThread())
                viewModel?.getList()
            };
            println("Thread ddd " + Thread.currentThread())
            println("list" + list.toString())
            adapter.setListData(list)
            finishRefresh()
            switchState(State.CONTENT)
        }
    }

    override fun onLoadMore(`object`: Any?) {
        super.onLoadMore(`object`)
        lifecycleScope.launch {
            println("Thread bbb " + Thread.currentThread())
            val list = withContext(Dispatchers.IO) {
                println("Thread ccc " + Thread.currentThread())
                viewModel?.getList()
            };
            println("Thread ddd " + Thread.currentThread())
            println("list" + list.toString())
            adapter.addAll(list)
            finishLoadMore()
        }
    }
}