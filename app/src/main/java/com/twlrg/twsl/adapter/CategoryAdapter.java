package com.twlrg.twsl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.twlrg.twsl.R;

import java.util.List;

/**
 * 作者：王先云 on 2018/4/19 23:16
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class CategoryAdapter extends BaseAdapter
{

    private Context      mContext;
    private List<String> list;

    public CategoryAdapter(Context mContext, List<String> list)
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
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category, null);
            holder = new ViewHolder();
            holder.mNameTv = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);

        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mNameTv.setText(list.get(position));

        return convertView;
    }


    private class ViewHolder
    {
        TextView mNameTv;
    }
}