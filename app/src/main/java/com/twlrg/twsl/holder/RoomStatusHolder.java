package com.twlrg.twsl.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.RoomInfo;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.StringUtils;


/**
 *
 */
public class RoomStatusHolder extends RecyclerView.ViewHolder
{

    private RelativeLayout      mItemLayout;
    private TextView            mRoomTitleTv;
    private TextView            mDateTv;
    private MyItemClickListener mItemClickListener;

    public RoomStatusHolder(View itemView, MyItemClickListener mItemClickListener)
    {
        super(itemView);

        mItemLayout = (RelativeLayout) itemView.findViewById(R.id.rl_item);
        mRoomTitleTv = (TextView) itemView.findViewById(R.id.tv_room_title);
        mDateTv = (TextView) itemView.findViewById(R.id.tv_date);
        this.mItemClickListener = mItemClickListener;

    }


    public void setData(RoomInfo mRoomInfo, final int p)
    {
        mRoomTitleTv.setText(mRoomInfo.getTitle());
        mDateTv.setText(StringUtils.getCurrentTime());
        mItemLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mItemClickListener.onItemClick(v, p);
            }
        });
    }

}
