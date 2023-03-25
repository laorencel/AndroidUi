package com.laorencel.uilibrary.widget.bean;

import androidx.databinding.ObservableField;

import com.laorencel.uilibrary.R;
import com.laorencel.uilibrary.widget.State;

public class StateItem {
    public ObservableField<String> content = new ObservableField<>();
    public ObservableField<String> tip = new ObservableField<>();

    public ObservableField<Integer> imageSrc = new ObservableField<>();
    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> button = new ObservableField<>();

    //从另外一个StateItem中获取数据
    public void from(StateItem item) {
        content.set(item.content.get());
        tip.set(item.tip.get());
        imageSrc.set(item.imageSrc.get());
        imageUrl.set(item.imageUrl.get());
        button.set(item.button.get());
    }

    public static StateItem create(State state) {
        StateItem item = new StateItem();
        switch (state) {
            case LOADING:
                item.content.set("加载中");
                item.imageSrc.set(R.drawable.ic_state_loading);
                break;
            case EMPTY:
                item.content.set("暂无数据");
                item.imageSrc.set(R.drawable.ic_state_empty);
                break;
            case FAILURE:
                item.content.set("加载失败");
                item.imageSrc.set(R.drawable.ic_state_failure);
                break;
            case NO_AUTH:
                item.content.set("未登录");
                item.imageSrc.set(R.drawable.ic_state_login);
                break;
            default:
                item.content.set("");
                item.imageSrc.set(R.drawable.ic_state_empty);
                break;
        }
        return item;
    }

    @Override
    public String toString() {
        return "StateItem{" +
                "content=" + content.get() +
                ", tip=" + tip.get() +
                ", imageSrc=" + imageSrc.get() +
                ", imageUrl=" + imageUrl.get() +
                ", button=" + button.get() +
                '}';
    }
}
