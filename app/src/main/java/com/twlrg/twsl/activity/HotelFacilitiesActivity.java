package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.FacilitiesAdapter;
import com.twlrg.twsl.adapter.FacilitiesAdapter1;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.FacilitiesHandler;
import com.twlrg.twsl.json.HotelFacilitiesHandler;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;
import com.twlrg.twsl.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/23 21:32
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class HotelFacilitiesActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.topView)
    View             topView;
    @BindView(R.id.iv_back)
    ImageView        ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView  tvTitle;
    @BindView(R.id.gv_hotel)
    NoScrollGridView gvHotel;
    @BindView(R.id.gv_activity)
    NoScrollGridView gvActivity;
    @BindView(R.id.gv_room)
    NoScrollGridView gvRoom;
    @BindView(R.id.gv_conferenc)
    NoScrollGridView gvConferenc;
    @BindView(R.id.gv_dining)
    NoScrollGridView gvDining;

    private String merchant_id;


    FacilitiesAdapter mFacilitiesAdapter1;
    FacilitiesAdapter mFacilitiesAdapter2;
    FacilitiesAdapter mFacilitiesAdapter3;
    FacilitiesAdapter mFacilitiesAdapter4;
    FacilitiesAdapter mFacilitiesAdapter5;
    private List<String> stringList1 = new ArrayList<>();
    private List<String> stringList2 = new ArrayList<>();
    private List<String> stringList3 = new ArrayList<>();
    private List<String> stringList4 = new ArrayList<>();
    private List<String> stringList5 = new ArrayList<>();

    private static final int    REQUEST_LOGIN_SUCCESS = 0x01;
    public static final  int    REQUEST_FAIL          = 0x02;
    private static final String ADD_COMMENT           = "add_comment";

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {


                case REQUEST_LOGIN_SUCCESS:
                    HotelFacilitiesHandler mFacilitiesHandler = (HotelFacilitiesHandler)msg.obj;
                    stringList1.addAll(mFacilitiesHandler.getHotelFacilities());
                    mFacilitiesAdapter1 = new FacilitiesAdapter(stringList1, HotelFacilitiesActivity.this);
                    setListViewHeightBasedOnChildren(gvHotel);
                    gvHotel.setAdapter(mFacilitiesAdapter1);

                    stringList2.addAll(mFacilitiesHandler.getActivityFacilities());
                    mFacilitiesAdapter2 = new FacilitiesAdapter(stringList2, HotelFacilitiesActivity.this);
                    setListViewHeightBasedOnChildren(gvActivity);
                    gvActivity.setAdapter(mFacilitiesAdapter2);

                    stringList3.addAll(mFacilitiesHandler.getRoomFacilities());
                    mFacilitiesAdapter3 = new FacilitiesAdapter(stringList3, HotelFacilitiesActivity.this);
                    setListViewHeightBasedOnChildren(gvRoom);
                    gvRoom.setAdapter(mFacilitiesAdapter3);

                    stringList4.addAll(mFacilitiesHandler.getConferenceFacilities());
                    mFacilitiesAdapter4 = new FacilitiesAdapter(stringList4, HotelFacilitiesActivity.this);
                    setListViewHeightBasedOnChildren(gvConferenc);
                    gvConferenc.setAdapter(mFacilitiesAdapter4);

                    stringList5.addAll(mFacilitiesHandler.getDiningFacilities());
                    mFacilitiesAdapter5 = new FacilitiesAdapter(stringList5, HotelFacilitiesActivity.this);
                    setListViewHeightBasedOnChildren(gvDining);
                    gvDining.setAdapter(mFacilitiesAdapter5);
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(HotelFacilitiesActivity.this, msg.obj.toString());
                    break;


            }
        }
    };

    @Override
    protected void initData()
    {
        merchant_id = getIntent().getStringExtra("MERCHANT_ID");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_hotel_facilities);
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
        tvTitle.setText("酒店设施");


        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("merchant_id", merchant_id);
        DataRequest.instance().request(HotelFacilitiesActivity.this, Urls.getFacilitiesUrl(), this, HttpRequest.POST, ADD_COMMENT, valuePairs,
                new HotelFacilitiesHandler());
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if(v == ivBack)
        {
            finish();
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (ADD_COMMENT.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_LOGIN_SUCCESS, obj));
            }

            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }

    public static void setListViewHeightBasedOnChildren(GridView listView)
    {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return;
        }
        // 固定列宽，有多少列
        int col = 3;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col)
        {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight() + 30;
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
        ((ViewGroup.MarginLayoutParams) params).setMargins(30, 10, 30, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }

}
