package com.laorencel.uilibrary.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.laorencel.uilibrary.R;
import com.laorencel.uilibrary.databinding.FragmentBaseUiBinding;
import com.laorencel.uilibrary.widget.State;
import com.laorencel.uilibrary.widget.bean.StateItem;

public abstract class BaseUiFragment<VDB extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment<VDB, VM> {
    FragmentBaseUiBinding baseUiBinding;

    //页面头部（添加在appbarLayout內，toolbar下面，stateLayout上面）
    protected ViewDataBinding headerBinding;

    //页面底部（添加在bottomAppbar內，stateLayout下面，固定在底部）
    protected ViewDataBinding footerBinding;

    protected abstract int layoutID();

    /**
     * 页面头部布局资源layoutID(添加在appbarLayout內，toolbar下面，stateLayout上面)
     */
    protected int headerLayoutID() {
        return -1;
    }

    /**
     * 页面底部布局资源layoutID(添加在bottomAppbar內，stateLayout下面，固定在底部)
     */
    protected int footerLayoutID() {
        return -1;
    }

    @Override
    public View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.createView(inflater, container, savedInstanceState);

        if (null == baseUiBinding) {
            baseUiBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_ui, container, false);
            if (layoutID() != -1) {
                contentBinding = DataBindingUtil.inflate(getLayoutInflater(), layoutID(), null, false);
                if (null != contentBinding && null != baseUiBinding) {
                    //这里的LayoutParams要看contentBinding是加载在哪个父组件里面，相应的获取RelativeLayout还是其他类型。
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    contentBinding.getRoot().setLayoutParams(params);
                    baseUiBinding.rlContent.addView(contentBinding.getRoot());
                }
            }

            //页面头部（添加在appbarLayout內，toolbar下面，stateLayout上面）
            if (headerLayoutID() != -1) {
                headerBinding = DataBindingUtil.inflate(getLayoutInflater(), headerLayoutID(), null, false);
                if (null != headerBinding && null != baseUiBinding) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    headerBinding.getRoot().setLayoutParams(params);
                    baseUiBinding.appbarLayout.addView(headerBinding.getRoot());
                }
            }
            //页面底部（添加在bottomAppbar內，stateLayout下面，固定在底部）
            if (footerLayoutID() != -1) {
                footerBinding = DataBindingUtil.inflate(getLayoutInflater(), footerLayoutID(), null, false);
                if (null != footerBinding && null != baseUiBinding) {
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    footerBinding.getRoot().setLayoutParams(params);
                    baseUiBinding.bottomAppbar.addView(footerBinding.getRoot());
                    baseUiBinding.bottomAppbar.setVisibility(View.VISIBLE);

                    if (null != contentBinding) {
                        contentBinding.getRoot().setPadding(
                                contentBinding.getRoot().getPaddingLeft(),
                                contentBinding.getRoot().getPaddingTop(),
                                contentBinding.getRoot().getPaddingRight(),
                                contentBinding.getRoot().getPaddingBottom() + baseUiBinding.bottomAppbar.getHeight()
                        );
                    }
                }
            } else {
                baseUiBinding.bottomAppbar.setVisibility(View.GONE);
            }

        } else {
            ViewGroup parent = (ViewGroup) baseUiBinding.getRoot().getParent();
            if (null != parent) {
                parent.removeView(baseUiBinding.getRoot());
            }
        }

        return baseUiBinding.getRoot();
    }

    /**
     * 状态页面切换
     *
     * @param state State状态
     * @param item  StateItem配置
     */
    public void switchState(State state, StateItem item) {
        if (null != baseUiBinding)
            baseUiBinding.stateLayout.switchState(state, item);
    }
}
