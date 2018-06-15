package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.RoomPriceMonthAdapter;
import com.twlrg.twsl.entity.RoomDayInfo;
import com.twlrg.twsl.entity.RoomMonthInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.RoomMonthListHandler;
import com.twlrg.twsl.listener.MyOnClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.DialogUtils;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;
import com.twlrg.twsl.widget.EmptyDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/5/14 10:20
 * 邮箱：wangxianyun1@163.com
 * 描述：房状维护
 */
public class RoomPriceDetailActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView    mRecyclerView;
    @BindView(R.id.tv_submit)
    TextView        tvSubmit;


    private RoomPriceMonthAdapter mRoomPriceMonthAdapter;
    private List<RoomMonthInfo> monthInfoList = new ArrayList<>();

    private String roomId,roomName;
    private static final int REQUEST_SUCCESS = 0x01;
    public static final  int REQUEST_FAIL    = 0x02;

    private static final String GET_ROOM_lIST = "get_room_list";

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    RoomMonthListHandler mRoomMonthListHandler = (RoomMonthListHandler) msg.obj;
                    monthInfoList.clear();
                    monthInfoList.addAll(mRoomMonthListHandler.getMonthInfoList());
                    mRoomPriceMonthAdapter.notifyDataSetChanged();
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(RoomPriceDetailActivity.this, msg.obj.toString());

                    break;


            }
        }
    };


    @Override
    protected void initData()
    {
        roomId = getIntent().getStringExtra("ROOM_ID");
        roomName = getIntent().getStringExtra("ROOM_NAME");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_room_price_detail);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("房价维护");

        tvSubmit.setText("批量修改");
        tvSubmit.setVisibility(View.VISIBLE);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(RoomPriceDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new EmptyDecoration(RoomPriceDetailActivity.this, ""));
        mRoomPriceMonthAdapter = new RoomPriceMonthAdapter(monthInfoList, RoomPriceDetailActivity.this, new MyOnClickListener.OnClickCallBackListener()
        {
            @Override
            public void onSubmit(int p, int n)
            {
                RoomDayInfo mRoomDayInfo = monthInfoList.get(p).getRoomDayInfoList().get(n);

                String day = mRoomDayInfo.getYear() + "-" + mRoomDayInfo.getMonth() + "-" + mRoomDayInfo.getDay();
                //  showModifyPriceDialog(day, day);
                String id = mRoomDayInfo.getId();
                startActivity(new Intent(RoomPriceDetailActivity.this, SettingRoomPriceActivity.class)
                        .putExtra("S_DATE", day)
                        .putExtra("E_DATE", day)
                        .putExtra("ID", id)
                        .putExtra("ROOM_ID", mRoomDayInfo.getRoom_id())
                        .putExtra("ROOM_NAME", roomName)
                );
            }
        });

        mRecyclerView.setAdapter(mRoomPriceMonthAdapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadData();
    }

    private void loadData()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("token", ConfigManager.instance().getToken());
        valuePairs.put("uid", ConfigManager.instance().getUserID());
        valuePairs.put("merchant_id", ConfigManager.instance().getMerchantId());
        valuePairs.put("room_id", roomId);
        valuePairs.put("city_value", ConfigManager.instance().getCityValue());
        DataRequest.instance().request(RoomPriceDetailActivity.this, Urls.getRoomStatusAndPriceUrl(), this, HttpRequest.POST, GET_ROOM_lIST, valuePairs,
                new RoomMonthListHandler());
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        if (v == ivBack)
        {
            finish();
        }
        else if (v == tvSubmit)
        {
            startActivity(new Intent(RoomPriceDetailActivity.this, SettingRoomPriceActivity.class)
                    .putExtra("ROOM_ID", roomId)
                    .putExtra("ROOM_NAME", roomName)
            );
        }
    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_ROOM_lIST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }
}
