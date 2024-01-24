package com.laorencel.uilibrary.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.laorencel.uilibrary.R;
import com.laorencel.uilibrary.databinding.ActivityBaseCommonBinding;
import com.laorencel.uilibrary.util.StatusBarUtil;
import com.laorencel.uilibrary.widget.state.State;
import com.laorencel.uilibrary.widget.state.StateLayout;
import com.laorencel.uilibrary.widget.state.bean.StateItem;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.Objects;

public abstract class BaseCommonActivity<VDB extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivity<VDB, VM> {
    protected ActivityBaseCommonBinding baseCommonBinding;

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
     * 页面底部布局资源layoutID（添加在bottomAppbar內，stateLayout下面，固定在底部）
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
     * @return 默认true，因为本UI框架已经设置透明状态栏及全屏
     */
    protected boolean rootFitsSystemWindows() {
        return false;
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
        baseCommonBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_common);
        baseCommonBinding.getRoot().setFitsSystemWindows(rootFitsSystemWindows());

        viewModel = createViewModel();

        if (layoutID() != -1) {
            contentBinding = DataBindingUtil.inflate(getLayoutInflater(), layoutID(), null, false);
            if (null != contentBinding && null != baseCommonBinding) {
                //这里的LayoutParams要看contentBinding是加载在哪个父组件里面，相应的获取RelativeLayout还是其他类型。
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                contentBinding.getRoot().setLayoutParams(params);
                baseCommonBinding.refreshLayout.addView(contentBinding.getRoot());
            }
        }

        //页面底部（添加在bottomAppbar內，stateLayout下面，固定在底部）
        if (footerLayoutID() != -1) {
            footerBinding = DataBindingUtil.inflate(getLayoutInflater(), footerLayoutID(), null, false);
            if (null != footerBinding && null != baseCommonBinding) {
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                footerBinding.getRoot().setLayoutParams(params);
                baseCommonBinding.llBottom.addView(footerBinding.getRoot());
                baseCommonBinding.llBottom.setVisibility(View.VISIBLE);


                if (null != contentBinding) {
                    int navigationBarHeight = StatusBarUtil.getNavigationBarHeight(this);
                    Log.e("llBottom.getHeight", navigationBarHeight + " hasNavigationBar");
//                    contentBinding.getRoot().setPadding(
//                            contentBinding.getRoot().getPaddingLeft(),
//                            contentBinding.getRoot().getPaddingTop(),
//                            contentBinding.getRoot().getPaddingRight(),
//                            contentBinding.getRoot().getPaddingBottom() + navigationBarHeight
//                    );
                    footerBinding.getRoot().setPadding(
                            footerBinding.getRoot().getPaddingLeft(),
                            footerBinding.getRoot().getPaddingTop(),
                            footerBinding.getRoot().getPaddingRight(),
                            footerBinding.getRoot().getPaddingBottom() + navigationBarHeight
                    );
                }
            }
        } else {
            int navigationBarHeight = StatusBarUtil.getNavigationBarHeight(this);
            baseCommonBinding.getRoot().setPadding(
                    baseCommonBinding.getRoot().getPaddingLeft(),
                    baseCommonBinding.getRoot().getPaddingTop(),
                    baseCommonBinding.getRoot().getPaddingRight(),
                    baseCommonBinding.getRoot().getPaddingBottom() + navigationBarHeight
            );
            baseCommonBinding.llBottom.setVisibility(View.GONE);
        }

        if (null != baseCommonBinding) {
            setToolbar(baseCommonBinding.toolbar);

            baseCommonBinding.refreshLayout.setEnableRefresh(refreshEnable());
            baseCommonBinding.refreshLayout.setEnableLoadMore(loadMoreEnable());
            baseCommonBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    BaseCommonActivity.this.onLoadMore(refreshLayout);
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    BaseCommonActivity.this.onRefresh(refreshLayout);
                }
            });

            baseCommonBinding.stateLayout.setOnStateClickListener(new StateLayout.OnStateClickListener() {
                @Override
                public void onClick(View view, State state) {
                    onStateClick(view, state);
                }
            });
        }
    }

    public void showToolbar(boolean show) {
        if (show) {
            Objects.requireNonNull(getSupportActionBar()).show();
        } else {
            Objects.requireNonNull(getSupportActionBar()).hide();
        }
//        baseCommonBinding.appbarLayout.setVisibility(show ? View.VISIBLE : View.GONE);
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

    /**
     * 状态页面切换
     *
     * @param state State状态
     * @param item  StateItem配置
     */
    public void switchState(State state, StateItem item) {
        if (null != baseCommonBinding) {
            if (state == State.CONTENT) {
                if (null != contentBinding) {
                    contentBinding.getRoot().setVisibility(View.VISIBLE);
                }
                baseCommonBinding.stateLayout.setVisibility(View.GONE);
            } else {
                if (null != contentBinding) {
                    contentBinding.getRoot().setVisibility(View.GONE);
                }
                baseCommonBinding.stateLayout.setVisibility(View.VISIBLE);
                baseCommonBinding.stateLayout.switchState(state, item);
            }
        }
    }
}
