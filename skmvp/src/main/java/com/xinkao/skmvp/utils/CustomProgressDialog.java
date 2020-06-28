package com.xinkao.skmvp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.wang.avi.AVLoadingIndicatorView;
import com.xinkao.skmvp.R;
import com.xinkao.skmvp.base.BaseConfig;

import java.lang.ref.WeakReference;

public class CustomProgressDialog extends ProgressDialog {

	public CustomProgressDialog(Context context) {
		super(context, R.style.Dialog_Fullscreen);
		setCanceledOnTouchOutside(false);
	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
		setCanceledOnTouchOutside(false);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_layout);

		if (BaseConfig.CUSTOM_PROGRESS_DIALOG_COLOR != -1) { // 自定义颜色
			AVLoadingIndicatorView a = findViewById(R.id.avloading);
			a.setIndicatorColor(BaseConfig.CUSTOM_PROGRESS_DIALOG_COLOR);
		}
 	}

}
