package com.xinkao.skmvpdemo.mvp.fragment.dagger.module;

import com.xinkao.skmvpdemo.mvp.fragment.HomeContract;
import com.xinkao.skmvpdemo.mvp.fragment.HomeModel;
import com.xinkao.skmvpdemo.mvp.fragment.HomePresenter;
import com.xinkao.skmvp.dagger.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {

    private HomeContract.V v;

    public HomeModule(HomeContract.V v) {
        this.v = v;
    }

    @FragmentScope
    @Provides
    HomeContract.V provideHomeView() {
        return v;
    }

    @FragmentScope
    @Provides
    HomeContract.M provideHomeModel(HomeModel model) {
        return model;
    }

    @FragmentScope
    @Provides
    HomeContract.P provideHomePresenter(HomePresenter presenter) {
        return presenter;
    }
}