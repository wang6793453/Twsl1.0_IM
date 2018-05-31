package com.twlrg.twsl.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.RoomDayInfo;
import com.twlrg.twsl.listener.MyItemClickListener;


/**
 *
 */
public class RoomStatusDayHolder extends RecyclerView.ViewHolder
{

    public TextView mDayTv;      //日期文本

    private MyItemClickListener myItemClickListener;

    public RoomStatusDayHolder(View itemView, MyItemClickListener myItemClickListener)
    {
        super(itemView);
        mDayTv = (TextView) itemView.findViewById(R.id.tv_day);
        this.myItemClickListener = myItemClickListener;
    }


    public void setData(RoomDayInfo mRoomDayInfo, final int p)
    {

        if (mRoomDayInfo.getDay() == 0)
        {
            mDayTv.setEnabled(false);
            mDayTv.setText( "");
        }
        else
        {
            mDayTv.setText(mRoomDayInfo.getDay() + "");
            if (mRoomDayInfo.getStatus() == 1)
            {
                mDayTv.setSelected(false);
            }
            else
            {
                mDayTv.setSelected(true);

            }

            mDayTv.setOnClickListener(new View.OnClickListener()
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
