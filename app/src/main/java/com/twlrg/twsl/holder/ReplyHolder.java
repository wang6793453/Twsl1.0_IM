package com.twlrg.twsl.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.ReplyInfo;


/**
 * Date:
 */
public class ReplyHolder extends RecyclerView.ViewHolder
{
    private TextView mUserNameTv;
    private TextView mContentTv;
    private TextView mTimeTv;

    public ReplyHolder(View rootView)
    {
        super(rootView);
        mUserNameTv = (TextView) rootView.findViewById(R.id.tv_user_name);
        mContentTv = (TextView) rootView.findViewById(R.id.tv_content);
        mTimeTv = (TextView) rootView.findViewById(R.id.tv_time);


    }


    public void setReplyInfo(ReplyInfo mReplyInfo)
    {
        mUserNameTv.setText(mReplyInfo.getNickname());
        mContentTv.setText(mReplyInfo.getContent());
        mTimeTv.setText(mReplyInfo.getCreate_time());

    }

}
