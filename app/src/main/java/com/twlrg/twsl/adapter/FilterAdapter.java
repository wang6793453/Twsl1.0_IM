package com.twlrg.twsl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.FilterInfo;

import java.util.ArrayList;
import java.util.List;


public class FilterAdapter extends BaseAdapter
{
    private List<FilterInfo> infos = new ArrayList<>();

    private Context mContext;

    public FilterAdapter(List<FilterInfo> infos, Context mContext)
    {
        this.infos = infos;
        this.mContext = mContext;
    }

    @Override
    public int getCount()
    {
        return infos.size();
    }

    @Override
    public Object getItem(int position)
    {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pop_filter, null);
            holder = new ViewHolder();
            holder.mNameTv = (TextView) convertView.findViewById(R.id.tv_title);
            holder.mSelectedIv = (ImageView) convertView.findViewById(R.id.iv_selected);
            convertView.setTag(holder);

        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        FilterInfo mFilterInfo = infos.get(position);
        holder.mNameTv.setText(mFilterInfo.getTitle());

        if (mFilterInfo.isSelected())
        {
            holder.mSelectedIv.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.mSelectedIv.setVisibility(View.GONE);
        }

        return convertView;
    }


    private class ViewHolder
    {
        TextView  mNameTv;
        ImageView mSelectedIv;
    }
}
