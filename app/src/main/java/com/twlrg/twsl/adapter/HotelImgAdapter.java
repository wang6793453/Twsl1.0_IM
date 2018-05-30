package com.twlrg.twsl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.BillInfo;
import com.twlrg.twsl.entity.HotelImgInfo;
import com.twlrg.twsl.holder.BillHolder;
import com.twlrg.twsl.holder.HotelImgHolder;
import com.twlrg.twsl.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class HotelImgAdapter extends RecyclerView.Adapter<HotelImgHolder>
{

    private List<HotelImgInfo>  list;
    private MyItemClickListener listener;
    private MyItemClickListener listener1;
    public HotelImgAdapter(List<HotelImgInfo> list, MyItemClickListener listener,MyItemClickListener listener1)
    {
        this.list = list;
        this.listener = listener;
        this.listener1=listener1;
    }

    @Override
    public HotelImgHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel_img, parent, false);
        HotelImgHolder mHolder = new HotelImgHolder(itemView, parent.getContext(), listener,listener1);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(HotelImgHolder holder, int position)
    {
        HotelImgInfo mHotelImgInfo = list.get(position);
        holder.setHotelImgInfo(mHotelImgInfo, position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
