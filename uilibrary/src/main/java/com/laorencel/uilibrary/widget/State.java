package com.laorencel.uilibrary.widget;

public enum State {

    //正常内容视图
    CONTENT(1),
    //加载中
    LOADING(2),
    //数据为空视图（一般用于列表界面）
    EMPTY(3),
    //数据加载失败视图（一般用于非列表界面）
    FAILURE(4),
    //未登录视图
    NO_AUTH(5);

    private int value;

    private State(int value) {
        this.value = value;
    }
}
