package com.twlrg.twsl.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.BillInfo;
import com.twlrg.twsl.entity.CityInfo;
import com.twlrg.twsl.listener.MyItemClickListener;


/**
 * Date:
 */
public class CityHolder extends RecyclerView.ViewHolder
{

    private LinearLayout mItemLayout;
    private TextView     mNameTv;

    private MyItemClickListener listener;

    public CityHolder(View rootView, MyItemClickListener listener)
    {
        super(rootView);
        mNameTv = (TextView) rootView.findViewById(R.id.tv_name);
        mItemLayout = (LinearLayout) rootView.findViewById(R.id.ll_item);
        this.listener = listener;
    }


    public void setCityInfo(CityInfo mCityInfo, final int p)
    {

        mNameTv.setText(mCityInfo.getName());

        mItemLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v, p);
            }
        });
    }

}
