package com.laorencel.ui.test.kt.frag

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.laorencel.ui.R
import com.laorencel.ui.databinding.ActivityFragBinding
import com.laorencel.uilibrary.ui.KtCommonActivity
import com.laorencel.uilibrary.ui.KtViewModel
import com.laorencel.uilibrary.ui.adapter.KtRecyclerViewAdapter
import com.laorencel.uilibrary.util.kt.LiveDataBus
import com.laorencel.uilibrary.util.kt.TipUtil
import kotlinx.coroutines.launch

class KtFragActivity : KtCommonActivity<ActivityFragBinding, KtViewModel>() {
    override fun layoutID(): Int {
        return R.layout.activity_frag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        LiveDataBus.with("key_test", FragTitle::class.java, true)
            .observe(this) {
                println("LiveDataBus key_test $it")
                showSnackbar(it?.toString())
            }

//        lifecycleScope.launch{
//           val result = TipUtil.showConfirmDialogWithResult("确定？")
//            println("showConfirmDialogWithResult result:$result")
//        }
//        println("onCreate showConfirmDialogWithResult ")

        initAdapter()

        switchFragment("frag1")
    }


    private fun initAdapter() {
        val list = mutableListOf(
            FragTitle("frag1", "frag1", true),
            FragTitle("frag2", "frag2", true),
            FragTitle("frag3", "frag3", true),
        )
        val layoutManager = LinearLayoutManager(this)
        val adapter = FragTitleAdapter()
        contentBinding.rvTitle.layoutManager = layoutManager
        contentBinding.rvTitle.adapter = adapter
        adapter.onItemClickListener = object :
            KtRecyclerViewAdapter.OnItemClickListener<FragTitle> {
            override fun onClick(view: View?, position: Int, data: FragTitle) {
                switchFragment(data.tag)
            }
        }
        adapter.setListData(list)
    }

    private var currentFragment: Fragment? = null

    private fun switchFragment(tag: String) {
        val fragmentManager = supportFragmentManager
        var fragment = fragmentManager.findFragmentByTag(tag)
        if (null == fragment) {
            fragment = getFragment(tag)
        }
        if (null != fragment) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            if (null != currentFragment && currentFragment!!.isAdded) {
                fragmentTransaction.hide(currentFragment!!)
            }
            if (fragment.isAdded) {
                fragmentTransaction.show(fragment)
            } else {
                fragmentTransaction.add(R.id.fragment_container, fragment, tag)
            }
            currentFragment = fragment
            //            fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.replace(R.id.fragment, fragment, tag);
            fragmentTransaction.commit()
        }
    }

    private fun getFragment(tag: String): Fragment? {
        return TestFragment(tag)
    }
}