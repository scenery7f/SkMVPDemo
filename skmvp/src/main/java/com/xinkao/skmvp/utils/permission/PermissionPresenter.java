package com.xinkao.skmvp.utils.permission;

import android.Manifest;
import android.content.Context;

import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;

public class PermissionPresenter {

    private Map<String, String> permissionMsg;

    private PermissionContract.V mView;

    public PermissionPresenter(PermissionContract.V mView) {
        this.mView = mView;
    }

    public void requestPresenter(RxPermissions rxPermissions, String... permissions){
        requestPresenter(rxPermissions, Arrays.asList(permissions));
    }

    public void requestPresenter(RxPermissions rxPermissions, List<String> permissions) {

        // 没有请求别来捣乱
        if (permissions == null) {
            mView.permissionOver();
            return;
        }

        RxErrorHandler errorHandler = RxErrorHandler
                .builder()
                .with(mView.getContext())
                .responseErrorListener(new ResponseErrorListener() {
                    @Override
                    public void handleResponseError(Context context, Throwable t) {
                        if (t instanceof UnknownHostException) {
                            //do something ...
                        } else if (t instanceof SocketTimeoutException) {
                            //do something ...
                        } else {
                            //handle other Exception ...
                        }
                        Logger.w("Error handle");
                    }
                }).build();

        PermissionUtil.requestPermission(
                new PermissionUtil.RequestPermission() {
                    @Override
                    public void onRequestPermissionSuccess() {
                        // 执行下面的操作
                        mView.permissionOver();
                    }

                    @Override
                    public void onRequestPermissionFailure(List<String> permissions) {
                        StringBuffer buffer = new StringBuffer("以下权限：\n");
                        for (String arg : permissions) {
                            buffer.append("    " + getPermissionsMsg(arg));
                            buffer.append("\n");
                        }
                        buffer.append("为保证正常使用，请您同意！");

                        mView.permissionError("使用权限", buffer.toString(), permissions);
                    }

                    @Override
                    public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                        StringBuffer buffer = new StringBuffer("以下权限：\n");
                        for (String arg : permissions) {
                            buffer.append("    " + getPermissionsMsg(arg));
                            buffer.append("\n");
                        }
                        buffer.append("为保证正常使用，请您在设置中打开！");

                        mView.permissionError("使用权限", buffer.toString(), null);
                    }
                },
                rxPermissions,
                errorHandler,
                // 开始添加请求权限
                permissions.toArray(new String[permissions.size()])
        );
    }

    /**
     * 根据权限获取相关注释
     *
     * @param permission
     * @return
     */
    private String getPermissionsMsg(String permission) {

        if (permissionMsg == null) {
            permissionMsg = new HashMap<>();
            permissionMsg.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储权限");
            permissionMsg.put(Manifest.permission.READ_PHONE_STATE, "获取手机状态");
            permissionMsg.put(Manifest.permission.CAMERA, "相机权限");
            permissionMsg.put(Manifest.permission.CALL_PHONE, "直接拨打电话");
            permissionMsg.put(Manifest.permission.ACCESS_COARSE_LOCATION, "获取大致位置");
            permissionMsg.put(Manifest.permission.ACCESS_FINE_LOCATION, "使用GPS");
        }

        return permissionMsg.get(permission);
    }
}
