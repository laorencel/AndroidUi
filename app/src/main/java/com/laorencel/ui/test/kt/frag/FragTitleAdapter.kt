package com.laorencel.ui.test.kt.frag

import com.laorencel.ui.R
import com.laorencel.ui.databinding.ItemFragTitleBinding
import com.laorencel.uilibrary.ui.adapter.KtRecyclerViewAdapter
import com.laorencel.uilibrary.ui.adapter.KtRecyclerViewViewHolder

class FragTitleAdapter : KtRecyclerViewAdapter<FragTitle>() {
    override fun layoutId(): Int {
        return R.layout.item_frag_title
    }

    override fun onBindViewHolder(holder: KtRecyclerViewViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val binding = holder.dataBinding as ItemFragTitleBinding
        binding.data = list[position]
    }
}