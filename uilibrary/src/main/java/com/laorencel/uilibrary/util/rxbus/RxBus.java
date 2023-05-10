package com.laorencel.uilibrary.util.rxbus;

import androidx.lifecycle.LifecycleOwner;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.Subject;

/**
 * RxBus 不用多说了吧
 *
 * RxBus.get().post(new RxEvent(0))
 */
public class RxBus {
    private RxBus() {
    }

    private static class Builder {
        private static final RxBus INSTANCE = new RxBus();
    }

    public static RxBus get() {
        return Builder.INSTANCE;
    }

    private final Subject<Object> subject;

    public void post(Object obj) {
        subject.onNext(obj);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return subject.ofType(eventType);
    }
}
