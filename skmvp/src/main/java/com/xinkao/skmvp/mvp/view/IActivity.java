package com.xinkao.skmvp.mvp.view;

import android.os.Bundle;
import android.view.View;

import com.xinkao.skmvp.dagger.component.AppComponent;

public interface IActivity {
    default boolean checkDataBefoAll(){return true;};
    default void registerDagger(AppComponent appComponent){}
//    int getContentView();
    View getContentView();

    /**
     * @param savedInstanceState
     * @return true:继续执行initLoadData，false：不再执行initLoadData
     */
    default boolean dataRecovery(Bundle savedInstanceState){return true;};
    default void initBindWidget(){}
    default void initSetListener(){}
    default void initLoadData(){}
    void exit();
}
