package com.laorencel.uilibrary.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.laorencel.uilibrary.bean.Pagination;
import com.laorencel.uilibrary.manager.UiWindowManager;
import com.laorencel.uilibrary.ui.adapter.BaseAdapter;
import com.laorencel.uilibrary.util.ClassUtil;
import com.laorencel.uilibrary.util.EmptyUtil;
import com.laorencel.uilibrary.widget.state.State;
import com.laorencel.uilibrary.widget.state.bean.StateItem;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class BaseActivity<VDB extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {
    //Rxjava 使用 CompositeDisposable 收集所有的 Disposable 句柄，而后在 onDestroy 中调用 clear 统一注销
    //在适当时机取消订阅、截断数据流，避免内存泄露。
    private CompositeDisposable compositeDisposable;
    protected VDB contentBinding;
    protected VM viewModel;
    private ProgressDialog progressDialog;

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
        uiWindowManager.applyEdgeToEdge(getWindow(), isEdgeToEdgeEnabled());
        uiWindowManager.setDefaultNightMode(defaultNightMode());

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

    /**
     * 是否设置EdgeToEdge
     *
     * @return true or false
     */
    protected boolean isEdgeToEdgeEnabled() {
        return true;
    }

    /**
     * 设置夜间模式
     *
     * @return 默认跟随系统 AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
     */
    protected int defaultNightMode() {
        return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
    }

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

    protected void showSnackbar(View view, String content) {
        if (null != view && !EmptyUtil.isEmpty(content)) {
            Snackbar.make(view, content, Snackbar.LENGTH_LONG).show();
        }
    }

    protected void showSnackbar(View view, int stringId) {
        if (null != view && !EmptyUtil.isEmpty(stringId)) {
            Snackbar.make(view, getResources().getString(stringId), Snackbar.LENGTH_LONG).show();
        }
    }

    protected void showToast(View view, int stringId) {
        if (null != view && !EmptyUtil.isEmpty(stringId)) {
            Toast.makeText(this, getResources().getString(stringId), Toast.LENGTH_LONG).show();
        }
    }

    protected void showToast(View view, String content) {
        if (null != view && !EmptyUtil.isEmpty(content)) {
            Toast.makeText(this, content, Toast.LENGTH_LONG).show();
        }
    }

    //上次加载弹窗message内容
    private String lastProgressMessage;

    protected void showProgress(boolean show) {
        showProgress(show, "", true);
    }

    protected void showProgress(boolean show, String message, boolean cancelable) {
        if (show) {
            if (!EmptyUtil.isEmpty(lastProgressMessage) && !lastProgressMessage.equals(message)) {
                //2次弹窗message不一样，销毁重新创建
                destroyProgress();
            }
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setCancelable(cancelable);//设置是否可以通过点击Back键取消
                progressDialog.setCanceledOnTouchOutside(cancelable);//设置在点击Dialog外是否取消Dialog进度条
                progressDialog.setMessage(!EmptyUtil.isEmpty(message) ? message : "加载中");
            }
            //            Logger.d("progressDialog.isShowing()" + (progressDialog.isShowing()));
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    protected void destroyProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = null;
        }
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

    /**
     * 状态页面点击
     *
     * @param view  点击的View
     * @param state {@link State}
     */
    public void onStateClick(View view, State state) {

    }

    /**
     * 通用设置列表数据，根据当前页数，数据是否为空，切换状态页面，如果不是第一页且数据为空，会将页数减一
     *
     * @param list       数据
     * @param adapter    BaseAdapter
     * @param pagination Pagination
     * @param <T>        数据类型
     */
    protected <T> void setListData(List<T> list, BaseAdapter<T> adapter, Pagination pagination) {
        setListData(list, adapter, pagination, true);
    }

    protected <T> void setListData(List<T> list, BaseAdapter<T> adapter, Pagination pagination, boolean switchState) {
        if (switchState && EmptyUtil.isEmpty(list)) {
            if (pagination.isStartPage()) {
                switchState(State.EMPTY);
            } else {
                switchState(State.CONTENT);
                pagination.minusPage();
            }
            return;
        }
        switchState(State.CONTENT);
        if (pagination.isStartPage()) {
            adapter.setList(list);
        } else {
            adapter.addAll(list);
        }
    }

    protected void addDisposable(Observable observable, Consumer consumer, Consumer errorConsumer) {
        if (null == observable) return;
        Disposable disposable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, errorConsumer);
        addDisposable(disposable);

    }

    public void addDisposable(Disposable disposable) {
        if (null == disposable) {
            return;
        }
        if (null == compositeDisposable) {
            compositeDisposable = new CompositeDisposable();
        }
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.add(disposable);
        }
    }

    public void clearDisposable() {
        if (this.compositeDisposable != null && !compositeDisposable.isDisposed()) {
            this.compositeDisposable.clear();
            this.compositeDisposable = null;
        }
    }

    @Override
    protected void onDestroy() {
        clearDisposable();
        destroyProgress();
        super.onDestroy();
    }
}
