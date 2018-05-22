package com.twlrg.twsl.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.CommentInfo;
import com.twlrg.twsl.utils.Urls;


/**
 * Date:
 */
public class CommentHolder extends RecyclerView.ViewHolder
{
    private ImageView mUserPicIv;


    private TextView mUserNameTv;
    private TextView mContentTv;
    private TextView mTimeTv;


    public CommentHolder(View rootView)
    {
        super(rootView);
        mUserPicIv = (ImageView) rootView.findViewById(R.id.iv_user_head);
        mUserNameTv = (TextView) rootView.findViewById(R.id.tv_user_name);
        mContentTv = (TextView) rootView.findViewById(R.id.tv_content);
        mTimeTv = (TextView) rootView.findViewById(R.id.tv_time);

    }


    public void setCommentInfo(CommentInfo mCommentInfo)
    {
        ImageLoader.getInstance().displayImage(Urls.getImgUrl(mCommentInfo.getPortrait()), mUserPicIv);
        mUserNameTv.setText(mCommentInfo.getNickname());
        mContentTv.setText(mCommentInfo.getContent());
        mTimeTv.setText(mCommentInfo.getCreate_time());
    }

}
