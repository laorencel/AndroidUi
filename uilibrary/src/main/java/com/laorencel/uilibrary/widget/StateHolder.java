package com.laorencel.uilibrary.widget;

import android.view.View;

import androidx.databinding.ViewDataBinding;

import com.laorencel.uilibrary.widget.bean.StateItem;

import java.util.List;

public class StateHolder {
    private State state;
    // 持有状态组件（可能是多个组件，所以用list承载）
    private List<View> views;
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

    public List<View> getViews() {
        return views;
    }

    public void setViews(List<View> views) {
        this.views = views;
    }

    public StateItem getItem() {
        return item;
    }

    public void setItem(StateItem item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "StateHolder{" +
                "state=" + state +
                ", views=" + views +
                ", item=" + item +
                '}';
    }
}
