package com.xinkao.skmvp.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.xinkao.skmvp.base.BaseApplication;
import com.xinkao.skmvp.mvp.presenter.IPresenter;
import com.xinkao.skmvp.utils.CustomProgressDialog;
import com.xinkao.skmvp.utils.MyToast;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IFragment, IView {

    public BaseFragment() {
    }

    @Inject
    protected P mPresenter;

    public View rootView;

    // 简单的加载中弹出框
    private CustomProgressDialog mCPDialog;

    // 记录是否第一次运行onPush;
    private boolean isFirst = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {

            if (getActivity().getApplication() != null && getActivity().getApplication() instanceof BaseApplication)
                registerDagger(((BaseApplication) getActivity().getApplicationContext()).getAppComponent());
            else
                registerDagger(null);

            /**
             * 1、加载布局文件
             */
            initGetDataFromParent(savedInstanceState);
            /**
             * 2、加载布局文件,返回布局文件id
             */
            rootView = inflater.inflate(getContextView(), container, false);

        } else {
            // 缓存的rootView需要判断是否已经被加过parent，
            // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * 3、绑定控件
         */
        initBindWidget();
        /**
         * 4、给控件绑定事件
         */
        initSetListener();
        /**
         * 5、初始化数据
         */
        initLoadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) {
            isFirst = false;
            firstLoadData();
        } else {
            refreshData();
        }

//        Logger.d("打印：onResume-");
    }

    @Override
    public void onPause() {
        super.onPause();
        cleanData();
//        Logger.d("打印：onPause-");
    }

    protected final <E extends BaseApplication> E getApp() {
        if (rootView.getContext().getApplicationContext() instanceof BaseApplication) {
            return (E) rootView.getContext().getApplicationContext();
        } else {
            throw new RuntimeException("无法获取-BaseApplication");
        }
    }

    @Override
    public Context getContext() {
        return rootView.getContext();
    }

    @Override
    public void toastInfo(String info) {
        // 判断是否在主线程刷新
        if (Looper.getMainLooper() != Looper.myLooper()) {
            getActivity().runOnUiThread(new Runnable() {
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
            getActivity().runOnUiThread(new Runnable() {
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
            getActivity().runOnUiThread(new Runnable() {
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
            getActivity().runOnUiThread(new Runnable() {
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
        Intent intent = new Intent(rootView.getContext(), acClass);

        if (bundle != null)
            intent.putExtras(bundle);

        startUseIntent(needFinish, intent);
    }

    @Override
    public void startUseIntent(boolean needFinish, Intent intent) {
        startActivity(intent);
        if (needFinish) finishThis();
    }

    @Override
    public void finishThis() {
        // 移除
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
    }

    @Override
    public void showLoading() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
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
        if (mCPDialog != null) {
            // 判断是否在主线程刷新
            if (Looper.getMainLooper() != Looper.myLooper()) {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("打印：onDestroy-");
        if (mPresenter != null)
            mPresenter.onDestroy();
        mPresenter = null;

        if (mCPDialog != null && mCPDialog.isShowing())
            mCPDialog.dismiss();
        mCPDialog = null;

    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        Logger.d("打印：onAttach-");
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Logger.d("打印：onCreate-");
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Logger.d("打印：onActivityCreated-");
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Logger.d("打印：onStart-");
//    }
//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Logger.d("打印：onSaveInstanceState-");
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        Logger.d("打印：onStop-");
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        Logger.d("打印：onDestroyView-");
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        Logger.d("打印：onDetach-");
//    }
}
