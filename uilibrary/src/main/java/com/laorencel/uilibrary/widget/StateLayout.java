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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    }

    //返回当前状态
    public State getState() {
        return currentState;
    }

    public void switchState(State state) {
        switchState(state, null);
    }

    //设置content视图
    private void setContent() {
        StateHolder holder = stateMap.get(State.CONTENT);
        if (null == holder) {
            holder = new StateHolder(State.CONTENT);
            stateMap.put(State.CONTENT, holder);
        }
        if (null == holder.getViews() || holder.getViews().size() == 0) {
            List<View> views = new ArrayList<>();
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                if (null == view.getTag()) {
                    //默认没有tag的都是content视图
                    view.setTag(State.CONTENT);
                    views.add(view);
                }
            }
            holder.setViews(views);
        }
    }

    //切换状态
    public void switchState(State state, StateItem stateItem) {
        if (currentState == state) {
            return;
        }
        currentState = state;
        setContent();
        StateHolder holder = stateMap.get(state);
        if (null == holder) {
            holder = new StateHolder(state);
            stateMap.put(state, holder);
        }
        if (state != State.CONTENT) {
            // CONTENT视图不需要
            if (null == holder.getItem()) {
                holder.setItem(StateItem.create(state));
            }
            if (null != stateItem) {
                //刷新界面绑定
                holder.getItem().from(stateItem);
            }
        }

        if (null == holder.getViews() || holder.getViews().size() == 0) {
            LayoutStateBinding binding = LayoutStateBinding.inflate(inflater);
            binding.setItem(holder.getItem());
            binding.getRoot().setTag(state);//为状态视图添加相应状态的tag
            List<View> views = new ArrayList<>();
            views.add(binding.getRoot());
            holder.setViews(views);
            //添加为子状态组件
            addView(binding.getRoot());
        }

        //TODO 状态切换动画
        if (null != holder.getViews() && holder.getViews().size() > 0) {
            for (int i = 0; i < holder.getViews().size(); i++) {
                holder.getViews().get(i).setVisibility(View.VISIBLE);
            }
        }
        for (Map.Entry<State, StateHolder> entry : stateMap.entrySet()) {
            if (entry.getKey() != state) {
                StateHolder otherHolder = entry.getValue();
                if (null != otherHolder.getViews() && otherHolder.getViews().size() > 0) {
                    for (int i = 0; i < otherHolder.getViews().size(); i++) {
                        otherHolder.getViews().get(i).setVisibility(View.GONE);
                    }
                }
            }
        }
    }
}
