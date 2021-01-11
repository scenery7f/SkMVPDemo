package com.xinkao.skmvp.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;

import com.xinkao.skmvp.base.BaseApplication;
import com.xinkao.skmvp.mvp.presenter.IPresenter;
import com.xinkao.skmvp.utils.CustomProgressDialog;
import com.xinkao.skmvp.utils.MyToast;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IActivity, IView {

    @Inject
    protected P mPresenter;

    // 简单的加载中弹出框
    private CustomProgressDialog mCPDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 将Activity添加到管理，方便退出
        getApp().addActivity(this);

        // 开始加载页面前校验数据
        if (!checkDataBefoAll())
            return;

        // 通过Dagger注册数据
        if (getApplication() instanceof BaseApplication) {
            registerDagger(((BaseApplication) getApplicationContext()).getAppComponent());
        } else {
            registerDagger(null);
        }

        // 数据恢复用
        boolean doInitLoadData = dataRecovery(savedInstanceState);

        setContentView(getContentView());

        // 绑定控件
        ButterKnife.bind(this);

        initBindWidget();

        initSetListener();

        if (doInitLoadData)
            initLoadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getApp().removeActivity(this);
        if (mCPDialog != null && mCPDialog.isShowing())
            mCPDialog.dismiss();
        mCPDialog = null;
        if (mPresenter != null)
            mPresenter.onDestroy();
        mPresenter = null;
    }

    public final <E extends BaseApplication> E getApp() {
        if (getApplication() instanceof BaseApplication) {
            return (E) getApplication();
        } else {
            throw new RuntimeException("无法获取-BaseApplication");
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void toastInfo(String info) {
        // 判断是否在主线程刷新
        if (Looper.getMainLooper() != Looper.myLooper()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyToast.info(info);
                }
            });
        } else {
            MyToast.info(info);
        }
    }

    @Override
    public void toastSuccess(String success) {
        // 判断是否在主线程刷新
        if (Looper.getMainLooper() != Looper.myLooper()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyToast.success(success);
                }
            });
        } else {
            MyToast.success(success);
        }
    }

    @Override
    public void toastWarn(String warn) {
        // 判断是否在主线程刷新
        if (Looper.getMainLooper() != Looper.myLooper()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyToast.warn(warn);
                }
            });
        } else {
            MyToast.warn(warn);
        }
    }

    @Override
    public void toastError(String error) {
        // 判断是否在主线程刷新
        if (Looper.getMainLooper() != Looper.myLooper()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyToast.error(error);
                }
            });
        } else {
            MyToast.error(error);
        }
    }

    @Override
    public void startUseIntent(boolean needFinish, Class<?> acClass, Bundle bundle) {
        Intent intent = new Intent(this, acClass);

        if (bundle != null)
            intent.putExtras(bundle);

        startUseIntent(needFinish, intent);
    }

    public void startUseIntent(boolean needFinish, Intent intent) {
        startActivity(intent);
        if (needFinish) finish();
    }

    @Override
    public void finishThis() {
        finish();
        getApp().removeActivity(this);
    }

    @Override
    public void exit() {
        getApp().exit();
    }

    @Override
    public void showLoading() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLoading();
                }
            });
            return;
        }

        if (mCPDialog == null) {
            mCPDialog = new CustomProgressDialog(getContext());
        }
        mCPDialog.show();
    }

    @Override
    public void cancelLoading() {
        if (mCPDialog != null && mCPDialog.isShowing()) {
            // 判断是否在主线程
            if (Looper.getMainLooper() != Looper.myLooper()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCPDialog.dismiss();
                    }
                });
            } else {
                mCPDialog.dismiss();
            }
        }
    }
}
