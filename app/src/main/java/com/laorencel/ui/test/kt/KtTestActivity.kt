package com.laorencel.ui.test.kt

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.laorencel.ui.R
import com.laorencel.ui.databinding.ActivityTestM3ButtonBinding
import com.laorencel.ui.databinding.ActivityTestM3ButtonFooterBinding
import com.laorencel.uilibrary.ui.KtCommonActivity
import com.laorencel.uilibrary.util.kt.PermissionRequest
import com.laorencel.uilibrary.util.kt.TipUtil
import kotlinx.coroutines.launch

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

        PermissionRequest.register(this)

        TipUtil.showConfirmDialog(
            "6秒自动关闭",
            "hahasdfksjf啊啥；都快放假啊啥的六块腹肌啊啥；的饭卡加水淀粉",
            autoCloseSeconds = 6,
            onConfirmListener = { dialog, which ->
                lifecycleScope.launch {
                    val result = PermissionRequest.request(
                        listOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_PHONE_STATE,
                        ),
//                        mapOf(
//                            Manifest.permission.CAMERA to "用于拍照",
//                            Manifest.permission.READ_CONTACTS to "用于读取联系人",
//                            Manifest.permission.READ_PHONE_STATE to "用于读取手机状态",
//                        ),
                    ).await()
                    println("PermissionRequest result:$result")
//                    if (!result.granted){
//                       val keys= result.permissionMap.filter { it.value==null||!it.value!! }.map{it.key}
//                    }
                }

            }
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