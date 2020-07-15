package com.xinkao.skmvpdemo.mvp.fragment;

import com.xinkao.skmvp.mvp.view.BaseFragment;

public class HomeFragment extends BaseFragment<HomeContract.P> implements HomeContract.V {
    @Override
    public int getContextView() {
        return 0;
    }
}
