package com.laorencel.uilibrary.ui.adapter.tree;

import androidx.databinding.ObservableField;

import java.util.ArrayList;
import java.util.List;

/**
 * 树形节点包装类
 *
 * @param <T> 范型
 */
public class TreeNode<T extends TreeNodeImpl> implements TreeNodeImpl {
    private T data;
    // 是否展开
    private ObservableField<Boolean> isExpand;
    // 是否选中
    private ObservableField<Boolean> isChecked;
    // 父节点
    private TreeNode<T> parent;
    // 子结点
    private List<TreeNode<T>> children = new ArrayList<>();

    public TreeNode(T data) {
        this.data = data;
    }

    @Override
    public String getNodeId() {
        if (null != data) {
            return data.getNodeId();
        }
        return null;
    }

    @Override
    public String getNodeParentId() {
        if (null != data) {
            return data.getNodeParentId();
        }
        return null;
    }

    @Override
    public List getOriginalChildren() {
        if (null != data) {
            return data.getOriginalChildren();
        }
        return null;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isExpand() {
        if (null == this.isExpand) {
            this.isExpand = new ObservableField<>(false);
        }
        return isExpand.get();
    }

    public void setExpand(boolean expand) {
        if (null == this.isExpand) {
            this.isExpand = new ObservableField<>(expand);
        } else {
            isExpand.set(expand);
        }
        if (!isExpand.get() && null != children) {
            for (TreeNode node : children) {
                node.setExpand(false);
            }
        }
    }

    public boolean isChecked() {
        if (null == this.isChecked) {
            this.isChecked = new ObservableField<>(false);
        }
        return isChecked.get();
    }

    public void setChecked(boolean checked) {
        if (null == this.isChecked) {
            this.isChecked = new ObservableField<>(checked);
        } else {
            isChecked.set(checked);
        }

        if (null != children) {
            for (TreeNode node : children) {
                node.setChecked(checked);
            }
        }
    }

    public int getLevel() {
        //获取当前节点的层级
        return parent == null ? 0 : parent.getLevel() + 1;
    }

//    public void setLevel(int level) {
//        this.level = level;
//    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode<T>> children) {
        this.children = children;
    }

    public boolean isRoot() {
        //是否为根节点
        return null == parent || null == parent.getData();
    }

    public boolean isLeaf() {
        //是否是叶子结点，没有子结点，就证明是叶子结点
        return null == children || children.size() == 0;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "data=" + data +
                ", isExpand=" + isExpand +
                ", isChecked=" + isChecked +
                ", parent=" + parent +
                ", children=" + children +
                '}';
    }


}
