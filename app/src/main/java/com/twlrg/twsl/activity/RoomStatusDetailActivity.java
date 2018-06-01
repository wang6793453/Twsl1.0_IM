package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.RoomStatusMonthAdapter;
import com.twlrg.twsl.entity.RoomDayInfo;
import com.twlrg.twsl.entity.RoomMonthInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.ResultHandler;
import com.twlrg.twsl.json.RoomListHandler;
import com.twlrg.twsl.json.RoomMonthListHandler;
import com.twlrg.twsl.listener.MyOnClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.DialogUtils;
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
public class RoomStatusDetailActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView    mRecyclerView;

    private RoomStatusMonthAdapter mRoomStatusMonthAdapter;
    private List<RoomMonthInfo> monthInfoList = new ArrayList<>();


    private static final int REQUEST_SUCCESS = 0x01;
    public static final  int REQUEST_FAIL    = 0x02;
    public static final  int EDIT_ROOM_SUCCESS    = 0x03;

    private static final String GET_ROOM_lIST = "get_room_list";
    private static final String EDIT_ROOM_STATUS = "edit_room_status";
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
                    mRoomStatusMonthAdapter.notifyDataSetChanged();
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(RoomStatusDetailActivity.this, msg.obj.toString());

                    break;

                case EDIT_ROOM_SUCCESS:
                    loadData();
                    break;

            }
        }
    };


    @Override
    protected void initData()
    {


        //        RoomMonthInfo mRoomMonthInfo1 = new RoomMonthInfo();
        //        mRoomMonthInfo1.setYear(2018);
        //        mRoomMonthInfo1.setMonth(5);
        //
        //        List<RoomDayInfo> r1 = new ArrayList<>();
        //        //得到该月份的第一天
        //        Calendar calendar = Calendar.getInstance();
        //        calendar.set(Calendar.YEAR, mRoomMonthInfo1.getYear());          //指定年份
        //        calendar.set(Calendar.MONTH, mRoomMonthInfo1.getMonth() - 1);        //指定月份 Java月份从0开始算
        //        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //
        //        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);             //得到该月份第一天 是星期几
        //        for (int i = 0; i < dayOfWeek - 1; i++)
        //        {
        //            RoomDayInfo mRoomDayInfo = new RoomDayInfo();
        //            mRoomDayInfo.setYear(2018);
        //            mRoomDayInfo.setMonth(5);
        //            mRoomDayInfo.setDay(0);
        //            r1.add(mRoomDayInfo);
        //        }
        //        for (int i = 0; i < 31; i++)
        //        {
        //            RoomDayInfo mRoomDayInfo = new RoomDayInfo();
        //            mRoomDayInfo.setYear(2018);
        //            mRoomDayInfo.setMonth(5);
        //            mRoomDayInfo.setDay(i + 1);
        //            r1.add(mRoomDayInfo);
        //        }
        //
        //
        //        RoomMonthInfo mRoomMonthInfo2 = new RoomMonthInfo();
        //        mRoomMonthInfo2.setYear(2018);
        //        mRoomMonthInfo2.setMonth(6);
        //
        //        List<RoomDayInfo> r2 = new ArrayList<>();
        //        //得到该月份的第一天
        //        Calendar calendar1 = Calendar.getInstance();
        //        calendar.set(Calendar.YEAR, mRoomMonthInfo2.getYear());          //指定年份
        //        calendar1.set(Calendar.MONTH, mRoomMonthInfo2.getMonth() - 1);        //指定月份 Java月份从0开始算
        //        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        //
        //        int dayOfWeek1 = calendar1.get(Calendar.DAY_OF_WEEK);             //得到该月份第一天 是星期几
        //        for (int i = 0; i < dayOfWeek1 - 1; i++)
        //        {
        //            RoomDayInfo mRoomDayInfo = new RoomDayInfo();
        //            mRoomDayInfo.setYear(2018);
        //            mRoomDayInfo.setMonth(6);
        //            mRoomDayInfo.setDay(0);
        //            r2.add(mRoomDayInfo);
        //        }
        //        for (int i = 0; i < 30; i++)
        //        {
        //            RoomDayInfo mRoomDayInfo = new RoomDayInfo();
        //            mRoomDayInfo.setYear(2018);
        //            mRoomDayInfo.setMonth(6);
        //            mRoomDayInfo.setDay(i + 1);
        //            r2.add(mRoomDayInfo);
        //        }


        //                mRoomMonthInfo1.setRoomDayInfoList(r1);
        //                mRoomMonthInfo2.setRoomDayInfoList(r2);
        //                monthInfoList.add(mRoomMonthInfo1);
        //                monthInfoList.add(mRoomMonthInfo2);

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_room_status_detail);
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
        setStatusBarTextDeep(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("房态维护");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(RoomStatusDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new EmptyDecoration(RoomStatusDetailActivity.this, ""));
        mRoomStatusMonthAdapter = new RoomStatusMonthAdapter(monthInfoList, RoomStatusDetailActivity.this, new MyOnClickListener.OnClickCallBackListener()
        {
            @Override
            public void onSubmit(int p, int n)
            {
                //                if (monthInfoList.get(p).getRoomDayInfoList().get(n).getStatus() == 1)
                //                {
                //                    monthInfoList.get(p).getRoomDayInfoList().get(n).setStatus(0);
                //                }
                //                else
                //                {
                //                    monthInfoList.get(p).getRoomDayInfoList().get(n).setStatus(1);
                //                }
                // mRoomStatusMonthAdapter.notifyDataSetChanged();

                final int status = monthInfoList.get(p).getRoomDayInfoList().get(n).getStatus();
                final String id = monthInfoList.get(p).getRoomDayInfoList().get(n).getId();
                final String date = monthInfoList.get(p).getRoomDayInfoList().get(n).getDate();
                String statusTitle;
                if (status == 1)
                {
                    statusTitle = "是否执行满房操作";
                }
                else
                {
                    statusTitle = "是否执行房间可预订操作";
                }

                DialogUtils.showToastDialog2Button(RoomStatusDetailActivity.this, statusTitle, new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        showProgressDialog();
                        Map<String, String> valuePairs = new HashMap<>();
                        valuePairs.put("token", ConfigManager.instance().getToken());
                        valuePairs.put("uid", ConfigManager.instance().getUserID());
                        valuePairs.put("id", id);
                        valuePairs.put("date", date);
                        valuePairs.put("city_value", ConfigManager.instance().getCityValue());

                        if (status == 1)
                        {
                            valuePairs.put("status", "0");
                        }
                        else
                        {
                            valuePairs.put("status", "1");
                        }

                        DataRequest.instance().request(RoomStatusDetailActivity.this, Urls.getEditRoomStatusUrl(), RoomStatusDetailActivity.this,
                                HttpRequest.POST, EDIT_ROOM_STATUS,
                                valuePairs,
                                new ResultHandler());
                    }
                });
            }
        });

        mRecyclerView.setAdapter(mRoomStatusMonthAdapter);

        loadData();

    }


    private void loadData()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("token", ConfigManager.instance().getToken());
        valuePairs.put("uid", ConfigManager.instance().getUserID());
        valuePairs.put("merchant_id", ConfigManager.instance().getMerchantId());
        valuePairs.put("room_id", getIntent().getStringExtra("ROOM_ID"));
        valuePairs.put("city_value", ConfigManager.instance().getCityValue());
        DataRequest.instance().request(RoomStatusDetailActivity.this, Urls.getRoomStatusAndPriceUrl(), this, HttpRequest.POST, GET_ROOM_lIST, valuePairs,
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
        else if(EDIT_ROOM_STATUS.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(EDIT_ROOM_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }
}
