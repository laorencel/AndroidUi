package com.laorencel.uilibrary.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.laorencel.uilibrary.R;
import com.laorencel.uilibrary.databinding.FragmentBaseCommonBinding;
import com.laorencel.uilibrary.widget.state.State;
import com.laorencel.uilibrary.widget.state.StateLayout;
import com.laorencel.uilibrary.widget.state.bean.StateItem;

public abstract class BaseCommonFragment<VDB extends ViewDataBinding, VM extends BaseViewModel>
        extends BaseFragment<VDB, VM> {

    protected FragmentBaseCommonBinding baseUiBinding;

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

    /**
     * 设置根组件是否setFitsSystemWindows为true，setFitsSystemWindows为true时，
     * 系统会为该View设置一个paddingTop，值为statusbar的高度。
     * 用户在布局文件中设置的padding会被忽略
     * 可参考 https://www.jianshu.com/p/5cc3bd23be7b
     *
     * @return 默认false
     */
    protected boolean rootFitsSystemWindows() {
        return false;
    }

    @Override
    public View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.createView(inflater, container, savedInstanceState);

        if (null == baseUiBinding) {
            baseUiBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_common, container, false);
            baseUiBinding.rlRoot.setFitsSystemWindows(rootFitsSystemWindows());
            if (layoutID() != -1) {
                contentBinding = DataBindingUtil.inflate(getLayoutInflater(), layoutID(), null, false);
                if (null != contentBinding && null != baseUiBinding) {
                    //这里的LayoutParams要看contentBinding是加载在哪个父组件里面，相应的获取RelativeLayout还是其他类型。
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    contentBinding.getRoot().setLayoutParams(params);
                    baseUiBinding.llContent.addView(contentBinding.getRoot());
                }
            }

            //页面头部（添加在appbarLayout內，toolbar下面，stateLayout上面）
            if (headerLayoutID() != -1) {
                headerBinding = DataBindingUtil.inflate(getLayoutInflater(), headerLayoutID(), null, false);
                if (null != headerBinding && null != baseUiBinding) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    headerBinding.getRoot().setLayoutParams(params);
                    baseUiBinding.topLayout.addView(headerBinding.getRoot());
                }
            }
            //页面底部（添加在bottomAppbar內，stateLayout下面，固定在底部）
            if (footerLayoutID() != -1) {
                footerBinding = DataBindingUtil.inflate(getLayoutInflater(), footerLayoutID(), null, false);
                if (null != footerBinding && null != baseUiBinding) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    footerBinding.getRoot().setLayoutParams(params);
                    baseUiBinding.bottomLayout.addView(footerBinding.getRoot());
                    baseUiBinding.bottomLayout.setVisibility(View.VISIBLE);
                }
            } else {
                baseUiBinding.bottomLayout.setVisibility(View.GONE);
            }

        } else {
            ViewGroup parent = (ViewGroup) baseUiBinding.getRoot().getParent();
            if (null != parent) {
                parent.removeView(baseUiBinding.getRoot());
            }
        }

        if (null != baseUiBinding) {

            baseUiBinding.stateLayout.setOnStateClickListener(new StateLayout.OnStateClickListener() {
                @Override
                public void onClick(View view, State state) {
                    onStateClick(view, state);
                }
            });
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
        if (null != baseUiBinding) {
            if (state == State.CONTENT) {
                if (null != contentBinding) {
                    contentBinding.getRoot().setVisibility(View.VISIBLE);
                }
                baseUiBinding.stateLayout.setVisibility(View.GONE);
            } else {
                if (null != contentBinding) {
                    contentBinding.getRoot().setVisibility(View.GONE);
                }
                baseUiBinding.stateLayout.setVisibility(View.VISIBLE);
                baseUiBinding.stateLayout.switchState(state, item);
            }
        }
    }

}
