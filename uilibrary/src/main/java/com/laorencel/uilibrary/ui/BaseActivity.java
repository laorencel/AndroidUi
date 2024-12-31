package com.laorencel.uilibrary.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.laorencel.uilibrary.bean.Pagination;
import com.laorencel.uilibrary.ui.adapter.BaseAdapter;
import com.laorencel.uilibrary.util.ClassUtil;
import com.laorencel.uilibrary.util.EmptyUtil;
import com.laorencel.uilibrary.widget.state.State;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class BaseActivity<VDB extends ViewDataBinding, VM extends BaseViewModel> extends KtAppUiActivity {
    //Rxjava 使用 CompositeDisposable 收集所有的 Disposable 句柄，而后在 onDestroy 中调用 clear 统一注销
    //在适当时机取消订阅、截断数据流，避免内存泄露。
    private CompositeDisposable compositeDisposable;
    protected VDB contentBinding;
    protected VM viewModel;


    @Override
    protected void createView(@Nullable Bundle savedInstanceState) {
        //        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);//写法一
//        viewDataBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.activity_main,null,false);//写法二（主要用于Fragment和Adapter)
//        viewDataBinding = ActivityMainBinding.inflate(getLayoutInflater());//写法三
//        setContentView(viewDataBinding.getRoot());//写法二和写法三需要setContentView
        contentBinding = DataBindingUtil.setContentView(this, layoutID());
        contentBinding.getRoot().setFitsSystemWindows(rootFitsSystemWindows());
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

    protected void showSnackbar(View view, String content) {
        showSnackbar(content);
    }

    protected void showSnackbar(View view, int stringId) {
        showSnackbar(stringId);
    }

    protected void showToast(View view, int stringId) {
        showToast(stringId);
    }

    protected void showToast(View view, String content) {
        showToast(content);
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
        super.onDestroy();
    }
}
