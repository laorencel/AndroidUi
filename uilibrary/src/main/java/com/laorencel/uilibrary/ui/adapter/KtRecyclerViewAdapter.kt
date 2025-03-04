package com.laorencel.uilibrary.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.min

class KtRecyclerViewViewHolder(var dataBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(
        dataBinding.root
    ) {
}

abstract class KtRecyclerViewAdapter<T>(var list: MutableList<T> = mutableListOf()) :
    RecyclerView.Adapter<KtRecyclerViewViewHolder>() {

    var onItemClickListener: OnItemClickListener<T>? = null

//    fun setOnItemClickListener(listener: OnItemClickListener<T>?) {
//        this.onItemClickListener = listener
//    }

    abstract fun layoutId(): Int

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KtRecyclerViewViewHolder {
        return KtRecyclerViewViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutId(),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0;
    }

    override fun onBindViewHolder(holder: KtRecyclerViewViewHolder, position: Int) {
//        val dataBinding = holder.dataBinding;

        if (null != onItemClickListener) {
            holder.itemView.tag = position
            holder.itemView.setOnClickListener { view ->
                if (null != onItemClickListener) {
                    val index = view.tag as Int
                    onItemClickListener!!.onClick(view, index, list[index])
                }
            }
        }
    }

    fun getListData(): List<T> {
        return list
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(list: List<T>?) {
        if (null != list) {
            this.list = list as MutableList<T>
        } else {
            this.list = mutableListOf()
        }
        notifyDataSetChanged()
    }

    /**
     * 插入数据 调用List.addAll方法
     * @param list 数据
     */
    fun addAll(list: List<T>?) {
        if (!list.isNullOrEmpty()) {
            this.list.addAll(list)
            notifyItemRangeInserted(this.list.size - list.size, list.size)
        }
    }

    /**
     * 插入数据
     * @param fromIndex 开始index
     * @param list 数据
     */
    fun addAll(fromIndex: Int, list: List<T>?) {
        if (!list.isNullOrEmpty()) {
            this.list.addAll(fromIndex, list)
            notifyItemRangeInserted(fromIndex, list.size)
        }
    }


    /**
     * 新增
     *
     * @param index
     * @param data
     */
    fun insert(index: Int, data: T) {
        list.add(index, data)
        notifyItemInserted(index)
        notifyItemRangeChanged(index, list.size + 1)
    }

    /**
     * 更新数据，T data数据从list中取，更新后调用该方法
     *
     * @param index
     * @param data
     */
    fun update(index: Int, data: T) {
        list[index] = data
        notifyItemChanged(index, data)
    }

    /**
     * 交换2条数据的位置
     *
     * @param fromIndex
     * @param toIndex
     */
    fun changeIndex(fromIndex: Int, toIndex: Int) {
        //注意位置的变换fromIndex和toIndex交换，如： 1 和 4 交换
        val removeTo: T = list.removeAt(toIndex)
        val removeFrom: T = list.removeAt(fromIndex)
        list.add(fromIndex, removeTo)
        list.add(toIndex, removeFrom)
        notifyItemMoved(toIndex, fromIndex)
        //受影响的item都刷新position
        notifyItemRangeChanged(
            min(toIndex.toDouble(), fromIndex.toDouble()).toInt(),
            (abs((toIndex - fromIndex).toDouble()) + 1).toInt()
        ) //受影响的item都刷新position
    }

    /**
     * 删除数据
     *
     * @param index
     */
    fun remove(index: Int) {
        list.removeAt(index)
        notifyItemRemoved(index)
        //受影响的item都刷新position
        notifyItemRangeChanged(index, list.size - 1)
    }

    interface OnItemClickListener<T> {
        fun onClick(view: View?, position: Int, data: T)
    }
}