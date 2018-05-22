package com.twlrg.twsl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.HotelInfo;
import com.twlrg.twsl.holder.HotelHolder;
import com.twlrg.twsl.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class HotelAdapter extends RecyclerView.Adapter<HotelHolder>
{

    private List<HotelInfo>     list;
    private Context             mContext;
    private MyItemClickListener listener;

    public HotelAdapter(List<HotelInfo> list, Context mContext, MyItemClickListener listener)
    {
        this.list = list;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public HotelHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel, parent, false);
        HotelHolder mHolder = new HotelHolder(itemView, listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(HotelHolder holder, int position)
    {
        HotelInfo mHotelInfo = list.get(position);
        holder.setHotelInfo(mHotelInfo, mContext, position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
