package com.laorencel.ui.test.kt

import com.laorencel.ui.R
import com.laorencel.ui.databinding.ItemStudentBinding
import com.laorencel.uilibrary.ui.adapter.KtRecyclerViewAdapter
import com.laorencel.uilibrary.ui.adapter.KtRecyclerViewViewHolder

class KtTestListAdapter : KtRecyclerViewAdapter<Student>() {
    override fun layoutId(): Int {
        return R.layout.item_student
    }

    override fun onBindViewHolder(holder: KtRecyclerViewViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val dataBinding = holder.dataBinding as ItemStudentBinding
        dataBinding.student = list[position]
    }
}