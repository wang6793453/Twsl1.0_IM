package com.tencent.qcloud.presentation.viewfeatures;

/**
 * 闪屏界面控制接口
 */
public interface SplashView extends MvpView {

    /**
     * 跳转到主界面
     */
    void navToHome();




    /**
     * 是否已有用户登录
     */
    boolean isUserLogin();

    /**
     * IM是否登录
     */
    boolean isIMLogin();

    /**
     * IM登录
     */
    void imLogin();
}
