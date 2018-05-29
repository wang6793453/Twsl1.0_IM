package com.twlrg.twsl.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.FacilitiesInfo;
import com.twlrg.twsl.listener.MyItemClickListener;


/**
 * Date:
 */
public class EditFacilitiesHolder extends RecyclerView.ViewHolder
{

    private TextView  mTitleTv;
    private ImageView mSelectedIv;

    private MyItemClickListener listener;

    public EditFacilitiesHolder(View rootView,MyItemClickListener listener)
    {
        super(rootView);
        mTitleTv = (TextView) rootView.findViewById(R.id.tv_name);
        mSelectedIv = (ImageView)rootView.findViewById(R.id.iv_selected);
        this.listener = listener;
    }

    public void setFacilities(FacilitiesInfo mFacilitiesInfo,final int p)
    {
        mTitleTv.setText(mFacilitiesInfo.getName());

        if(mFacilitiesInfo.getIsSelected()==1)
        {
            mSelectedIv.setSelected(true);
        }
        else
        {
            mSelectedIv.setSelected(false);
        }



        mSelectedIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v,p);
            }
        });
    }

}
