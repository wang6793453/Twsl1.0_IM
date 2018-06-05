package com.twlrg.twsl.holder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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

    private Context context;

    public ReplyHolder(View rootView, Context context)
    {
        super(rootView);
        this.context = context;
        mUserNameTv = (TextView) rootView.findViewById(R.id.tv_user_name);
        mContentTv = (TextView) rootView.findViewById(R.id.tv_content);
        mTimeTv = (TextView) rootView.findViewById(R.id.tv_time);


    }


    public void setReplyInfo(ReplyInfo mReplyInfo)
    {
        mUserNameTv.setText(mReplyInfo.getNickname());
        mTimeTv.setText(mReplyInfo.getCreate_time());

        String content = "商家回应:" + mReplyInfo.getContent();


        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.blackB)), 0, 4, Spanned
                .SPAN_EXCLUSIVE_EXCLUSIVE);
        mContentTv.setText(spannableString);

    }

}
