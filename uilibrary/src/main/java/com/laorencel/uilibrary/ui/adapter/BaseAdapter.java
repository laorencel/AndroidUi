package com.laorencel.uilibrary.ui.adapter;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView.Adapter基础封装
 *
 * @param <T>
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private List<T> list = new ArrayList<>();
    private OnItemClickListener<T> itemClickListener;

    public void setItemClickListener(OnItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position, list.get(position));
        if (null != itemClickListener) {
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != itemClickListener) {
                        int index = (Integer) view.getTag();
                        itemClickListener.onClick(view, index, list.get(index));
                    }
                }
            });
        }
    }

    public List<T> getList() {
        return list;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<T> list) {
        if (null != list) {
            this.list = list;
        } else {
            this.list = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    /**
     * 插入数据 调用List.addAll方法
     * @param list 数据
     */
    public void addAll(List<T> list) {
        if (null != list && list.size() > 0) {
            this.list.addAll(list);
            notifyItemRangeInserted(this.list.size() - list.size(), list.size());
        }
    }

    /**
     * 插入数据
     * @param fromIndex 开始index
     * @param list 数据
     */
    public void addAll(int fromIndex, List<T> list) {
        if (null != list && list.size() > 0) {
            this.list.addAll(list);
            notifyItemRangeInserted(fromIndex, list.size());
        }
    }


    /**
     * 新增
     *
     * @param index
     * @param data
     */
    public void insert(int index, T data) {
        list.add(index, data);
        notifyItemInserted(index);
        notifyItemRangeChanged(index, list.size() + 1);
    }

    /**
     * 更新数据，T data数据从list中取，更新后调用该方法
     *
     * @param index
     * @param data
     */
    public void update(int index, T data) {
//      data =  list.get(index);
//        data.setImage(refreshImage);
        notifyItemChanged(index, data);
    }

    /**
     * 交换2条数据的位置
     *
     * @param fromIndex
     * @param toIndex
     */
    public void changeIndex(int fromIndex, int toIndex) {
        //注意位置的变换fromIndex和toIndex交换，如： 1 和 4 交换
        T removeTo = list.remove(toIndex);
        T removeFrom = list.remove(fromIndex);
        list.add(fromIndex, removeTo);
        list.add(toIndex, removeFrom);
        notifyItemMoved(toIndex, fromIndex);
        //受影响的item都刷新position
        notifyItemRangeChanged(Math.min(toIndex, fromIndex), Math.abs(toIndex - fromIndex) + 1);//受影响的item都刷新position
    }

    /**
     * 删除数据
     *
     * @param index
     */
    public void remove(int index) {
        notifyItemRemoved(index);
        list.remove(index);
        //受影响的item都刷新position
        notifyItemRangeChanged(index, list.size() - 1);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener<T> {
        void onClick(View view, int position, T data);
    }
}
