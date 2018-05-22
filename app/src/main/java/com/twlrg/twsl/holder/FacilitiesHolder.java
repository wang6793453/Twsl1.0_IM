package com.twlrg.twsl.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.twlrg.twsl.R;


/**
 * Date:
 */
public class FacilitiesHolder extends RecyclerView.ViewHolder
{

    private TextView     mTitleTv;


    public FacilitiesHolder(View rootView)
    {
        super(rootView);
        mTitleTv = (TextView) rootView.findViewById(R.id.tv_title);
    }


    public void setFacilities(String name)
    {
        mTitleTv.setText(name);
    }

}
