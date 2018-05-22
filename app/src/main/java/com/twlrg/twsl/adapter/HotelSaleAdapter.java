package com.twlrg.twsl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.SaleInfo;
import com.twlrg.twsl.holder.HotelSaleHolder;

import java.util.List;

/**
 */
public class HotelSaleAdapter extends RecyclerView.Adapter<HotelSaleHolder>
{

    private List<SaleInfo> list;

    private Context mContext;

    public HotelSaleAdapter(List<SaleInfo> list, Context mContext)
    {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public HotelSaleHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel_sale, parent, false);
        HotelSaleHolder mHolder = new HotelSaleHolder(itemView, mContext);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(HotelSaleHolder holder, int position)
    {
        SaleInfo mSaleInfo = list.get(position);
        holder.setSaleInfo(mSaleInfo);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
