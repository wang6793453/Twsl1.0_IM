package com.twlrg.twsl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.OrderInfo;
import com.twlrg.twsl.holder.BillOrderHolder;
import com.twlrg.twsl.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class BillOrderAdapter extends RecyclerView.Adapter<BillOrderHolder>
{

    private List<OrderInfo>     list;
    private MyItemClickListener listener;
    public BillOrderAdapter(List<OrderInfo> list, MyItemClickListener listener)
    {
        this.list = list;
        this.listener =listener;
    }

    @Override
    public BillOrderHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_bill, parent, false);
        BillOrderHolder mHolder = new BillOrderHolder(itemView,listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(BillOrderHolder holder, int position)
    {
        OrderInfo mOrderInfo = list.get(position);
        holder.setOrderInfo(mOrderInfo,position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
