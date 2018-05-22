package com.twlrg.twsl.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.SaleInfo;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.Urls;


/**
 * Date:
 */
public class SaleHolder extends RecyclerView.ViewHolder
{
    private ImageView           mUserPicIv;
    private TextView            mUserNameTv;
    private TextView            mPositionTv;
    private ImageView           mSelectedIv;
    private RelativeLayout      mItemLayout;
    private MyItemClickListener listener;

    public SaleHolder(View rootView, MyItemClickListener listener)
    {
        super(rootView);
        this.listener = listener;
        mUserPicIv = (ImageView) rootView.findViewById(R.id.iv_user_head);
        mUserNameTv = (TextView) rootView.findViewById(R.id.tv_user_name);
        mPositionTv = (TextView) rootView.findViewById(R.id.tv_position);
        mSelectedIv = (ImageView) rootView.findViewById(R.id.iv_selected);
        mItemLayout = (RelativeLayout) rootView.findViewById(R.id.ll_item);

    }


    public void setSaleInfo(SaleInfo mSaleInfo, final int p)
    {
        ImageLoader.getInstance().displayImage(Urls.getImgUrl(mSaleInfo.getPortrait()), mUserPicIv);
        mUserNameTv.setText(mSaleInfo.getNickname());
        mPositionTv.setText(mSaleInfo.getPosition());
        if (mSaleInfo.isSelected())
        {
            mSelectedIv.setImageResource(R.drawable.ic_single_selection_on);
        }
        else
        {
            mSelectedIv.setImageResource(R.drawable.ic_single_selection_off);
        }

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
