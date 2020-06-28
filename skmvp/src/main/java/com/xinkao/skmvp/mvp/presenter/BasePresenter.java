package com.xinkao.skmvp.mvp.presenter;

import com.xinkao.skmvp.mvp.model.IModel;
import com.xinkao.skmvp.mvp.view.IView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.CallSuper;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import io.reactivex.disposables.Disposable;

public class BasePresenter<V extends IView, M extends IModel> implements LifecycleObserver, IPresenter {
    protected V mView;
    protected M mModel;

    public BasePresenter() {
        initPresenter();
    }

    public BasePresenter(V mView) {
        this.mView = mView;

        initPresenter();
    }

    public BasePresenter(V mView, M mModel) {
        this.mView = mView;
        this.mModel = mModel;

        initPresenter();
    }

    /**
     * 初始化
     */
    protected void initPresenter() {
        if (mView != null && mView instanceof LifecycleOwner) {
            ((LifecycleOwner) mView).getLifecycle().addObserver(this);
            if (mModel != null && mModel instanceof LifecycleObserver) {
                ((LifecycleOwner) mView).getLifecycle().addObserver((LifecycleObserver) mModel);
            }
        }
    }

    private List<Disposable> mDisposable;

    protected List<Disposable> getDisposable() {
        if (mDisposable == null) {
            mDisposable = new ArrayList<>();
        }
        return mDisposable;
    }

    protected void addDisposable(Disposable disposable) {
        if (mDisposable == null) {
            mDisposable = new ArrayList<>();
        }
        mDisposable.add(disposable);
    }

    protected void removeDisposable(Disposable disposable) {
        if (mDisposable != null) {
            if (!disposable.isDisposed()) disposable.dispose();
            mDisposable.remove(disposable);
        }
    }

    protected void cleanDisposable() {
        if (mDisposable != null) {
            for (Disposable arg : mDisposable) {
                if (arg != null && !arg.isDisposed()) {
                    arg.dispose();
                }
            }

            mDisposable.clear();
            mDisposable = null;
        }
    }

    // 生命周期方法

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected void onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {
    }

    @CallSuper
    @Override
    public void onDestroy() {
        mView = null;
        if (mModel != null)
            mModel.onDestroy();
        mModel = null;

        cleanDisposable();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }
}
