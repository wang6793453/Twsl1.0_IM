package com.twlrg.twsl.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.RoomDayInfo;
import com.twlrg.twsl.listener.MyItemClickListener;


/**
 *
 */
public class RoomPriceDayHolder extends RecyclerView.ViewHolder
{


    private LinearLayout mItemLayout;
    private TextView     mDayTv;
    private TextView     mWzPriceTv;
    private TextView     mDzPriceTv;
    private TextView     mSzPriceTv;

    private MyItemClickListener myItemClickListener;

    public RoomPriceDayHolder(View itemView, MyItemClickListener myItemClickListener)
    {
        super(itemView);
        mDayTv = (TextView) itemView.findViewById(R.id.tv_day);
        mWzPriceTv = (TextView) itemView.findViewById(R.id.tv_wz);
        mDzPriceTv = (TextView) itemView.findViewById(R.id.tv_dz);
        mSzPriceTv = (TextView) itemView.findViewById(R.id.tv_sz);
        mItemLayout = (LinearLayout) itemView.findViewById(R.id.ll_item);
        this.myItemClickListener = myItemClickListener;
    }


    public void setData(RoomDayInfo mRoomDayInfo, final int p)
    {

        if (mRoomDayInfo.getDay() == 0)
        {
            mDayTv.setEnabled(false);
            mItemLayout.setEnabled(false);
            mDayTv.setText("");
            mWzPriceTv.setText("");
            mDzPriceTv.setText("");
            mSzPriceTv.setText("");
        }
        else
        {
            mDayTv.setText(mRoomDayInfo.getDay() + "");
            mWzPriceTv.setText("无  " + mRoomDayInfo.getWz_price());
            mDzPriceTv.setText("单  " + mRoomDayInfo.getDz_price());
            mSzPriceTv.setText("双  " + mRoomDayInfo.getSz_price());
            if (mRoomDayInfo.getStatus() == 1)
            {
                mDayTv.setSelected(true);
                mItemLayout.setSelected(true);
            }
            else
            {
                mDayTv.setSelected(false);
                mItemLayout.setSelected(false);
            }

            mItemLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    myItemClickListener.onItemClick(v, p);
                }
            });
        }


    }
}
