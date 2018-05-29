package com.twlrg.twsl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.FacilitiesInfo;
import com.twlrg.twsl.holder.EditFacilitiesHolder;
import com.twlrg.twsl.holder.FacilitiesHolder;
import com.twlrg.twsl.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class FacilitiesAdapter1 extends RecyclerView.Adapter<EditFacilitiesHolder>
{

    private List<FacilitiesInfo> list;

    private MyItemClickListener listener;
    public FacilitiesAdapter1(List<FacilitiesInfo> list,MyItemClickListener listener)
    {
        this.list = list;
        this.listener=listener;
    }

    @Override
    public EditFacilitiesHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_facilites, parent, false);
        EditFacilitiesHolder mHolder = new EditFacilitiesHolder(itemView,listener);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(EditFacilitiesHolder holder, int position)
    {
        holder.setFacilities(list.get(position),position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}