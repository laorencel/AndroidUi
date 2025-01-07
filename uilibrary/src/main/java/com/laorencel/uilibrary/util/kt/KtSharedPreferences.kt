package com.laorencel.uilibrary.util.kt

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

object KtSharedPreferences {
    private lateinit var gson: Gson
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        init(context, null)
    }

    fun init(context: Context, name: String?) {
        gson = Gson()
        sharedPreferences = context.applicationContext.getSharedPreferences(
            if (!isEmpty(name)) name else "AppSharedPreferences",
            Context.MODE_PRIVATE
        )
    }

    fun put(key: String?, obj: Any): KtSharedPreferences {
        // key 不为null时再存入，否则不存储
        if (!isEmpty(key)) {
//            if (null == sharedPreferences) {
//                throw new Exception("you must init AppSharedPreferences!");
//            }
            val editor = sharedPreferences.edit()
            if (obj is Int) {
                editor.putInt(key, obj)
            } else if (obj is Long) {
                editor.putLong(key, obj)
            } else if (obj is Boolean) {
                editor.putBoolean(key, obj)
            } else if (obj is Float) {
                editor.putFloat(key, obj)
            } else if (obj is String) {
                editor.putString(key, obj.toString())
            } else {
                if (isEmpty(obj)) {
                    editor.putString(key, "")
                } else {
                    try {
                        //对象类型，gson转换为String存储
                        val jsonOb = gson!!.toJson(obj)
                        editor.putString(key, jsonOb)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        editor.putString(key, "")
                    }
                }
            }
            //apply() 会立即更改内存中的 SharedPreferences 对象，但会将更新异步写入磁盘。
            // 或者，您也可以使用 commit() 将数据同步写入磁盘。但是，由于 commit() 是同步的，您应避免从主线程调用它，因为它可能会暂停您的界面呈现。
            editor.apply()
        }
        return this
    }

    fun <T> getObject(key: String?, tClass: Class<T>?): T? {
        val value = sharedPreferences.getString(key, "")
        if (!isEmpty(value)) {
            return try {
                gson!!.fromJson(value, tClass)
            } catch (e: Exception) {
                null
            }
        }
        return null
    }

    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun getFloat(key: String?, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    fun getInt(key: String?, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun getLong(key: String?, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun getString(key: String?, defaultValue: String?): String? {
        return sharedPreferences.getString(key, defaultValue)
    }
}