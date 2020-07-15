package com.xinkao.skmvpdemo.mvp.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xinkao.skmvp.mvp.view.BaseActivity;
import com.xinkao.skmvpdemo.R;

public class MainActivity extends BaseActivity<MainContract.P> implements MainContract.V {

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

}
