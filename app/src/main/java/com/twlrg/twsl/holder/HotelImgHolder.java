package com.twlrg.twsl.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.BillInfo;
import com.twlrg.twsl.entity.HotelImgInfo;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.listener.MyOnClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.Urls;


/**
 * Date:
 */
public class HotelImgHolder extends RecyclerView.ViewHolder
{

    private RelativeLayout                       mItemLayout;
    private TextView                             mPicTv;
    private TextView                             mDelTv;
    private TextView                             mFirstTv;
    private ImageView                            mPivIv;
    private MyItemClickListener                  listener;
    private MyOnClickListener.OnCallBackListener listener1;
    private Context                              mContext;

    public HotelImgHolder(View rootView, Context mContext, MyItemClickListener listener, MyOnClickListener.OnCallBackListener listener1)
    {
        super(rootView);
        mPicTv = (TextView) rootView.findViewById(R.id.tv_pic);
        mDelTv = (TextView) rootView.findViewById(R.id.tv_del);
        mPivIv = (ImageView) rootView.findViewById(R.id.iv_pic);
        mFirstTv = (TextView) rootView.findViewById(R.id.tv_first);
        mItemLayout = (RelativeLayout) rootView.findViewById(R.id.ll_item);
        this.listener = listener;
        this.mContext = mContext;
        this.listener1 = listener1;
    }


    public void setHotelImgInfo(HotelImgInfo mHotelImgInfo, final int p)
    {
        int width = APPUtils.getScreenWidth(mContext) / 2;
        int height = width * 3 / 4;

        mItemLayout.setLayoutParams(new ViewGroup.LayoutParams(width, height));

        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(width, height);
        rp.addRule(RelativeLayout.CENTER_IN_PARENT);
        rp.setMargins(20, 10, 20, 10);
        mPivIv.setLayoutParams(rp);
        mPicTv.setLayoutParams(rp);


        if (!TextUtils.isEmpty(mHotelImgInfo.getPic()))
        {
            ImageLoader.getInstance().displayImage(Urls.getImgUrl(mHotelImgInfo.getPic()), mPivIv);
            mPivIv.setVisibility(View.VISIBLE);
            mDelTv.setVisibility(View.VISIBLE);
            mPicTv.setVisibility(View.GONE);
            mFirstTv.setVisibility(View.VISIBLE);
            if ("2".equals(mHotelImgInfo.getStatus()))//2已经设为首图
            {
                mFirstTv.setText("首图");
                mFirstTv.setEnabled(false);
                mFirstTv.setBackgroundResource(R.drawable.common_red_5dp);
            }
            else
            {
                mFirstTv.setText("设为首图");
                mFirstTv.setEnabled(true);
                mFirstTv.setBackgroundResource(R.drawable.common_green_5dp);
            }
        }
        else
        {
            mPivIv.setVisibility(View.GONE);
            mDelTv.setVisibility(View.GONE);
            mPicTv.setVisibility(View.VISIBLE);
            mFirstTv.setVisibility(View.GONE);
        }

        mDelTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener1.onSubmit(p, "1");
            }
        });

        mFirstTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener1.onSubmit(p, "2");
            }
        });

        mPicTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v, p);
            }
        });


    }

}
