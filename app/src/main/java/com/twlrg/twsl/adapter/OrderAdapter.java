package com.twlrg.twsl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.OrderInfo;
import com.twlrg.twsl.holder.OrderHolder;
import com.twlrg.twsl.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderHolder>
{

    private List<OrderInfo>     list;
    private Context             mContext;
    private MyItemClickListener listener;

    public OrderAdapter(List<OrderInfo> list, Context mContext, MyItemClickListener listener)
    {
        this.list = list;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        OrderHolder mHolder = new OrderHolder(itemView, listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(OrderHolder holder, int position)
    {
        OrderInfo mOrderInfo = list.get(position);
        holder.setOrderInfo(mOrderInfo, mContext, position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
