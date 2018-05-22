package com.twlrg.twsl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.RoomStatusAdapter;
import com.twlrg.twsl.entity.RoomInfo;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.widget.AutoFitTextView;
import com.twlrg.twsl.widget.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/5/14 10:20
 * 邮箱：wangxianyun1@163.com
 * 描述：房状维护
 */
public class RoomStatusListActivity extends BaseActivity
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView    mRecyclerView;

    private List<RoomInfo> roomInfoList = new ArrayList<>();
    private RoomStatusAdapter mRoomStatusAdapter;

    @Override
    protected void initData()
    {
        for (int i = 0; i < 10; i++)
        {
            RoomInfo mRoomInfo = new RoomInfo();
            roomInfoList.add(mRoomInfo);
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_room_status);
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
        tvTitle.setText("房态维护");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(RoomStatusListActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerDecoration(RoomStatusListActivity.this));
        mRoomStatusAdapter = new RoomStatusAdapter(roomInfoList, RoomStatusListActivity.this, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                startActivity(new Intent(RoomStatusListActivity.this, RoomStatusDetailActivity.class));
            }
        });


        mRecyclerView.setAdapter(mRoomStatusAdapter);
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
