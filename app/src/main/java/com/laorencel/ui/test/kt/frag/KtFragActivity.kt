package com.laorencel.ui.test.kt.frag

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.laorencel.ui.R
import com.laorencel.ui.databinding.ActivityFragBinding
import com.laorencel.uilibrary.ui.KtCommonActivity
import com.laorencel.uilibrary.ui.KtViewModel
import com.laorencel.uilibrary.ui.adapter.KtRecyclerViewAdapter

class KtFragActivity:KtCommonActivity<ActivityFragBinding,KtViewModel>() {
    override fun layoutID(): Int {
        return R.layout.activity_frag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAdapter()

        switchFragment("frag1")
    }


    private fun initAdapter() {
        val list = mutableListOf(
            FragTitle("frag1","frag1", true),
            FragTitle("frag2","frag2", true),
            FragTitle("frag3","frag3", true),
        )
        val layoutManager = LinearLayoutManager(this)
        val adapter = FragTitleAdapter()
        contentBinding.rvTitle.layoutManager = layoutManager
        contentBinding.rvTitle.adapter = adapter
        adapter.setOnItemClickListener(object :
            KtRecyclerViewAdapter.OnItemClickListener<FragTitle> {
            override fun onClick(view: View?, position: Int, data: FragTitle) {
                switchFragment(data.tag)
            }
        })
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