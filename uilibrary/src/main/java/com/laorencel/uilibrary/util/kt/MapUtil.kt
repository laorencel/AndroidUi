package com.laorencel.uilibrary.util.kt

import com.google.gson.Gson

object MapUtil {
    private val gson by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Gson()
    }

    fun toMap(obj: Any): Map<String, Any?> {
        var map: Map<String, Any?>? = null
        try {

            val jsonString = gson.toJson(obj)
            map = gson.fromJson<Map<String, Any?>>(
                jsonString,
                Map::class.java
            )
        } catch (e: Exception) {
            map = null
        }
        return map ?: mapOf()
    }
}