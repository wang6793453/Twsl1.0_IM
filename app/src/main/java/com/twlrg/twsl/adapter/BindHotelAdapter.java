package com.twlrg.twsl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.BillInfo;
import com.twlrg.twsl.entity.HotelInfo;
import com.twlrg.twsl.holder.BillHolder;
import com.twlrg.twsl.holder.BindHotelHolder;
import com.twlrg.twsl.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class BindHotelAdapter extends RecyclerView.Adapter<BindHotelHolder>
{

    private List<HotelInfo>     list;
    private MyItemClickListener listener;

    public BindHotelAdapter(List<HotelInfo> list, MyItemClickListener listener)
    {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public BindHotelHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bind_hotel, parent, false);
        BindHotelHolder mHolder = new BindHotelHolder(itemView, listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(BindHotelHolder holder, int position)
    {
        HotelInfo mHotelInfo = list.get(position);
        holder.setHotelInfo(mHotelInfo, position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
