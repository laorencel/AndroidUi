package com.laorencel.uilibrary.ui;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.color.MaterialColors;
import com.laorencel.uilibrary.R;
import com.laorencel.uilibrary.manager.UiWindowManager;
import com.laorencel.uilibrary.util.ClassUtil;
import com.laorencel.uilibrary.util.EdgeToEdgeUtil;
import com.laorencel.uilibrary.widget.State;
import com.laorencel.uilibrary.widget.bean.StateItem;

public abstract class BaseActivity<VDB extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {

    protected VDB contentBinding;
    protected VM viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BaseActivity", "onCreate");

        // control the status bar content color
//        WindowInsetsControllerCompat windowInsetsController =
//                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
//        if (windowInsetsController != null) {
//            windowInsetsController.setAppearanceLightNavigationBars(true);
//        }

        UiWindowManager uiWindowManager = new UiWindowManager();
        uiWindowManager.applyEdgeToEdge(getWindow(), true);
        uiWindowManager.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        createView(savedInstanceState);
    }

    protected void createView(@Nullable Bundle savedInstanceState) {
        //        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);//写法一
//        viewDataBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.activity_main,null,false);//写法二（主要用于Fragment和Adapter)
//        viewDataBinding = ActivityMainBinding.inflate(getLayoutInflater());//写法三
//        setContentView(viewDataBinding.getRoot());//写法二和写法三需要setContentView
        contentBinding = DataBindingUtil.setContentView(this, layoutID());
        viewModel = createViewModel();
    }

    protected abstract int layoutID();

    protected VM createViewModel() {
        Class<VM> viewModelClass = ClassUtil.getViewModel(this);
        if (null != viewModelClass) {
            return new ViewModelProvider(this).get(viewModelClass);
        }
        return null;
    }

    protected void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });
//        toolbar.setNavigationIcon(R.drawable.ic_navigation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
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
