package com.twlrg.twsl.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.CityInfo;
import com.twlrg.twsl.entity.HotelInfo;
import com.twlrg.twsl.listener.MyItemClickListener;


/**
 * Date:
 */
public class BindHotelHolder extends RecyclerView.ViewHolder
{

    private RelativeLayout      mItemLayout;
    private TextView            mNameTv;
    private ImageView           mSelectedIv;
    private MyItemClickListener listener;

    public BindHotelHolder(View rootView, MyItemClickListener listener)
    {
        super(rootView);
        mNameTv = (TextView) rootView.findViewById(R.id.tv_name);
        mSelectedIv = (ImageView) rootView.findViewById(R.id.iv_selected);
        mItemLayout = (RelativeLayout) rootView.findViewById(R.id.ll_item);
        this.listener = listener;
    }


    public void setHotelInfo(HotelInfo mHotelInfo, final int p)
    {


        if (mHotelInfo.isSelected())
        {
            mSelectedIv.setImageResource(R.drawable.ic_dx_on);
        }
        else
        {
            mSelectedIv.setImageResource(R.drawable.ic_dx_off);
        }
        mNameTv.setText(mHotelInfo.getTitle());
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
