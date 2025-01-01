package com.laorencel.uilibrary.util.kt

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.laorencel.uilibrary.R
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

/**
 * 申请权限结果
 * granted：是否通过申请
 * permissionMap: Map<String, Boolean?> permission对应是否通过 true，false（null视为false，可能是
 */
data class PermissionResult(val granted: Boolean, val permissionMap: Map<String, Boolean?>)


private var permissionDescList: List<Map<String, Any>>? = null

fun getPermissionDescList(): List<Map<String, Any>>? {
    if (isEmpty(permissionDescList)) {
        val fileName = "PermissionJson"
        val jsonString = ActivityManager.getCurrentActivity()?.assets?.open(fileName)
            ?.bufferedReader()?.use {
                it.readText()
            }
        val gson = Gson()
        var json = gson.fromJson<Map<String, Any>>(jsonString, Map::class.java)
        var list = json["PermissionList"] as List<Map<String, Any>>
        permissionDescList = list
    }
    return permissionDescList
}

fun getPermissionDescList(permissions: List<String>): List<Map<String, Any>>? {
    var desc: List<Map<String, Any>>? = null
    val permissionDescList = getPermissionDescList()
    if (!isEmpty(permissionDescList)) {
        desc = permissionDescList!!.filter { permissions.contains(it["Key"] as String) }
    }
    println("getPermissionDescList:$desc")
    return desc
}

/**
 * 权限描述，从assets/PermissionJson文件中获取。
 * Map中包含：Key(String 对应permission),Title(String 简要描述),Memo(String 详细描述),Level(Integer 等级)
 */
fun getPermissionDesc(permission: String): Map<String, Any>? {
    var desc: Map<String, Any>? = null
    val permissionDescList = getPermissionDescList()
    if (!isEmpty(permissionDescList)) {
        desc = permissionDescList!!.find { permission.equals(it["Key"] as String?, true) }
    }
    println("getPermissionDesc:$desc")
    return desc
}


/**
 * 权限申请工具
 */
object PermissionRequest {
    private var context: WeakReference<ComponentActivity>? = null
    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null
    private val permissions: MutableList<String> = mutableListOf()
    private val permissionsMap: MutableMap<String, Boolean?> = mutableMapOf()
    private var permissionsTip: MutableMap<String, String?> = mutableMapOf()
    private var currentIndex: Int = 0
    private var allGranted: CompletableDeferred<PermissionResult>? = null

    /**
     * 必须在Activity.onCreate中（或之前）调用，使用registerForActivityResult方法。
     * 内部对activity弱引用
     */
    fun register(activity: ComponentActivity?) {
        context = WeakReference(activity)
        if (null != context!!.get()) {
            requestPermissionLauncher =
                context!!.get()!!.registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    val permission = permissions[currentIndex]
                    permissionsMap[permission] = isGranted

//                    println(
//                        "ccc currentIndex:$currentIndex,permission:$permission," +
//                                "permissionsMap:${permissionsMap}"
//                    )

                    if (currentIndex == permissions.size - 1) {
                        val denied =
                            permissionsMap.values.find { result -> null == result || !result }
                        allGranted?.complete(
                            PermissionResult(
                                null == denied,
                                permissionsMap
                            )
                        )
                    } else {
                        currentIndex++
                        requestPermission()
                    }
                }
        }
    }

    /**
     * 申请单个权限
     * permission 单个权限
     * permissionsTip 权限被拒后重新申请的提示
     */
    suspend fun request(
        permission: String,
        tip: String?
    ): CompletableDeferred<PermissionResult> {
        var tipMap: MutableMap<String, String>? = null
        if (!isEmpty(tip)) {
            tipMap = mutableMapOf()
            tipMap[permission] = tip!!
        }
        return request(listOf(permission), tipMap)
    }

    /**
     * 申请多个权限
     * permissions 多个权限。
     * permissionsTip 每个权限被拒后重新申请的提示。
     */
    suspend fun request(
        permissions: List<String>,
        permissionsTip: Map<String, String>? = null
    ): CompletableDeferred<PermissionResult> = withContext(Dispatchers.Main) {
        allGranted = CompletableDeferred();

        if (!isEmpty(permissions)) {
            this@PermissionRequest.permissions.clear()
            permissionsMap.clear()
            this@PermissionRequest.permissionsTip.clear()
            for (permission in permissions) {
                this@PermissionRequest.permissions.add(permission)
                permissionsMap[permission] = null
                this@PermissionRequest.permissionsTip[permission] = permissionsTip?.get(permission)
            }
            currentIndex = 0

            requestPermission()
        } else {
            allGranted!!.complete(PermissionResult(false, mapOf()))
        }

        allGranted!!
    }

    private fun requestPermission() {
        if (null != context?.get() && null != requestPermissionLauncher) {
            val permission = permissions[currentIndex]
            when {
                ContextCompat.checkSelfPermission(
                    context!!.get()!!,
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    //已获得权限
                    permissionsMap[permission] = true

//                    println("aaa currentIndex:$currentIndex,permission:$permission,permissionsMap:${permissionsMap}")
                    if (currentIndex == permissions.size - 1) {
                        val denied =
                            permissionsMap.values.find { result -> null == result || !result }
                        allGranted!!.complete(
                            PermissionResult(
                                null == denied,
                                permissionsMap
                            )
                        )
                    } else {
                        currentIndex++
                        requestPermission()
                    }
                }

                ActivityCompat.shouldShowRequestPermissionRationale(
                    context!!.get()!!,
                    permission
                ) -> {
                    //应用认为其应该显示权限请求的理由
                    //当用户之前拒绝了某个权限的请求，但没有选择“禁止后不再询问”的选项时，再次请求该权限时，
                    //shouldShowRequestPermissionRationale将返回true，表示此时应该向用户展示一个解释为什么需要这个权限的对话框
                    var content: String? = permissionsTip[permission]
                    if (isEmpty(content)) {
                        content = getPermissionDesc(permission)?.get("Memo") as String?
                    }

                    TipUtil.showConfirmDialog(title = context!!.get()!!
                        .getString(R.string.permission_required),
                        content = content,
                        onCancelListener = { dialog, which ->
                            permissionsMap[permission] = false

//                            println("bbb currentIndex:$currentIndex,permission:$permission,permissionsMap:${permissionsMap}")
                            if (currentIndex == permissions.size - 1) {
                                val denied =
                                    permissionsMap.values.find { result -> null == result || !result }
                                allGranted?.complete(
                                    PermissionResult(
                                        null == denied,
                                        permissionsMap
                                    )
                                )
                            } else {
                                currentIndex++
                                requestPermission()
                            }
                        },
                        onConfirmListener = { dialog, which ->
                            requestPermissionLauncher!!.launch(
                                permission
                            )
                        }
                    )
                }

                else -> {
                    //尚未收到权限请求
                    requestPermissionLauncher!!.launch(
                        permission
                    )
                }
            }
        } else {
            allGranted?.complete(
                PermissionResult(
                    false,
                    permissionsMap
                )
            )
        }
    }
}

