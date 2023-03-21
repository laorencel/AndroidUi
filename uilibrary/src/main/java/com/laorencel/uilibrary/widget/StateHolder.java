package com.laorencel.uilibrary.widget;

import android.view.View;

import androidx.databinding.ViewDataBinding;

import com.laorencel.uilibrary.widget.bean.StateItem;

public class StateHolder {
    private State state;
    //持有子状态组件
    private ViewDataBinding dataBinding;
    //有时并不是使用dataBinding，这时候可以使用view代替
    private View view;
    private StateItem item;

    public StateHolder() {
    }

    public StateHolder(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ViewDataBinding getDataBinding() {
        return dataBinding;
    }

    public void setDataBinding(ViewDataBinding dataBinding) {
        this.dataBinding = dataBinding;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public StateItem getItem() {
        return item;
    }

    public void setItem(StateItem item) {
        this.item = item;
    }
}
