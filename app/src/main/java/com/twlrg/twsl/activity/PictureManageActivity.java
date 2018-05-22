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
 * 作者：王先云 on 2018/5/16 13:54
 * 邮箱：wangxianyun1@163.com
 * 描述：图片管理
 */
public class PictureManageActivity extends BaseActivity
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.rl_hotel)
    RelativeLayout  rlHotel;
    @BindView(R.id.rl_room)
    RelativeLayout  rlRoom;
    @BindView(R.id.rl_conference)
    RelativeLayout  rlConference;

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_picture_manage);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        rlRoom.setOnClickListener(this);
        rlHotel.setOnClickListener(this);
        rlConference.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(true);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("图片管理");
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        if (v == ivBack)
        {
            finish();
        }
        else if (v == rlRoom)
        {
            startActivity(new Intent(PictureManageActivity.this, PictureRoomListActivity.class));
        }
        else if (v == rlHotel)
        {

        }
        else if (v == rlConference)
        {
            startActivity(new Intent(PictureManageActivity.this, PicConferenceListActivity.class));

        }
    }
}
