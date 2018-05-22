package com.twlrg.twsl;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;


import com.baidu.mapapi.SDKInitializer;
import com.twlrg.twsl.im.TencentCloud;
import com.twlrg.twsl.service.LocationService;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.StringUtils;



/**
 * 作者：王先云 on 2016/8/5 14:46
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class MyApplication extends Application
{
    public         LocationService locationService;
    public         Vibrator        mVibrator;
    private static MyApplication   instance;

    public static MyApplication getInstance() {return instance;}


    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        APPUtils.configImageLoader(getApplicationContext());
        ConfigManager.instance().init(this);
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        TencentCloud.init(this);
    }


    public boolean isLogin()
    {
        if (StringUtils.stringIsEmpty(ConfigManager.instance().getUserID()))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static MyApplication getContext(){
        return instance;
    }

}
