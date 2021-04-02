package com.xinkao.skmvpdemo.mvp.main;

import android.view.View;

import com.xinkao.skmvp.mvp.view.BaseActivity;
import com.xinkao.skmvpdemo.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<MainContract.P> implements MainContract.V {

    ActivityMainBinding binding;

    @Override
    public View getContentView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

}
