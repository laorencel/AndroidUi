package com.laorencel.ui.test.kt

import com.laorencel.uilibrary.ui.KtViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

class KtTestListVM : KtViewModel() {

    suspend fun getList(): List<Student> {
        delay(4 * 1000L)
        val list = mutableListOf<Student>();
        for (i in 1..40) {
            list.add(Student("name" + i, i, "className" + i))
        }
        return list
    }
}