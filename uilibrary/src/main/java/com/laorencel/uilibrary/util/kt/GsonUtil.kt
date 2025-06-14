package com.laorencel.uilibrary.util.kt

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.laorencel.uilibrary.util.kt.log.logE
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


object GsonUtil {
    val gson by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Gson()
    }

    fun toJson(obj: Any?): String? {
        return gson.toJson(obj)
    }

    /**
     * json字符串转为class对象
     * 例
     * val json = "{"checked":false,"tag":"tag1","title":"title1"}"
     * val titleConvert: FragTitle? = GsonUtil.fromJson(titleString, FragTitle::class.java)
     */
    fun <T> fromJson(json: String?, classType: Class<T>): T? {
        if (!isEmpty(json)) {
            try {
                val t = gson.fromJson(json, classType)
                return t
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                logE("GsonUtil fromJson JsonSyntaxException $e")
            }
        }
        return null
    }

    /**
     * json字符串（集合）转为集合
     * 例
     * val listString = "[{"checked":false,"tag":"tag2","title":"title2"},{"checked":false,"tag":"tag3","title":"title3"}]"
     * val listConvert:List<FragTitle>? = GsonUtil.fromJsonList(listString, FragTitle::class.java)
     */
    fun <T> fromJsonList(json: String?, classType: Class<T>): List<T>? {
        if (!isEmpty(json)) {
            try {
//                val listType = object : TypeToken<List<T>>() {
//                }.type
//                val t: List<T> = gson.fromJson<List<T>>(json, listType)
                val type = ParameterizedTypeImpl(classType)
                val t = gson.fromJson<List<T>>(json, type)
                return t
            } catch (e: JsonSyntaxException) {
                logE("GsonInstance fromJsonList JsonSyntaxException $e")
            }
        }
        return null
    }
}

private class ParameterizedTypeImpl(var clazz: Class<*>) : ParameterizedType {
    override fun getActualTypeArguments(): Array<Type> {
        //getActualTypeArguments 返回实际类型组成的数据，即new Type[]{String.class,Integer.class}
        return arrayOf(clazz)
    }

    override fun getRawType(): Type {
        //getRawType 返回原生类型，即 HashMap
        return MutableList::class.java
    }

    override fun getOwnerType(): Type? {
        //getOwnerType 返回 Type 对象，表示此类型是其成员之一的类型。
        // 例如，如果此类型为 O<T>.I<S>，则返回 O<T> 的表示形式。
        // 如果此类型为顶层类型，则返回 null。这里就直接返回null就行了
        return null
    }
}