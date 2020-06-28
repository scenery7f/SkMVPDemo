package com.xinkao.skmvp.dagger.component;

import com.xinkao.skmvp.dagger.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
}
