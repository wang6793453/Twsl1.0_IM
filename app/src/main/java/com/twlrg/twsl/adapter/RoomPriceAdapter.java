package com.twlrg.twsl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.RoomInfo;
import com.twlrg.twsl.holder.RoomPriceHolder;
import com.twlrg.twsl.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class RoomPriceAdapter extends RecyclerView.Adapter<RoomPriceHolder>
{

    private List<RoomInfo>      list;
    private Context             mContext;
    private MyItemClickListener listener;

    public RoomPriceAdapter(List<RoomInfo> list, Context mContext, MyItemClickListener listener)
    {
        this.list = list;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public RoomPriceHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_price, parent, false);
        RoomPriceHolder mHolder = new RoomPriceHolder(itemView, listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(RoomPriceHolder holder, int position)
    {
        RoomInfo mRoomInfo = list.get(position);
        holder.setData(mRoomInfo, position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
