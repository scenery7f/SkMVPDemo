package com.xinkao.skmvp.mvp.view;

import android.os.Bundle;

import com.xinkao.skmvp.dagger.component.AppComponent;

public interface IFragment {

    default void initGetDataFromParent(Bundle savedInstanceState){}

    default void registerDagger(AppComponent appComponent){}
    int getContextView();
    default void initBindWidget(){}
    default void initSetListener(){}
    default void initLoadData(){}
    // 执行懒加载方法(第一次加载数据，为了和子类的lazyLoadData分开而使用不同的方法名)
    default void firstLoadData(){}
    // 执行onResume方法时调用
    default void refreshData(){}
    // 执行onPause方法时调用
    default void cleanData(){}

}
