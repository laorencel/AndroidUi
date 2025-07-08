package com.laorencel.ui.test.kt

import android.Manifest
import android.content.ComponentName
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.laorencel.ui.R
import com.laorencel.ui.databinding.ActivityTestM3ButtonBinding
import com.laorencel.ui.databinding.ActivityTestM3ButtonFooterBinding
import com.laorencel.ui.test.kt.frag.FragTitle
import com.laorencel.uilibrary.ui.KtCommonActivity
import com.laorencel.uilibrary.util.kt.GsonUtil
import com.laorencel.uilibrary.util.kt.PermissionRequest
import com.laorencel.uilibrary.util.kt.TipUtil
import com.laorencel.uilibrary.util.kt.log.logD
import kotlinx.coroutines.launch
import throttleClick

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

//        testGson()
//        testConfirmDialog()

        contentBinding.btnShape.throttleClick {
//            getPackageInfo()
            switchShowApp(false)
//            lifecycleScope.launch {
//                val result = TipUtil.showInputDialog("输入框", "请输入")
//                println("result $result")
//            }
        }
    }

    private fun switchShowApp(show: Boolean) {
        val packageManager = packageManager;
//        val componentName = ComponentName(this, "com.laorencel.ui.main.MainActivity");
        val componentName =
            ComponentName("com.yx.steammachine", "com.yx.steammachine.ui.mainui.MainUiActivity");
        Log.d("test", componentName.toString())
//        val res = packageManager.getComponentEnabledSetting(componentName);
//        if (res == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
//            || res == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
//        ) {
        if (!show) {
            // 隐藏应用图标
            packageManager.setComponentEnabledSetting(
                componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            );
        } else {
            // 显示应用图标
            packageManager.setComponentEnabledSetting(
                componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                PackageManager.DONT_KILL_APP
            );
        }
    }

    private fun getPackageInfo() {

        val packages = getPackageManager().getInstalledPackages(0).filter { !isSystemApp(it) };
        logD("getPackageInfo ${packages.size}")
        packages.forEach { packageInfo ->
            logD("$packageInfo")
//            logD(
//                "appName:${
//                    packageInfo.applicationInfo.loadLabel(getPackageManager()).toString()
//                }," +
//                        " packageName:${packageInfo.packageName} "
//            )
        }
    }
    fun isSystemApp(pi: PackageInfo): Boolean {
        val isSysApp = (pi.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 1
        val isSysUpd = (pi.applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1
        return isSysApp || isSysUpd
    }
    fun testGson() {
        val title = FragTitle("tag1", "title1")
        val titleString = GsonUtil.toJson(title)
        val titleConvert: FragTitle? = GsonUtil.fromJson(titleString, FragTitle::class.java)
        println("titleString $titleString, title:${title.toString()}, titleConvert:${titleConvert.toString()}")

        val list = listOf(
            FragTitle("tag2", "title2"),
            FragTitle("tag3", "title3"),
        )
        val listString = GsonUtil.toJson(list)
        val listConvert: List<FragTitle>? = GsonUtil.fromJsonList(listString, FragTitle::class.java)
        println("listString $listString, list:${list.toString()}, listConvert:${listConvert.toString()}")
    }

    fun testConfirmDialog() {
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