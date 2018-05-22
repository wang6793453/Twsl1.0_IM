package com.twlrg.twsl.holder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.RoomStatusDayAdapter;
import com.twlrg.twsl.entity.RoomMonthInfo;
import com.twlrg.twsl.listener.MyItemClickListener;

/**
 *
 */
public class RoomStatusMonthHolder extends RecyclerView.ViewHolder
{
    private RecyclerView mRecyclerView;
    private TextView     mMonthTv;
    private Context      context;

    public RoomStatusMonthHolder(View itemView, Context context)
    {
        super(itemView);
        this.context = context;
        mMonthTv = (TextView) itemView.findViewById(R.id.tv_month);
        mRecyclerView = (RecyclerView) itemView.findViewById(R.id.day_recyclerView);
        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(context,
                        7,  // 每行显示item项数目
                        GridLayoutManager.VERTICAL, //水平排列
                        false
                );

        mRecyclerView.setLayoutManager(layoutManager);

    }


    public void setData(RoomMonthInfo mRoomMonthInfo, MyItemClickListener listener)
    {
        mMonthTv.setText(mRoomMonthInfo.getYear() + "年" + mRoomMonthInfo.getMonth() + "月");
        mRecyclerView.setAdapter(new RoomStatusDayAdapter(mRoomMonthInfo.getRoomDayInfoList(), context, listener));
    }

}
