package com.xinkao.skmvp.utils;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class MyToast {
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static void info(Object str) {
        if (mContext != null)
            Toasty.info(mContext, String.valueOf(str), Toast.LENGTH_SHORT, true).show();
    }

    public static void success(Object str) {
        if (mContext != null)
            Toasty.success(mContext, String.valueOf(str), Toast.LENGTH_SHORT, true).show();
    }

    public static void warn(Object str) {
        if (mContext != null)
            Toasty.warning(mContext, String.valueOf(str), Toast.LENGTH_SHORT, true).show();
    }

    public static void error(Object str) {
        if (mContext != null)
            Toasty.error(mContext, String.valueOf(str), Toast.LENGTH_SHORT, true).show();
    }

}
