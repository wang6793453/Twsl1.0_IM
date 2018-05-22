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
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.DialogUtils;
import com.twlrg.twsl.widget.AutoFitTextView;

import java.util.Arrays;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/5/15 23:49
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class RoomDetailActivity extends BaseActivity
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.et_room_title)
    EditText        etRoomTitle;
    @BindView(R.id.et_area)
    EditText        etArea;
    @BindView(R.id.et_check_in)
    EditText        etCheckIn;
    @BindView(R.id.et_floor)
    EditText        etFloor;
    @BindView(R.id.et_bed_type)
    EditText        etBedType;
    @BindView(R.id.et_add_bed)
    EditText        etAddBed;
    @BindView(R.id.tv_smokeless)
    TextView        tvSmokeless;
    @BindView(R.id.ll_smokeless)
    LinearLayout    llSmokeless;
    @BindView(R.id.tv_wifi)
    TextView        tvWifi;
    @BindView(R.id.ll_wifi)
    LinearLayout    llWifi;
    @BindView(R.id.tv_window)
    TextView        tvWindow;
    @BindView(R.id.ll_window)
    LinearLayout    llWindow;
    @BindView(R.id.btn_save)
    Button          btnSave;

    private int smokeless = 0;//1,//0为无烟房，1为有烟房
    private int wifi      = 0;//0,//0为无WIFI，1为有WIFI
    private int window    = 0;//1,//0为无窗，1为有窗

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_room_detail);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        llSmokeless.setOnClickListener(this);
        llWifi.setOnClickListener(this);
        llWindow.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(true);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("客房详情");
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == llSmokeless)
        {
            final String[] smokelessArr = getResources().getStringArray(R.array.smokelessType);
            DialogUtils.showCategoryDialog(this, Arrays.asList(smokelessArr), new MyItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    smokeless = position;

                    tvSmokeless.setText(smokelessArr[position]);

                }
            });
        }
        else if (v == llWifi)
        {
            final String[] wifiArr = getResources().getStringArray(R.array.wifiType);
            DialogUtils.showCategoryDialog(this, Arrays.asList(wifiArr), new MyItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    wifi = position;

                    tvWifi.setText(wifiArr[position]);

                }
            });
        }
        else if (v == llWindow)
        {
            final String[] windowArr = getResources().getStringArray(R.array.windowType);
            DialogUtils.showCategoryDialog(this, Arrays.asList(windowArr), new MyItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    window = position;

                    tvWindow.setText(windowArr[position]);

                }
            });
        }
        else if (v == btnSave)
        {
            String mRoomTitle = etRoomTitle.getText().toString();
            String mAre = etArea.getText().toString();
            String mCheckIn = etCheckIn.getText().toString();
            String mFloor = etFloor.getText().toString();
            String mBedType = etBedType.getText().toString();
            String mAddBed = etAddBed.getText().toString();
        }
    }
}
