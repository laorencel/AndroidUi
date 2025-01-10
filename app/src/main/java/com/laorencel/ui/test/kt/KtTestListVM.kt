package com.laorencel.ui.test.kt

import com.laorencel.uilibrary.ui.KtViewModel
import com.laorencel.uilibrary.util.kt.MapUtil
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

class KtTestListVM : KtViewModel() {

    suspend fun getList(): List<Student> {
        delay(4 * 1000L)
        val list = mutableListOf<Student>();
        for (i in 1..20) {
            val student = Student("name" + i, i, "className" + i)
            if (i == 2) {
                val map = MapUtil.toMap(student)
                println("map:$map ${map["name"]} ${map["age"]}")
            }
            list.add(student)
        }
        return list
    }
}