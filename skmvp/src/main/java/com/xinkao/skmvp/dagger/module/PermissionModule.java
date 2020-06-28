package com.xinkao.skmvp.dagger.module;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xinkao.skmvp.dagger.scope.ActivityScope;
import com.xinkao.skmvp.utils.permission.PermissionContract;
import com.xinkao.skmvp.utils.permission.PermissionPresenter;

import androidx.fragment.app.FragmentActivity;
import dagger.Module;
import dagger.Provides;

@Module
public class PermissionModule {

    private PermissionContract.V v;

    public PermissionModule(PermissionContract.V v) {
        this.v = v;
    }

    @ActivityScope
    @Provides
    PermissionContract.V providePermissionContractView() {
        return v;
    }

    @ActivityScope
    @Provides
    RxPermissions provideRxPermissions(PermissionContract.V view) {
        return new RxPermissions((FragmentActivity) view.getActivity());
    }

    @ActivityScope
    @Provides
    public PermissionPresenter providePermissionPresenter(PermissionContract.V view) {
        return new PermissionPresenter(view);
    }
}
