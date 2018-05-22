package com.twlrg.twsl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.ConferenceInfo;
import com.twlrg.twsl.holder.ConferenceManageHolder;
import com.twlrg.twsl.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class ConferenceManageAdapter extends RecyclerView.Adapter<ConferenceManageHolder>
{

    private List<ConferenceInfo> list;
    private MyItemClickListener  listener;
    public ConferenceManageAdapter(List<ConferenceInfo> list, MyItemClickListener listener)
    {
        this.list = list;
        this.listener =listener;
    }

    @Override
    public ConferenceManageHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conference_manage, parent, false);
        ConferenceManageHolder mHolder = new ConferenceManageHolder(itemView,listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(ConferenceManageHolder holder, int position)
    {
        ConferenceInfo mConferenceInfo = list.get(position);
        holder.setConferenceInfo(mConferenceInfo,position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
