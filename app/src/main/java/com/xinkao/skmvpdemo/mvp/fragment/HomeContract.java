package com.xinkao.skmvpdemo.mvp.fragment;

import com.xinkao.skmvp.mvp.model.IModel;
import com.xinkao.skmvp.mvp.presenter.IPresenter;
import com.xinkao.skmvp.mvp.view.IView;

public interface HomeContract {
    interface V extends IView{}
    interface P extends IPresenter{}
    interface M extends IModel{}
    interface Net {}
}