package com.xinkao.skmvp.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.xinkao.skmvp.dagger.component.AppComponent;
import com.xinkao.skmvp.dagger.component.DaggerAppComponent;
import com.xinkao.skmvp.network.RetrofitManager;

import es.dmoral.toasty.MyToast;

public abstract class BaseApplication extends Application implements IApplication, IAppManager {

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        appComponent = DaggerAppComponent.builder().build();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化MyToast
        if (isDebug()) {
            // Logger 初始化
            Logger.addLogAdapter(new AndroidLogAdapter());
        }

        RetrofitManager.config(isDebug());

        MyToast.init(this, isDebug(), false);

        initConfig();
    }

    /**
     * 跳转到系统应用设置页面->用于打开权限等操作。
     */
    public void toSelfSetting() {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(mIntent);
    }
}
