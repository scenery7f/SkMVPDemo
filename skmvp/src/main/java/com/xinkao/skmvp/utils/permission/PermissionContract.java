package com.xinkao.skmvp.utils.permission;

import android.app.Activity;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xinkao.skmvp.mvp.presenter.IPresenter;
import com.xinkao.skmvp.mvp.view.IView;

import java.util.List;

public interface PermissionContract{
    interface V extends IView {
        Activity getActivity();
//        RxPermissions getRxPermissions();
        void permissionError(String title, String msg, List<String> permissions);
        void permissionOver();
    }
}
