package com.xinkao.skmvpdemo.mvp.main.dagger.module;

import com.xinkao.skmvpdemo.mvp.main.MainContract;
import com.xinkao.skmvpdemo.mvp.main.MainModel;
import com.xinkao.skmvpdemo.mvp.main.MainPresenter;
import com.xinkao.skmvp.dagger.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private MainContract.V v;

    public MainModule(MainContract.V v) {
        this.v = v;
    }

    @ActivityScope
    @Provides
    MainContract.V provideMainView() {
        return v;
    }

    @ActivityScope
    @Provides
    MainContract.M provideMainModel(MainModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    MainContract.P provideMainPresenter(MainPresenter presenter) {
        return presenter;
    }
}