package com.xinkao.skmvp.network.simpleNet;

import androidx.lifecycle.LifecycleOwner;
import io.reactivex.Observable;

public final class SkLife {
    public static <T> SkSimpleConverter<T> asSimple(LifecycleOwner owner) {
        return new SkSimpleConverter<T>() {
            @Override
            public SkObservableSet<T> apply(Observable<T> upstream) {
                return new SkObservableSet<T>(upstream, owner);
            }
        };
    }
}
