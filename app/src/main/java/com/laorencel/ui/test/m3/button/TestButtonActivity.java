package com.laorencel.ui.test.m3.button;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.laorencel.ui.R;
import com.laorencel.ui.databinding.ActivityTestM3ButtonBinding;
import com.laorencel.ui.databinding.ActivityTestM3ButtonFooterBinding;
import com.laorencel.uilibrary.ui.BaseUiActivity;
import com.laorencel.uilibrary.widget.state.State;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TestButtonActivity extends BaseUiActivity<ActivityTestM3ButtonBinding, TestButtonVM> {
    @Override
    protected int layoutID() {
        return R.layout.activity_test_m3_button;
    }

    @Override
    protected int headerLayoutID() {
        return R.layout.activity_test_m3_button_header;
    }

    @Override
    protected int footerLayoutID() {
        return R.layout.activity_test_m3_button_footer;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((ActivityTestM3ButtonFooterBinding) footerBinding).setActivity(TestButtonActivity.this);
    }

    @Override
    public boolean refreshEnable() {
        return true;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
        addDisposable(Observable
                .timer(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long integer) throws Throwable {
                        Log.d("onRefresh", "integer:" + integer);
                        baseUiBinding.refreshLayout.finishRefresh();
                        switchState(State.CONTENT);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("onRefresh", "throwable:" + throwable);
                    }
                }));
    }

    @Override
    public void onStateClick(View view, State state) {
        super.onStateClick(view, state);
        Log.d("onStateClick", "state:" + state);
        baseUiBinding.refreshLayout.autoRefresh();
    }
}
