package com.laorencel.uilibrary.ui.adapter.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TreeUtil {

//    public static <T extends TreeNodeImpl> void add(List<TreeNode<T>> nodes,T data) {
//        TreeNode node = new TreeNode(data);
//        String nodeParentId = node.getNodeParentId();
//        Iterator<TreeNode<T>> iterator = nodes.iterator();
//        for (int i = 0; i < nodes.size(); i++) {
//            if (nodes.get(i).getNodeId()==nodeParentId){
//                node.setParent(nodes.get(i));
//                if (nodes.get(i).isChecked()){
//                    node.setChecked(true);
//                }
//                nodes.get(i).getChildren().add(node);
//            } else if (!nodes.get(i).isLeaf()){
//                add(nodes,data);
//            }
//        }
//    }

//    public static void convertTreeNodeList(List<TreeNode> source, List<Department> list) {
////        List<TreeNode<Department>> nodes = new ArrayList<>();
//        for (Department department : list) {
//            TreeNode<Department> treeNode = new TreeNode<>(department);
//            source.add(treeNode);
//            if (null != department.getChildren() && department.getChildren().size() > 0) {
//                convertTreeNodeList(source, department.getChildren());
//            }
//        }
////        return nodes;
//    }

    //将接口请求的列表转为符合树形结构的列表
    public static <T extends TreeNodeImpl> void convertTreeNodeList(List<T> list, List<T> source) {
        if (null != source && source.size() > 0) {
            for (int i = 0; i < source.size(); i++) {
                list.add(source.get(i));
                if (null != source.get(i).getOriginalChildren() && source.get(i).getOriginalChildren().size() > 0) {
                    //如果有子分组，将子分组取出，
                    convertTreeNodeList(list, source.get(i).getOriginalChildren());
                }
            }
        }
    }

    //treeAdapter中将列表转为adapter可用的list结构
    public static <T extends TreeNodeImpl> List<TreeNode<T>> convertList(List<T> list) {
        List<TreeNode<T>> nodes = new ArrayList<>();
        Map<String, TreeNode<T>> map = new HashMap<>();

        for (TreeNodeImpl nodeImpl : list) {
            TreeNode treeNode = new TreeNode(nodeImpl);
            nodes.add(treeNode);
            map.put(nodeImpl.getNodeId(), treeNode);
        }

        Iterator<TreeNode<T>> iterator = nodes.iterator();
        while (iterator.hasNext()) {
            TreeNode treeNode = iterator.next();
            String parentId = treeNode.getNodeParentId();
            if (null != parentId) {
                TreeNode parentNode = map.get(parentId);
                if (parentNode != null) {
                    parentNode.getChildren().add(treeNode);
                    treeNode.setParent(parentNode);
                    //如果当前item有父节点，在list中将当前item删除，即不显示，当点击父节点展开时再显示
                    iterator.remove();
                }
            }
        }
        return nodes;
    }

    public static <T extends TreeNodeImpl> List<TreeNode<T>> getNodeChildren(TreeNode<T> node) {
        List<TreeNode<T>> result = new ArrayList<>();
        getRNodeChildren(result, node);
        return result;
    }

    private static <T extends TreeNodeImpl> void getRNodeChildren(List<TreeNode<T>> result, TreeNode<T> node) {
        List<TreeNode<T>> children = node.getChildren();
        for (TreeNode child : children) {
            result.add(child);
            if (child.isExpand() && !child.isLeaf()) {
                getRNodeChildren(result, child);
            }
        }
    }

}
