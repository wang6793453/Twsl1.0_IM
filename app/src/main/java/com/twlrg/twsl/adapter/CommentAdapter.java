package com.twlrg.twsl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.CommentInfo;
import com.twlrg.twsl.holder.CommentHolder;
import com.twlrg.twsl.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentHolder>
{

    private List<CommentInfo> list;

    private MyItemClickListener listener;

    public CommentAdapter(List<CommentInfo> list, MyItemClickListener listener)
    {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        CommentHolder mHolder = new CommentHolder(itemView, parent.getContext(), listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(CommentHolder holder, int position)
    {
        CommentInfo mCommentInfo = list.get(position);
        holder.setCommentInfo(mCommentInfo, position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
