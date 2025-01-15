package com.laorencel.ui.test.kt.frag

import android.os.Bundle
import android.view.View
import com.laorencel.ui.R
import com.laorencel.ui.databinding.FragTestBinding
import com.laorencel.uilibrary.ui.KtCommonFragment
import com.laorencel.uilibrary.ui.KtViewModel
import com.laorencel.uilibrary.util.kt.LiveDataBus
import throttleClick

class TestFragment(private var fragTag: String) : KtCommonFragment<FragTestBinding, KtViewModel>() {
    override fun layoutID(): Int {
        return R.layout.frag_test
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentBinding.tv.text = fragTag

        contentBinding.btnSendMsgByBus.throttleClick {
            LiveDataBus.with("key_test", FragTitle::class.java,false)
                .value = FragTitle("tag","title")
        }
    }
}