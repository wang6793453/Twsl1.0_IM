package com.twlrg.twsl.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.OrderInfo;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.widget.AutoFitTextView;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/5/15 23:15
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class BillDetailActivity extends BaseActivity
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.tv_hotel_name)
    AutoFitTextView tvHotelName;
    @BindView(R.id.tv_room_title)
    AutoFitTextView tvRoomTitle;
    @BindView(R.id.tv_order_code)
    TextView        tvOrderCode;
    @BindView(R.id.tv_check_in)
    TextView        tvCheckIn;
    @BindView(R.id.tv_days)
    TextView        tvDays;
    @BindView(R.id.tv_name)
    TextView        tvName;
    @BindView(R.id.tv_room_price)
    TextView        tvRoomPrice;
    @BindView(R.id.tv_service_price)
    TextView        tvServicePrice;
    @BindView(R.id.tv_total_price)
    TextView        tvTotalPrice;

    private OrderInfo mOrderInfo;

    @Override
    protected void initData()
    {
        mOrderInfo = (OrderInfo) getIntent().getSerializableExtra("ORDER_INFO");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_bill_detail);
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
        tvTitle.setText("订单详情");
        setStatusBarTextDeep(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));

        if (null != mOrderInfo)
        {
            tvHotelName.setText("");
            tvRoomTitle.setText(mOrderInfo.getTitle());
            tvOrderCode.setText(mOrderInfo.getOrdcode());
            tvCheckIn.setText(mOrderInfo.getCheck_in());
            tvDays.setText(mOrderInfo.getBuynum() + "间   " + mOrderInfo.getDays() + "晚");
            tvName.setText(mOrderInfo.getName());
            tvRoomPrice.setText("¥ " + mOrderInfo.getTotal_fee());
            tvServicePrice.setText("¥ 0");
            tvTotalPrice.setText("¥ " + mOrderInfo.getTotal_fee());
        }

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
