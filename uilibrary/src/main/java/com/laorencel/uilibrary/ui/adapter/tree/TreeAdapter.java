package com.laorencel.uilibrary.ui.adapter.tree;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class TreeAdapter<T extends TreeNodeImpl> extends RecyclerView.Adapter<TreeHolder> {
    //0:不选；1：单选；2：多选
    public static final int CHECK_MODE_NONE = 0;
    public static final int CHECK_MODE_RADIO = 1;
    public static final int CHECK_MODE_MULTI = 2;

    private List<TreeNode<T>> list = new ArrayList<>();
    private int checkMode = 0;
    private OnItemChangeListener<T> itemChangeListener;

    public TreeAdapter() {
    }

    public TreeAdapter(int checkMode) {
        this.checkMode = checkMode;
    }

    public TreeAdapter(List<T> list) {
        setList(list);
    }

    public TreeAdapter(List<T> list, int checkMode) {
        this.checkMode = checkMode;
        setList(list);
    }

    public void setCheckMode(int checkMode) {
        this.checkMode = checkMode;
    }

    public int getCheckMode() {
        return checkMode;
    }

    public void setItemChangeListener(OnItemChangeListener<T> itemChangeListener) {
        this.itemChangeListener = itemChangeListener;
    }

    private int getTreeNodeMargin() {
        return 50;
    }

    @Override
    public void onBindViewHolder(@NonNull TreeHolder holder, int position) {
        holder.onBindViewHolder(position, list.get(position));

        int level = list.get(position).getLevel();
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        layoutParams.leftMargin = getTreeNodeMargin() * level;

        holder.dataBinding.cbCheck.setVisibility(checkMode == 0 ? View.GONE : View.VISIBLE);

        holder.dataBinding.cbCheck.setTag(position);
        holder.dataBinding.llContent.setTag(position);
        holder.dataBinding.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag();
                TreeNode<T> node = list.get(index);
                if (!node.isLeaf()) {
                    List<TreeNode<T>> children = TreeUtil.getNodeChildren(node);
                    if (node.isExpand()) {
                        list.removeAll(children);
                        node.setExpand(false);
                        notifyItemChanged(index);
                        notifyItemRangeRemoved(index + 1, children.size());
                        if (null != itemChangeListener) {
                            itemChangeListener.onExpandChange(v, index, node.isExpand(), node);
                        }
                    } else {
                        list.addAll(index + 1, children);
                        node.setExpand(true);
                        notifyItemChanged(index);
                        notifyItemRangeInserted(index + 1, children.size());
                        if (null != itemChangeListener) {
                            itemChangeListener.onExpandChange(v, index, node.isExpand(), node);
                        }
                    }
                } else {
                    node.setExpand(!node.isExpand());
                    notifyItemChanged(index);
                    if (null != itemChangeListener) {
                        itemChangeListener.onExpandChange(v, index, node.isExpand(), node);
                    }
                }
            }
        });
        holder.dataBinding.cbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMode != CHECK_MODE_NONE) {
                    int index = (int) v.getTag();
                    TreeNode<T> node = list.get(index);
                    int nodeChildrenSize = 0;
                    if (!node.isLeaf()) {
                        List<TreeNode<T>> nodeChildren = TreeUtil.getNodeChildren(node);
                        nodeChildrenSize = nodeChildren.size();
                        for (int i = 0; i < nodeChildren.size(); i++) {
                            nodeChildren.get(i).setChecked(node.isChecked());
                        }
                    }
                    //0:不选；1：单选；2：多选
                    if (checkMode == CHECK_MODE_RADIO) {
                        for (int i = 0; i < list.size(); i++) {
                            if (i < index || i > (index + nodeChildrenSize)) {
                                TreeNode<T> otherNode = list.get(i);
                                otherNode.setChecked(!node.isChecked());
//                                if (otherNode.getLevel() <= node.getLevel()) {
//                                    otherNode.setChecked(!node.isChecked());
//                                    if (!otherNode.isLeaf()) {
//                                        List<TreeNode<T>> otherNodeChildren = TreeUtil.getNodeChildren(otherNode);
//                                        for (int j = 0; j < otherNodeChildren.size(); j++) {
//                                            otherNodeChildren.get(j).setChecked(!node.isChecked());
//                                        }
//                                    }
//                                }
                            }
                        }
                    }

                    notifyDataSetChanged();
                    if (null != itemChangeListener) {
                        List<TreeNode<T>> checkedNodes = new ArrayList<>();
                        List<String> checkedIds = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).isChecked()) {
                                checkedNodes.add(list.get(i));
                                checkedIds.add(list.get(i).getNodeId());
                            }
                        }
                        itemChangeListener.onCheckChange(v, index, true, checkedNodes, checkedIds);
                    }
                }
            }
        });
    }

    private void setChecked(TreeNode<T> node, boolean checked) {
        if (node.isLeaf()) {
            node.setChecked(checked);
        } else {
            for (int i = 0; i < node.getChildren().size(); i++) {
                node.getChildren().get(i).setChecked(checked);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<TreeNode<T>> getList() {
        return list;
    }

    public void setList(List<T> list) {
        if (null != list) {
            this.list = TreeUtil.convertList(list);
        } else {
            this.list = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    /**
     * 交换2条数据的位置
     *
     * @param fromIndex
     * @param toIndex
     */
    public void changeIndex(int fromIndex, int toIndex) {
        //注意位置的变换fromIndex和toIndex交换，如： 1 和 4 交换
        TreeNode<T> removeTo = list.remove(toIndex);
        TreeNode<T> removeFrom = list.remove(fromIndex);
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
        list.remove(index);
        notifyItemRemoved(index);
        //受影响的item都刷新position
        notifyItemRangeChanged(index, list.size() - 1);
    }

    public interface OnItemChangeListener<T extends TreeNodeImpl> {
        void onExpandChange(View view, int position, boolean expand, TreeNode<T> data);

        void onCheckChange(View view, int position, boolean checked, List<TreeNode<T>> checkedNodes, List<String> checkedIds);
    }
}
