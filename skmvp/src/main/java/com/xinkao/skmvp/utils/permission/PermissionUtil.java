/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xinkao.skmvp.utils.permission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xinkao.skmvp.mvp.view.IActivity;
import com.xinkao.skmvp.mvp.view.IView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * ================================================
 * 权限请求工具类
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#3.9">PermissionUtil wiki 官方文档</a>
 * Created by JessYan on 17/10/2016 10:09
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class PermissionUtil {
//    public static final int CODE_RECORD_AUDIO = 0;
//    public static final int CODE_GET_ACCOUNTS = 1;
//    public static final int CODE_READ_PHONE_STATE = 2;
//    public static final int CODE_CALL_PHONE = 3;
//    public static final int CODE_CAMERA = 4;
//    public static final int CODE_ACCESS_FINE_LOCATION = 5;
//    public static final int CODE_ACCESS_COARSE_LOCATION = 6;
//    public static final int CODE_READ_EXTERNAL_STORAGE = 7;
//    public static final int CODE_WRITE_EXTERNAL_STORAGE = 8;
//    public static final int CODE_MULTI_PERMISSION = 100;

//    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
//    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
//    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
//    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
//    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
//    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
//    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
//    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
//    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

//    private static final String[] requestPermissions = {
//            PERMISSION_RECORD_AUDIO,
//            PERMISSION_GET_ACCOUNTS,
//            PERMISSION_READ_PHONE_STATE,
//            PERMISSION_CALL_PHONE,
//            PERMISSION_CAMERA,
//            PERMISSION_ACCESS_FINE_LOCATION,
//            PERMISSION_ACCESS_COARSE_LOCATION,
//            PERMISSION_READ_EXTERNAL_STORAGE
//    };

    private PermissionUtil() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    public static void requestPermission(final RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return;
        }

        List<String> needRequest = new ArrayList<>();
        for (String permission : permissions) { //过滤调已经申请过的权限
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission);
            }
        }

        if (needRequest.isEmpty()) {//全部权限都已经申请过，直接执行操作
            requestPermission.onRequestPermissionSuccess();
        } else {//没有申请过,则开始申请
            rxPermissions
                    .requestEach(needRequest.toArray(new String[0]))
                    .buffer(permissions.length)
                    .subscribe(new ErrorHandleSubscriber<List<Permission>>(errorHandler) {
                        @Override
                        public void onNext(@NonNull List<Permission> permissions) {
                            List<String> failurePermissions = new ArrayList<>();
                            List<String> askNeverAgainPermissions = new ArrayList<>();
                            for (Permission p : permissions) {
                                if (!p.granted) {
                                    if (p.shouldShowRequestPermissionRationale) {
                                        failurePermissions.add(p.name);
                                    } else {
                                        askNeverAgainPermissions.add(p.name);
                                    }
                                }
                            }
                            if (failurePermissions.size() > 0) {
                                Logger.d("Request permissions failure");
                                requestPermission.onRequestPermissionFailure(failurePermissions);
                            }

                            if (askNeverAgainPermissions.size() > 0) {
                                Logger.d("Request permissions failure with ask never again");
                                requestPermission.onRequestPermissionFailureWithAskNeverAgain(askNeverAgainPermissions);
                            }

                            if (failurePermissions.size() == 0 && askNeverAgainPermissions.size() == 0) {
                                Logger.d("Request permissions success");
                                requestPermission.onRequestPermissionSuccess();
                            }
                        }
                    });
        }

    }

    /**
     * 请求摄像头权限
     */
    public static void launchCamera(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    /**
     * 请求外部存储的权限
     */
    public static void externalStorage(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 请求发送短信权限
     */
    public static void sendSms(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.SEND_SMS);
    }

    /**
     * 请求打电话权限
     */
    public static void callPhone(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.CALL_PHONE);
    }

    /**
     * 请求获取手机状态的权限
     */
    public static void readPhonestate(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.READ_PHONE_STATE);
    }

    public interface RequestPermission {

        /**
         * 权限请求成功
         */
        void onRequestPermissionSuccess();

        /**
         * 用户拒绝了权限请求, 权限请求失败, 但还可以继续请求该权限
         *
         * @param permissions 请求失败的权限名
         */
        void onRequestPermissionFailure(List<String> permissions);

        /**
         * 用户拒绝了权限请求并且用户选择了以后不再询问, 权限请求失败, 这时将不能继续请求该权限, 需要提示用户进入设置页面打开该权限
         *
         * @param permissions 请求失败的权限名
         */
        void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions);
    }

    /**
     * 提示用户打开权限
     *
     * @param context
     * @param message
     * @param okListener
     */
    private static void showMessageOKCancel(final Activity context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("退出App", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (context instanceof IActivity) {
                            ((IActivity) context).exit();
                        } else {
                            System.exit(0);
                        }
                    }
                })
                .create()
                .show();

    }

//    /**
//     * @param activity
//     * @param isShouldRationale true: return no granted and shouldShowRequestPermissionRationale permissions, false:return no granted and !shouldShowRequestPermissionRationale
//     * @return
//     */
//    public static ArrayList<String> getNoGrantedPermission(Activity activity, boolean isShouldRationale) {
//
//        ArrayList<String> permissions = new ArrayList<>();
//
//        for (int i = 0; i < requestPermissions.length; i++) {
//            String requestPermission = requestPermissions[i];
//
//
//            //TODO checkSelfPermission
//            int checkSelfPermission = -1;
//            try {
//                checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
//            } catch (RuntimeException e) {
//                Toast.makeText(activity, "please open those permission", Toast.LENGTH_SHORT)
//                        .show();
//                Logger.e("RuntimeException:" + e.getMessage());
//                return null;
//            }
//
//            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
//                Logger.i("getNoGrantedPermission ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED:" + requestPermission);
//
//                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) {
//                    Logger.d("shouldShowRequestPermissionRationale if");
//                    if (isShouldRationale) {
//                        permissions.add(requestPermission);
//                    }
//
//                } else {
//
//                    if (!isShouldRationale) {
//                        permissions.add(requestPermission);
//                    }
//                    Logger.d("shouldShowRequestPermissionRationale else");
//                }
//
//            }
//        }
//
//        return permissions;
//    }
}

