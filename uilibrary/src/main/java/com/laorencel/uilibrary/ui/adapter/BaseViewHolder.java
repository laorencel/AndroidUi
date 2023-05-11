package com.laorencel.uilibrary.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


/**
 * RecyclerView.ViewHolder基础封装
 *
 * @param <T>
 * @param <VDB>
 */
public abstract class BaseViewHolder<T, VDB extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public VDB dataBinding;

    public BaseViewHolder(@NonNull ViewGroup parent, int layoutId) {
        super(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false).getRoot());
        dataBinding = DataBindingUtil.getBinding(this.itemView);//itemView就是item的容器
    }

    /**
     * RecyclerView.Adapter的onBindViewHolder方法中调用
     *
     * @param position
     * @param data
     */
    public abstract void onBind(int position, T data);
}
