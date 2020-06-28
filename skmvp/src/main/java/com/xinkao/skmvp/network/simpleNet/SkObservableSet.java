package com.xinkao.skmvp.network.simpleNet;

import com.rxjava.rxlife.RxLife;

import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SkObservableSet<T> {
    private Observable<T> upStream;
    private LifecycleOwner owner;

    public SkObservableSet(Observable<T> upStream, LifecycleOwner owner) {
        this.upStream = upStream;
        this.owner = owner;
    }

    public final void subscribe(Observer<? super T> observer) {
        upStream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.asOnMain(owner))
                .subscribe(observer);
    }

    public final void subscribe(SkObservable<T> skObservable) {
        subscribe(null, skObservable);
    }

    public final void subscribe(List<Disposable> bind, SkObservable<T> skObservable) {
        upStream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.asOnMain(owner))
                .subscribe(new Observer<T>() {
                    private Disposable d;
                    @Override
                    public void onSubscribe(Disposable d) {
                        this.d = d;
                        skObservable.start(d);

                        if (bind != null) {
                            try {
                                bind.add(d);
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new RuntimeException("请检查List是否可执行.add方法");
                            }
                        }
                    }

                    @Override
                    public void onNext(T t) {
                        skObservable.onSuccess(t);
                        d.dispose();
                        if (bind != null) bind.remove(d);
                        skObservable.finish();
                    }

                    @Override
                    public void onError(Throwable t) {
                        skObservable.onError(t);
                        d.dispose();
                        if (bind != null) bind.remove(d);
                        skObservable.finish();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
