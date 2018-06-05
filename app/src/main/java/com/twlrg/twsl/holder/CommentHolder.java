package com.twlrg.twsl.holder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.twlrg.twsl.R;
import com.twlrg.twsl.activity.CommentListActivity;
import com.twlrg.twsl.adapter.ReplyAdapter;
import com.twlrg.twsl.entity.CommentInfo;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.DividerDecoration;
import com.twlrg.twsl.widget.EmptyDecoration;
import com.twlrg.twsl.widget.FullyLinearLayoutManager;


/**
 * Date:
 */
public class CommentHolder extends RecyclerView.ViewHolder
{
    private TextView mReplyTv;

    private ImageView           mUserPicIv;
    private RecyclerView        mRecyclerView;
    private TextView            mUserNameTv;
    private TextView            mContentTv;
    private TextView            mTimeTv;
    private View            mLine;
    private MyItemClickListener listener;

    public CommentHolder(View rootView, Context mContext, MyItemClickListener listener)
    {
        super(rootView);
        mReplyTv = (TextView) rootView.findViewById(R.id.tv_reply);
        mUserNameTv = (TextView) rootView.findViewById(R.id.tv_user_name);
        mContentTv = (TextView) rootView.findViewById(R.id.tv_content);
        mTimeTv = (TextView) rootView.findViewById(R.id.tv_time);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_reply);
        mUserPicIv = (ImageView) rootView.findViewById(R.id.iv_user_head);
        mLine= rootView.findViewById(R.id.line);
        mRecyclerView.setLayoutManager(new FullyLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new EmptyDecoration(mContext, ""));
        this.listener = listener;

    }


    public void setCommentInfo(CommentInfo mCommentInfo, final int p)
    {
        ImageLoader.getInstance().displayImage(Urls.getImgUrl(mCommentInfo.getPortrait()), mUserPicIv);
        mUserNameTv.setText(mCommentInfo.getNickname());
        mContentTv.setText(mCommentInfo.getContent());
        mTimeTv.setText(mCommentInfo.getCreate_time());

        mReplyTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v, p);
            }
        });

        if (mCommentInfo.getReplyInfoList().isEmpty())
        {
            mReplyTv.setVisibility(View.VISIBLE);
            mLine.setVisibility(View.GONE);
        }
        else
        {
            mReplyTv.setVisibility(View.GONE);
            mLine.setVisibility(View.VISIBLE);
        }


        mRecyclerView.setAdapter(new ReplyAdapter(mCommentInfo.getReplyInfoList()));
    }

}
