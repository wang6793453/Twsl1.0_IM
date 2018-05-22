package com.twlrg.twsl.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.HotelInfo;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.Urls;


/**
 * Date:
 */
public class HotelHolder extends RecyclerView.ViewHolder
{
    private ImageView mHotelImgIv;
    private RatingBar mStarRb;


    private TextView mHotelNameTv;
    private TextView        mPriceTv;
    private TextView        mPositionLabelTv;
    private TextView        mServiceLabelTv;
    private TextView        mDistanceTv;

    private LinearLayout mItemLayout;

    private MyItemClickListener listener1;

    public HotelHolder(View rootView, MyItemClickListener listener1)
    {
        super(rootView);
        mStarRb = (RatingBar) rootView.findViewById(R.id.rb_star);
        mHotelNameTv = (TextView) rootView.findViewById(R.id.tv_hotel_name);
        mHotelImgIv = (ImageView) rootView.findViewById(R.id.iv_hotel_img);
        mPriceTv = (TextView) rootView.findViewById(R.id.tv_price);
        mPositionLabelTv = (TextView) rootView.findViewById(R.id.tv_position_label);
        mServiceLabelTv = (TextView) rootView.findViewById(R.id.tv_service_label);
        mDistanceTv = (TextView) rootView.findViewById(R.id.tv_distance);
        mItemLayout = (LinearLayout) rootView.findViewById(R.id.ll_item);
        this.listener1 = listener1;
    }


    public void setHotelInfo(HotelInfo mHotelInfo, Context mContext, final int p)
    {

        int width = APPUtils.getScreenWidth(mContext);
        int height = (int) (width * 0.75);
        mHotelImgIv.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        ImageLoader.getInstance().displayImage(Urls.getImgUrl(mHotelInfo.getHotel_img()), mHotelImgIv);
        mStarRb.setRating(Float.parseFloat(mHotelInfo.getStar() + ""));
        mHotelNameTv.setText(mHotelInfo.getTitle());
        mPriceTv.setText("￥" + mHotelInfo.getPrice());
        mPositionLabelTv.setText(mHotelInfo.getPosition_label());
        mServiceLabelTv.setText(mHotelInfo.getService_label());
        mDistanceTv.setText(mHotelInfo.getJl()+"公里");
        mItemLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener1.onItemClick(v, p);
            }
        });
    }

}
