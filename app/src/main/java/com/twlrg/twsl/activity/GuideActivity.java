package com.twlrg.twsl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.ViewPagerAdapter;
import com.twlrg.twsl.utils.ConfigManager;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/12 15:46
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener
{
    @BindView(R.id.viewpager)
    ViewPager    viewpager;
    @BindView(R.id.btn_submit)
    Button       mSubmitBtn;
    @BindView(R.id.iv_point1)
    ImageView    ivPoint1;
    @BindView(R.id.iv_point2)
    ImageView    ivPoint2;
    @BindView(R.id.iv_point3)
    ImageView    ivPoint3;
    @BindView(R.id.ll_point)
    LinearLayout llPoint;


    // 定义ViewPager适配器
    private ViewPagerAdapter vpAdapter;
    // 定义一个ArrayList来存放View
    private ArrayList<View>  views;
    // 引导图片资源
    private static final int[] pics = {
            R.drawable.guide1, R.drawable.guide2,
            R.drawable.guide3};


    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_guide);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {

        mSubmitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(GuideActivity.this, WelComeActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void initViewData()
    {
        views = new ArrayList<View>();
        // 实例化ViewPager
        // 实例化ViewPager适配器
        vpAdapter = new ViewPagerAdapter(views);


        if (!ConfigManager.instance().getIsFristLogin())
        {
            startActivity(new Intent(GuideActivity.this, WelComeActivity.class));
            finish();
        }
        ConfigManager.instance().setIsFristLogin(false);


        // 定义一个布局并设置参数
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        // 初始化引导图片列表
        for (int i = 0; i < pics.length; i++)
        {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            //防止图片不能填满屏幕
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //加载图片资源
            iv.setImageResource(pics[i]);
            views.add(iv);
        }

        // 设置数据
        viewpager.setAdapter(vpAdapter);
        // 设置监听
        viewpager.addOnPageChangeListener(this);


    }

    @Override
    public void onClick(View v)
    {
        int position = (Integer) v.getTag();
        setCurView(position);
    }

    /**
     * 设置当前页面的位置
     */
    private void setCurView(int position)
    {
        if (position < 0 || position >= pics.length)
        {
            return;
        }
        viewpager.setCurrentItem(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {

    }

    @Override
    public void onPageSelected(int position)
    {
        //判断是否是最后一页，若是则显示按钮


        switch (position)
        {
            case 0:
                ivPoint1.setImageResource(R.drawable.ic_point_selected);
                ivPoint2.setImageResource(R.drawable.ic_point_normal);
                ivPoint3.setImageResource(R.drawable.ic_point_normal);
                mSubmitBtn.setVisibility(View.GONE);
                llPoint.setVisibility(View.VISIBLE);
                break;

            case 1:
                ivPoint1.setImageResource(R.drawable.ic_point_normal);
                ivPoint2.setImageResource(R.drawable.ic_point_selected);
                ivPoint3.setImageResource(R.drawable.ic_point_normal);
                mSubmitBtn.setVisibility(View.GONE);
                llPoint.setVisibility(View.VISIBLE);
                break;

            case 2:
                llPoint.setVisibility(View.GONE);
                mSubmitBtn.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }


}
