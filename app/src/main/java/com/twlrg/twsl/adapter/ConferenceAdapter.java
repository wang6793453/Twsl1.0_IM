package com.twlrg.twsl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.ConferenceInfo;
import com.twlrg.twsl.holder.ConferenceHolder;
import com.twlrg.twsl.listener.MyItemClickListener;

import java.util.List;

/**
 * 会议室
 */
public class ConferenceAdapter extends RecyclerView.Adapter<ConferenceHolder>
{

    private List<ConferenceInfo> list;
    private Context              mContext;
    private MyItemClickListener  listener;

    public ConferenceAdapter(List<ConferenceInfo> list, Context mContext)
    {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public ConferenceHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conference, parent, false);
        ConferenceHolder mHolder = new ConferenceHolder(itemView);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(ConferenceHolder holder, int position)
    {
        ConferenceInfo mHotelInfo = list.get(position);
        holder.setConferenceInfo(mHotelInfo, mContext);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
