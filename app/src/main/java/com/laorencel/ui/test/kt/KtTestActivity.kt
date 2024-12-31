package com.laorencel.ui.test.kt

import android.os.Bundle
import android.util.Log
import com.laorencel.ui.R
import com.laorencel.ui.databinding.ActivityTestM3ButtonBinding
import com.laorencel.ui.databinding.ActivityTestM3ButtonFooterBinding
import com.laorencel.uilibrary.ui.KtCommonActivity
import com.laorencel.uilibrary.ui.KtRefreshActivity
import com.laorencel.uilibrary.util.kt.TipUtil

class KtTestActivity : KtCommonActivity<ActivityTestM3ButtonBinding, KtTestVM>() {
    override fun layoutID(): Int {
        return R.layout.activity_test_m3_button
    }

    override fun footerLayoutID(): Int {
        return R.layout.activity_test_m3_button_footer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.test()

        (footerBinding as ActivityTestM3ButtonFooterBinding).appUi = this

        TipUtil.showConfirmDialog(
            "6秒自动关闭",
            "hahasdfksjf啊啥；都快放假啊啥的六块腹肌啊啥；的饭卡加水淀粉",
            autoCloseSeconds = 6
        )
    }

    override fun onRefresh(`object`: Any?) {
        super.onRefresh(`object`)
        Log.d("KtTestActivity", "onRefresh")
    }

    override fun onLoadMore(`object`: Any?) {
        super.onLoadMore(`object`)
        Log.d("KtTestActivity", "onLoadMore")
    }

    override fun refreshEnable(): Boolean {
        return true
    }

    override fun loadMoreEnable(): Boolean {
        return true
    }
}