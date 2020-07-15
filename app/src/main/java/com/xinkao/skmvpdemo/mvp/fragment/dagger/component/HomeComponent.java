package com.xinkao.skmvpdemo.mvp.fragment.dagger.component;

import com.xinkao.skmvpdemo.mvp.fragment.dagger.module.HomeModule;
import com.xinkao.skmvpdemo.mvp.fragment.HomeFragment;
import com.xinkao.skmvp.dagger.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(modules = {HomeModule.class})
public interface HomeComponent {
    void Inject(HomeFragment arg);
}