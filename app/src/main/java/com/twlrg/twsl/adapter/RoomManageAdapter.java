package com.twlrg.twsl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.RoomInfo;
import com.twlrg.twsl.holder.RoomManageHolder;
import com.twlrg.twsl.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class RoomManageAdapter extends RecyclerView.Adapter<RoomManageHolder>
{

    private List<RoomInfo>      list;
    private MyItemClickListener listener;
    public RoomManageAdapter(List<RoomInfo> list, MyItemClickListener listener)
    {
        this.list = list;
        this.listener =listener;
    }

    @Override
    public RoomManageHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_manage, parent, false);
        RoomManageHolder mHolder = new RoomManageHolder(itemView,listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(RoomManageHolder holder, int position)
    {
        RoomInfo mRoomInfo = list.get(position);
        holder.setRoomInfo(mRoomInfo,position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
