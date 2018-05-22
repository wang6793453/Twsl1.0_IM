package com.twlrg.twsl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.RoomMonthInfo;
import com.twlrg.twsl.holder.RoomStatusMonthHolder;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.listener.MyOnClickListener;

import java.util.List;

/**
 */
public class RoomStatusMonthAdapter extends RecyclerView.Adapter<RoomStatusMonthHolder>
{

    private List<RoomMonthInfo>                       monthInfoList;
    private Context                                   context;
    private MyOnClickListener.OnClickCallBackListener listener;

    public RoomStatusMonthAdapter(List<RoomMonthInfo> monthInfoList, Context context, MyOnClickListener.OnClickCallBackListener listener)
    {
        this.monthInfoList = monthInfoList;
        this.context = context;
        this.listener = listener;
    }


    @Override
    public RoomStatusMonthHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.item_room_status_month, parent, false);
        RoomStatusMonthHolder ret = new RoomStatusMonthHolder(v, context);
        return ret;
    }

    @Override
    public void onBindViewHolder(RoomStatusMonthHolder holder, final int position)
    {
        RoomMonthInfo mRoomMonthInfo = monthInfoList.get(position);
        holder.setData(mRoomMonthInfo, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int i)
            {
                listener.onSubmit(position, i);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        int ret = 0;
        if (monthInfoList != null)
        {
            ret = monthInfoList.size();
        }
        return ret;
    }
}
