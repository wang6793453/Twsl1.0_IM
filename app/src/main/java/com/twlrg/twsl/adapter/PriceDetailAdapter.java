package com.twlrg.twsl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.OrderInfo;
import com.twlrg.twsl.holder.PriceDetailHolder;

import java.util.List;

/**
 */
public class PriceDetailAdapter extends RecyclerView.Adapter<PriceDetailHolder>
{

    private List<OrderInfo> list;

    public PriceDetailAdapter(List<OrderInfo> list)
    {
        this.list = list;
    }

    @Override
    public PriceDetailHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_price_detail, parent, false);
        PriceDetailHolder mHolder = new PriceDetailHolder(itemView);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(PriceDetailHolder holder, int position)
    {
        OrderInfo mOrderInfo = list.get(position);
        holder.setOrderInfo(mOrderInfo);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
