package com.twlrg.twsl.activity;

import android.content.Intent;
import android.os.Bundle;

import com.twlrg.twsl.utils.ConfigManager;

/**
 * 作者：王先云 on 2018/4/12 16:38
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class IndexActivity extends  BaseActivity
{
    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {

    }

    @Override
    protected void initEvent()
    {

    }

    @Override
    protected void initViewData()
    {
        if(ConfigManager.instance().getIsFristLogin())
        {
            startActivity(new Intent(this,GuideActivity.class));
        }
        else
        {
            startActivity(new Intent(this,WelComeActivity.class));
        }
        finish();
    }
}
