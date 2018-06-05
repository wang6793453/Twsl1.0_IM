package com.twlrg.twsl.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.BillInfo;
import com.twlrg.twsl.listener.MyItemClickListener;


/**
 * Date:
 */
public class BillHolder extends RecyclerView.ViewHolder
{

    private RelativeLayout mItemLayout;
    private TextView       mTotalIncomeTv;
    private TextView       mStatusTv;
    private TextView       mTimeTv;

    private MyItemClickListener listener;

    public BillHolder(View rootView, MyItemClickListener listener)
    {
        super(rootView);
        mTotalIncomeTv = (TextView) rootView.findViewById(R.id.tv_total_income);
        mTimeTv = (TextView) rootView.findViewById(R.id.tv_time);
        mItemLayout = (RelativeLayout) rootView.findViewById(R.id.ll_item);
        this.listener = listener;
    }


    public void setBillInfo(BillInfo mBillInfo, final int p)
    {
        mTimeTv.setText(mBillInfo.getStartDate() + "  至  " + mBillInfo.getEndDate());

        mTotalIncomeTv.setText("¥ " + mBillInfo.getTotalIncome());

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
