package com.twlrg.twsl.holder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.activity.AddCommentActivity;
import com.twlrg.twsl.entity.OrderInfo;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.widget.AutoFitTextView;


/**
 * Date:
 */
public class OrderHolder extends RecyclerView.ViewHolder
{


    private AutoFitTextView mHotelNameTv;
    private TextView        mPriceTv;
    private TextView        mTitleTv;
    private TextView        mOrderStatusTv;
    private TextView        mNameTv;
    private TextView        mCreateTimeTv;
    private TextView        mEvaluateTv;
    private LinearLayout    mItemLayout;

    private MyItemClickListener listener1;

    public OrderHolder(View rootView, MyItemClickListener listener1)
    {
        super(rootView);
        mEvaluateTv = (TextView) rootView.findViewById(R.id.tv_evaluate);
        mHotelNameTv = (AutoFitTextView) rootView.findViewById(R.id.tv_merchant);
        mPriceTv = (TextView) rootView.findViewById(R.id.tv_total_fee);
        mTitleTv = (TextView) rootView.findViewById(R.id.tv_title);
        mNameTv = (TextView) rootView.findViewById(R.id.tv_name);
        mCreateTimeTv = (TextView) rootView.findViewById(R.id.tv_create_time);
        mOrderStatusTv = (TextView) rootView.findViewById(R.id.tv_payment_trade_status);
        mItemLayout = (LinearLayout) rootView.findViewById(R.id.ll_item);
        this.listener1 = listener1;
    }


    public void setOrderInfo(final OrderInfo mOrderInfo, final Context mContext, final int p)
    {
        mEvaluateTv.setVisibility(View.GONE);
        mHotelNameTv.setText(mOrderInfo.getMerchant());
        mPriceTv.setText("￥" + mOrderInfo.getTotal_fee());
        mTitleTv.setText(mOrderInfo.getTitle() + "  " + mOrderInfo.getBuynum() + "间  " + mOrderInfo.getDays() + "晚");
        mNameTv.setText(mOrderInfo.getName() + " " + mOrderInfo.getCheck_in() + "入住");
        mCreateTimeTv.setText(mOrderInfo.getCreate_time());
        String status = "待支付";


        switch (mOrderInfo.getIs_used())
        {
            case 0:

                if ("1".equals(mOrderInfo.getPayment_trade_status()))
                {
                    status = "待酒店确认";
                }
                else
                {
                    status = "待支付";
                }

                break;
            case 1:
                status = "已预订成功";
                mEvaluateTv.setVisibility(View.VISIBLE);
                break;
            case 2:
                status = "酒店满房拒单";
                break;
            case 3:
                status = "取消确认中";
                break;
            case 4:
                status = "已取消";
                break;
            case 5:
                status = "酒店拒绝取消";
                break;
        }


        mOrderStatusTv.setText(status);
        mItemLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener1.onItemClick(v, p);
            }
        });

        //跳转评价界面
        mEvaluateTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mContext.startActivity(new Intent(mContext, AddCommentActivity.class).putExtra("MERCHANT_ID", mOrderInfo.getMerchant_id()));
            }
        });
    }

}
