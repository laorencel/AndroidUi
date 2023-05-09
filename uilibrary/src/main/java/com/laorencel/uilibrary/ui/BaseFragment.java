package com.laorencel.uilibrary.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.laorencel.uilibrary.util.ClassUtil;
import com.laorencel.uilibrary.widget.State;
import com.laorencel.uilibrary.widget.bean.StateItem;

import org.jetbrains.annotations.NotNull;

public abstract class BaseFragment<VDB extends ViewDataBinding, VM extends BaseViewModel> extends Fragment {

    protected VDB contentBinding;
    protected VM viewModel;

    protected abstract int layoutID();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return createView(inflater, container, savedInstanceState);
    }

    public View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == contentBinding) {
            contentBinding = DataBindingUtil.inflate(inflater, layoutID(), container, false);
        } else {
            ViewGroup parent = (ViewGroup) contentBinding.getRoot().getParent();
            if (null != parent) {
                parent.removeView(contentBinding.getRoot());
            }
        }
        return contentBinding.getRoot();
    }

    protected VM createViewModel() {
        Class<VM> viewModelClass = ClassUtil.getViewModel(this);
        if (null != viewModelClass) {
            return new ViewModelProvider(this).get(viewModelClass);
        }
        return null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //onAttach()始终在任何Lifecycle 状态更改之前调用，所以onAttach()在onCreate()之前调用。

        //onActivityCreated弃用后的替代方案
        //因为onActivityCreated()是宿主Activity的onCreate()之后立即调用，
        // 所以可以在onAttach的时候，通过订阅Activity的lifecycle来获取Activity的onCreate()事件，记得要removeObserver。
        //requireActivity() 返回的是宿主activity
        requireActivity().getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull @NotNull LifecycleOwner source, @NonNull @NotNull Lifecycle.Event event) {
                if (event.getTargetState() == Lifecycle.State.CREATED) {
                    //在这里任你飞翔
                    onAttachActivityCreated();

                    requireActivity().getLifecycle().removeObserver(this);  //这里是删除观察者
                }
            }
        });
    }

    public void onAttachActivityCreated(){
        if (null == viewModel) {
            viewModel = createViewModel();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * 状态页面切换
     *
     * @param state State状态
     */
    public void switchState(State state) {
        switchState(state, null);
    }

    /**
     * 状态页面切换
     *
     * @param state State状态
     * @param item  StateItem配置
     */
    public void switchState(State state, StateItem item) {
//        if (null != baseUiBinding)
//            baseUiBinding.stateLayout.switchState(state, item);
    }
}
