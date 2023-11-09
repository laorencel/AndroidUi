package com.laorencel.uilibrary.ui.adapter.tree;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.laorencel.uilibrary.R;
import com.laorencel.uilibrary.databinding.ItemTreeBinding;

public abstract class TreeHolder<T extends TreeNodeImpl, VDB extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public ItemTreeBinding dataBinding;
    public VDB contentBinding;

    public TreeHolder(@NonNull ViewGroup parent, int layoutId) {
        super(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_tree, parent, false).getRoot());
        dataBinding = DataBindingUtil.getBinding(this.itemView);//itemView就是item的容器
        contentBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, null, false);
        if (null != contentBinding && null != dataBinding) {
            //这里的LayoutParams要看contentBinding是加载在哪个父组件里面，相应的获取RelativeLayout还是其他类型。
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            contentBinding.getRoot().setLayoutParams(params);
            dataBinding.llContent.addView(contentBinding.getRoot());
        }
    }

    public void onBindViewHolder(int position, TreeNode<T> data) {
        dataBinding.setData(data);
        onBind(position, data.getData());
    }

    public abstract void onBind(int position, T data);
}
