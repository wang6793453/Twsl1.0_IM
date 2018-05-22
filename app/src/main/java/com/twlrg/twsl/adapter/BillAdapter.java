package com.twlrg.twsl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.BillInfo;
import com.twlrg.twsl.holder.BillHolder;
import com.twlrg.twsl.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class BillAdapter extends RecyclerView.Adapter<BillHolder>
{

    private List<BillInfo>      list;
    private MyItemClickListener listener;
    public BillAdapter(List<BillInfo> list, MyItemClickListener listener)
    {
        this.list = list;
        this.listener =listener;
    }

    @Override
    public BillHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        BillHolder mHolder = new BillHolder(itemView,listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(BillHolder holder, int position)
    {
        BillInfo mBillInfo = list.get(position);
        holder.setBillInfo(mBillInfo,position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
