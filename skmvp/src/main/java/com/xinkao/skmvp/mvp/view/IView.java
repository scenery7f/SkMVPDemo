package com.xinkao.skmvp.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import es.dmoral.toasty.MyToast;

public interface IView {
    Context getContext();

    void showLoading();

    void cancelLoading();

    void toastInfo(String info);

    void toastSuccess(String success);

    void toastWarn(String warn);

    void toastError(String error);

    default void startUseIntent(Class<?> acClass) {
        startUseIntent(false, acClass, null);
    }

    default void startUseIntent(Class<?> acClass, Bundle bundle) {
        startUseIntent(false, acClass, bundle);
    }

    default void startUseIntent(boolean needFinish, Class<?> acClass) {
        startUseIntent(needFinish, acClass, null);
    }

    void startUseIntent(boolean needFinish, Class<?> acClass, Bundle bundle);

    default void startUseIntent(Intent intent) {
        startUseIntent(false, intent);
    }

    ;

    void startUseIntent(boolean needFinish, Intent intent);

    void finishThis();
}
