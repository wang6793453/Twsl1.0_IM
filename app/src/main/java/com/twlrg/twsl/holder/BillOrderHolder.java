package com.twlrg.twsl.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.OrderInfo;
import com.twlrg.twsl.listener.MyItemClickListener;


/**
 * Date:
 */
public class BillOrderHolder extends RecyclerView.ViewHolder
{

    private RelativeLayout      mItemLayout;
    private TextView            mTotalPriceTv;
    private TextView            mNameTv;
    private TextView            mTimeTv;
    private TextView            mBillTv;
    private TextView            mRoomTitleTv;
    private MyItemClickListener listener;

    public BillOrderHolder(View rootView, MyItemClickListener listener)
    {
        super(rootView);
        mTotalPriceTv = (TextView) rootView.findViewById(R.id.tv_total_price);
        mNameTv = (TextView) rootView.findViewById(R.id.tv_name);
        mTimeTv = (TextView) rootView.findViewById(R.id.tv_time);
        mBillTv= (TextView) rootView.findViewById(R.id.tv_bill);
        mRoomTitleTv = (TextView) rootView.findViewById(R.id.tv_room_title);
        mItemLayout = (RelativeLayout) rootView.findViewById(R.id.ll_item);
        this.listener = listener;
    }


    public void setOrderInfo(OrderInfo mOrderInfo, final int p)
    {
        String price_type = mOrderInfo.getPrice_type();
        String zc = "无早";
        if ("wz".equals(price_type))
        {
            zc = "无早";
        }
        else if ("dz".equals(price_type))
        {
            zc = "单早";
        }
        else if ("sz".equals(price_type))
        {
            zc = "双早";
        }
        mBillTv.setText("1".equals(mOrderInfo.getBill())?"已结算":"未结算");
        mRoomTitleTv.setText(mOrderInfo.getTitle() + "[" + zc + "]");
        mTotalPriceTv.setText("¥ " + mOrderInfo.getTotal_fee());
        mTimeTv.setText(mOrderInfo.getCheck_in() +"  至  "+mOrderInfo.getCheck_out());
        mNameTv.setText("入住人:"+mOrderInfo.getName());

        mItemLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v, p);
            }
        });
    }

}
