package com.twlrg.twsl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.holder.FacilitiesHolder;

import java.util.List;

/**
 */
public class FacilitiesAdapter extends BaseAdapter
{
    private List<String> list;
    private Context      mContext;

    public FacilitiesAdapter(List<String> list, Context mContext)
    {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_facilities, null);
            holder = new ViewHolder();
            holder.mTitleTv = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);

        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTitleTv.setText(list.get(position));

        return convertView;

    }


    private class ViewHolder
    {
        TextView mTitleTv;
    }

}
