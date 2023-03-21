package com.laorencel.uilibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.laorencel.uilibrary.databinding.LayoutStateBinding;
import com.laorencel.uilibrary.widget.bean.StateItem;

import java.util.HashMap;
import java.util.Map;

public class StateLayout extends FrameLayout {

    private State currentState = State.CONTENT;

    //使用map保存子状态组件
    private final HashMap<State, StateHolder> stateMap = new HashMap<>();

    public StateLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private LayoutInflater inflater;

    private void init(Context context, AttributeSet attrs) {
//        LayoutHelper.parseAttr(context, attrs, this);

        inflater = LayoutInflater.from(context);

        //初始化时将第一个子组件作为内容状态组件
        View clild = getChildAt(0);
        StateHolder contentHolder = new StateHolder(State.CONTENT);
        contentHolder.setView(clild);
        stateMap.put(State.CONTENT, contentHolder);
    }

    public void switchState(State state) {
        switchState(state, null);
    }

    //切换状态
    public void switchState(State state, StateItem stateItem) {
        currentState = state;
//        getChildAt(0);
        StateHolder holder = stateMap.get(state);
        if (null == holder) {
            holder = new StateHolder(state);
            stateMap.put(state, holder);
        }
        if (null == holder.getItem()) {
            holder.setItem(StateItem.create(state));
        }
        if (null == holder.getDataBinding() && null == holder.getView()) {
            LayoutStateBinding binding = LayoutStateBinding.inflate(inflater);
            binding.setItem(holder.getItem());
            holder.setDataBinding(binding);
            //添加为子状态组件
            addView(binding.getRoot());
        }
        if (null != stateItem) {
            //刷新界面绑定
            holder.getItem().from(stateItem);
        }

        if (null != holder.getDataBinding()) {
            holder.getDataBinding().getRoot().setVisibility(View.VISIBLE);
        } else if (null != holder.getView()) {
            holder.getView().setVisibility(View.VISIBLE);
        }

        for (Map.Entry<State, StateHolder> entry : stateMap.entrySet()) {
            if (entry.getKey() != state) {
                StateHolder otherHolder = entry.getValue();
                if (null != otherHolder.getDataBinding()) {
                    otherHolder.getDataBinding().getRoot().setVisibility(View.GONE);
                } else if (null != otherHolder.getView()) {
                    otherHolder.getView().setVisibility(View.GONE);
                }
            }
        }
    }
}
