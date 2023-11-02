package com.laorencel.uilibrary.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.laorencel.uilibrary.bean.Pagination;
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

public abstract class BaseFragment<VDB extends ViewDataBinding, VM extends BaseViewModel> extends Fragment {

    //Rxjava 使用 CompositeDisposable 收集所有的 Disposable 句柄，而后在 onDestroy 中调用 clear 统一注销
    //在适当时机取消订阅、截断数据流，避免内存泄露。
    private CompositeDisposable compositeDisposable;
    protected VDB contentBinding;
    protected VM viewModel;
    private ProgressDialog progressDialog;

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

    /**
     * onActivityCreated() 方法现已弃用。
     * 与 Fragment 视图有关的代码应在 onViewCreated()（在 onActivityCreated() 之前调用）中执行，而其他初始化代码应在 onCreate() 中执行。
     * 如需专门在 Activity 的 onCreate() 完成时接收回调，应在 onAttach() 中的 Activity 的 Lifecycle 上注册 LifeCycleObserver，并在收到 onCreate() 回调后将其移除。
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (null == viewModel) {
            viewModel = createViewModel();
        }
    }

    protected VM createViewModel() {
        Class<VM> viewModelClass = ClassUtil.getViewModel(this);
        if (null != viewModelClass) {
            return new ViewModelProvider(this).get(viewModelClass);
        }
        return null;
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        //onAttach()始终在任何Lifecycle 状态更改之前调用，所以onAttach()在onCreate()之前调用。
//
//        //onActivityCreated弃用后的替代方案
//        //因为onActivityCreated()是宿主Activity的onCreate()之后立即调用，
//        // 所以可以在onAttach的时候，通过订阅Activity的lifecycle来获取Activity的onCreate()事件，记得要removeObserver。
//        //requireActivity() 返回的是宿主activity
//        requireActivity().getLifecycle().addObserver(new LifecycleEventObserver() {
//            @Override
//            public void onStateChanged(@NonNull @NotNull LifecycleOwner source, @NonNull @NotNull Lifecycle.Event event) {
//                if (event.getTargetState() == Lifecycle.State.CREATED) {
//                    //在这里任你飞翔
//                    onAttachActivityCreated();
//
//                    requireActivity().getLifecycle().removeObserver(this);  //这里是删除观察者
//                }
//            }
//        });
//    }

//    public void onAttachActivityCreated() {
//
//    }

    //    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }
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
            Toast.makeText(getContext(), getResources().getString(stringId), Toast.LENGTH_LONG).show();
        }
    }

    protected void showToast(View view, String content) {
        if (null != view && !EmptyUtil.isEmpty(content)) {
            Toast.makeText(getContext(), content, Toast.LENGTH_LONG).show();
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
                progressDialog = new ProgressDialog(getContext());
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
    public void onDestroy() {
        clearDisposable();
        destroyProgress();
        super.onDestroy();
    }
}
