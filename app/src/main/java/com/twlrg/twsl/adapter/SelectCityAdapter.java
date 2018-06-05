package com.twlrg.twsl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.BillInfo;
import com.twlrg.twsl.entity.CityInfo;
import com.twlrg.twsl.holder.BillHolder;
import com.twlrg.twsl.holder.CityHolder;
import com.twlrg.twsl.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class SelectCityAdapter extends RecyclerView.Adapter<CityHolder>
{

    private List<CityInfo>      list;
    private MyItemClickListener listener;
    public SelectCityAdapter(List<CityInfo> list, MyItemClickListener listener)
    {
        this.list = list;
        this.listener =listener;
    }

    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selecte_city, parent, false);
        CityHolder mHolder = new CityHolder(itemView,listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(CityHolder holder, int position)
    {
        CityInfo mCityInfo = list.get(position);
        holder.setCityInfo(mCityInfo,position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
