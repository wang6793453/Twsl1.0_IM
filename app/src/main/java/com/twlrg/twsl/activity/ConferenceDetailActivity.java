package com.twlrg.twsl.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.widget.AutoFitTextView;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/5/15 23:49
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class ConferenceDetailActivity extends BaseActivity
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.et_title)
    EditText        etTitle;
    @BindView(R.id.et_area)
    EditText        etArea;
    @BindView(R.id.et_ckg)
    EditText        etCkg;
    @BindView(R.id.et_floor)
    EditText        etFloor;
    @BindView(R.id.tv_led)
    TextView        tvLed;
    @BindView(R.id.ll_led)
    LinearLayout    llLed;
    @BindView(R.id.et_theatre)
    EditText        etTheatre;
    @BindView(R.id.et_desk)
    EditText        etDesk;
    @BindView(R.id.et_banquet)
    EditText        etBanquet;
    @BindView(R.id.et_fishbone)
    EditText        etFishbone;
    @BindView(R.id.et_price)
    EditText        etPrice;
    @BindView(R.id.btn_save)
    Button          btnSave;

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_conference_detail);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(true);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("会议室详情");
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }

    }


}
