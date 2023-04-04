package com.laorencel.uilibrary.ui;

import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BaseViewModel extends ViewModel {
    private CompositeDisposable compositeDisposable;

    protected void addDisposable(Observable observable, Consumer consumer, Consumer errorConsumer) {
        if (null == observable) return;
//        observable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Object o) {
//
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
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

    @Override
    protected void onCleared() {
        if (this.compositeDisposable != null && !compositeDisposable.isDisposed()) {
            this.compositeDisposable.clear();
            this.compositeDisposable = null;
        }
        super.onCleared();
    }
}
