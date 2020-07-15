package com.xinkao.skmvpdemo.mvp.main.dagger.component;

import com.xinkao.skmvpdemo.mvp.main.dagger.module.MainModule;
import com.xinkao.skmvpdemo.mvp.main.MainActivity;
import com.xinkao.skmvp.dagger.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = {MainModule.class})
public interface MainComponent {
    void Inject(MainActivity arg);
}