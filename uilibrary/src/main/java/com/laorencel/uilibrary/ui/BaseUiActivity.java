package com.laorencel.uilibrary.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.laorencel.uilibrary.R;
import com.laorencel.uilibrary.databinding.ActivityBaseUiBinding;
import com.laorencel.uilibrary.widget.state.State;
import com.laorencel.uilibrary.widget.state.StateLayout;
import com.laorencel.uilibrary.widget.state.bean.StateItem;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.Objects;

public abstract class BaseUiActivity<VDB extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivity<VDB, VM> {

    protected ActivityBaseUiBinding baseUiBinding;

    //页面头部（添加在appbarLayout內，toolbar下面，stateLayout上面）
    protected ViewDataBinding headerBinding;

    //页面底部（添加在bottomAppbar內，stateLayout下面，固定在底部）
    protected ViewDataBinding footerBinding;

    /**
     * 子页面必须实现，页面布局id，返回-1不加载
     *
     * @return 布局id
     */
    @Override
    protected abstract int layoutID();

    /**
     * 页面头部布局资源layoutID（添加在appbarLayout內，toolbar下面，stateLayout上面）
     */
    protected int headerLayoutID() {
        return -1;
    }

    /**
     * 页面底部布局资源layoutID（添加在bottomAppbar內，stateLayout下面，固定在底部）
     */
    protected int footerLayoutID() {
        return -1;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void createView(@Nullable Bundle savedInstanceState) {
        //        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);//写法一
//        viewDataBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.activity_main,null,false);//写法二（主要用于Fragment和Adapter)
//        viewDataBinding = ActivityMainBinding.inflate(getLayoutInflater());//写法三
//        setContentView(viewDataBinding.getRoot());//写法二和写法三需要setContentView
        baseUiBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_ui);
        viewModel = createViewModel();

        if (layoutID() != -1) {
            contentBinding = DataBindingUtil.inflate(getLayoutInflater(), layoutID(), null, false);
            if (null != contentBinding && null != baseUiBinding) {
                //这里的LayoutParams要看contentBinding是加载在哪个父组件里面，相应的获取RelativeLayout还是其他类型。
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                contentBinding.getRoot().setLayoutParams(params);
                baseUiBinding.refreshLayout.addView(contentBinding.getRoot());
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

        if (null != baseUiBinding) {
            setToolbar(baseUiBinding.toolbar);

            baseUiBinding.refreshLayout.setEnableRefresh(refreshEnable());
            baseUiBinding.refreshLayout.setEnableLoadMore(loadMoreEnable());
            baseUiBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    BaseUiActivity.this.onLoadMore(refreshLayout);
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    BaseUiActivity.this.onRefresh(refreshLayout);
                }
            });

            baseUiBinding.stateLayout.setOnStateClickListener(new StateLayout.OnStateClickListener() {
                @Override
                public void onClick(View view, State state) {
                    onStateClick(view, state);
                }
            });
        }
    }

    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
    }

    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
    }

    public boolean refreshEnable() {
        return false;
    }

    public boolean loadMoreEnable() {
        return false;
    }

    public void showToolbar(boolean show) {
        if (show) {
            Objects.requireNonNull(getSupportActionBar()).show();
        } else {
            Objects.requireNonNull(getSupportActionBar()).hide();
        }
//        baseUiBinding.appbarLayout.setVisibility(show ? View.VISIBLE : View.GONE);
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
