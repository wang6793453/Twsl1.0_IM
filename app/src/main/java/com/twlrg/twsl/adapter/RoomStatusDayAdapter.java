package com.twlrg.twsl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.RoomDayInfo;
import com.twlrg.twsl.holder.RoomStatusDayHolder;
import com.twlrg.twsl.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class RoomStatusDayAdapter extends RecyclerView.Adapter<RoomStatusDayHolder>
{

    private List<RoomDayInfo>   list;
    private Context             mContext;
    private MyItemClickListener listener;

    public RoomStatusDayAdapter(List<RoomDayInfo> list, Context mContext, MyItemClickListener listener)
    {
        this.list = list;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public RoomStatusDayHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_status_day, parent, false);
        RoomStatusDayHolder mHolder = new RoomStatusDayHolder(itemView, listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(RoomStatusDayHolder holder, int position)
    {
        RoomDayInfo mRoomDayInfo = list.get(position);
        holder.setData(mRoomDayInfo, position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
