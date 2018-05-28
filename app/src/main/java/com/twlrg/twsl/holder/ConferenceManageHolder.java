package com.twlrg.twsl.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.ConferenceInfo;
import com.twlrg.twsl.listener.MyItemClickListener;


/**
 * Date:
 */
public class ConferenceManageHolder extends RecyclerView.ViewHolder
{

    private RelativeLayout mItemLayout;
    private TextView       mRoomTitleTv;

    private MyItemClickListener listener;

    public ConferenceManageHolder(View rootView, MyItemClickListener listener)
    {
        super(rootView);
        mRoomTitleTv = (TextView) rootView.findViewById(R.id.tv_room_title);
        mItemLayout = (RelativeLayout) rootView.findViewById(R.id.ll_item);
        this.listener = listener;
    }


    public void setConferenceInfo(ConferenceInfo mConferenceInfo, final int p)
    {
        mRoomTitleTv.setText(mConferenceInfo.getTitle());
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
