# SkMVPDemo

[![](https://jitpack.io/v/scenery7f/SkMVPDemo.svg)](https://jitpack.io/#scenery7f/SkMVPDemo)

allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
    
dependencies {
        implementation 'com.github.scenery7f:SkMVPDemo:Tag'
}

使用SkMVPDagger.jar快速创建相应文件


compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
}

// dagger
annotationProcessor 'com.google.dagger:dagger-compiler:2.27' // 要放到项目的gradle中
// 控件绑定
annotationProcessor 'com.jakewharton:butterknife-compiler:10.2.1' // 要放到项目的gradle中

继承BaseApplication

添加
// 初始化本地存储
SharedPreferencesUtils.conf(Config.SP_NAME);

// 初始化网络管理
RetrofitManager.config(Config.BASE_URL);

// 加载中 动画颜色
BaseConfig.CUSTOM_PROGRESS_DIALOG_COLOR = ContextCompat.getColor(this, R.color.sysColor);

