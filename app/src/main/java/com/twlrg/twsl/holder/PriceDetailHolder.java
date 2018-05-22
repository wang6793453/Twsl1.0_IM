package com.twlrg.twsl.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.OrderInfo;


/**
 * Date:
 */
public class PriceDetailHolder extends RecyclerView.ViewHolder
{


    private TextView mDateTv;
    private TextView mPriceTv;


    public PriceDetailHolder(View rootView)
    {
        super(rootView);
        mDateTv = (TextView) rootView.findViewById(R.id.tv_date);
        mPriceTv = (TextView) rootView.findViewById(R.id.tv_price);

    }


    public void setOrderInfo(OrderInfo mOrderInfo)
    {
        mDateTv.setText(mOrderInfo.getDate());
        mPriceTv.setText(mOrderInfo.getPrice() + " x " + mOrderInfo.getBuynum());
    }

}
