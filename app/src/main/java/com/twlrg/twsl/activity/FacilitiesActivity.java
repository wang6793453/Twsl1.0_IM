package com.twlrg.twsl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.twlrg.twsl.R;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.widget.AutoFitTextView;


import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/23 21:32
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class FacilitiesActivity extends BaseActivity
{

    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.rl_jdss)
    RelativeLayout  rlJdss;
    @BindView(R.id.rl_hdss)
    RelativeLayout  rlHdss;
    @BindView(R.id.rl_kfss)
    RelativeLayout  rlKfss;
    @BindView(R.id.rl_hcss)
    RelativeLayout  rlHcss;
    @BindView(R.id.rl_ctss)
    RelativeLayout  rlCtss;


    @Override
    protected void initData()
    {
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_facilities);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        rlJdss.setOnClickListener(this);
        rlHdss.setOnClickListener(this);
        rlKfss.setOnClickListener(this);
        rlHcss.setOnClickListener(this);
        rlCtss.setOnClickListener(this);

    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("设施管理");

    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == rlJdss)
        {
            startActivity(new Intent(FacilitiesActivity.this, EditFacilitesActivity.class).putExtra("ID", "1"));
        }
        else if (v == rlHdss)
        {
            startActivity(new Intent(FacilitiesActivity.this, EditFacilitesActivity.class).putExtra("ID", "2"));
        }
        else if (v == rlKfss)
        {
            startActivity(new Intent(FacilitiesActivity.this, EditFacilitesActivity.class).putExtra("ID", "3"));
        }
        else if (v == rlHcss)
        {
            startActivity(new Intent(FacilitiesActivity.this, EditFacilitesActivity.class).putExtra("ID", "4"));
        }
        else if (v == rlCtss)
        {
            startActivity(new Intent(FacilitiesActivity.this, EditFacilitesActivity.class).putExtra("ID", "5"));
        }
    }


}
