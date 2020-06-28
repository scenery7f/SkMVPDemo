package com.xinkao.skmvp.network.simpleNet;

import io.reactivex.disposables.Disposable;

public interface SkObservable<T> {

    void start(Disposable d);
    void onSuccess(T t);
    void onError(Throwable t);
    void finish();
}
