package com.laorencel.uilibrary.ui.adapter.tree;

import java.util.List;

/**
 * 树形节点实现接口
 */
public interface TreeNodeImpl {

    String getNodeId();

    String getNodeParentId();

    /**
     * 原始数据可能已经是树形结构了，用该方法获取原始数据的children
     *
     * @return List or null
     */
    List getOriginalChildren();
}
