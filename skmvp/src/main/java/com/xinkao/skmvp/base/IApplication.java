package com.xinkao.skmvp.base;

public interface IApplication {
    void initConfig();
    default boolean isDebug() {return false;};
}
