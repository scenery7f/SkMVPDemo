package com.xinkao.skmvpdemo.mvp.fragment;

import com.xinkao.skmvp.mvp.presenter.BasePresenter;

import javax.inject.Inject;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

public class HomePresenter extends BasePresenter<HomeContract.V, HomeContract.M> implements HomeContract.P {

    @Inject
    public HomePresenter(HomeContract.V mView, HomeContract.M mModel) {
        super(mView, mModel);
    }
}