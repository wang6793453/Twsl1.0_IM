package com.twlrg.twsl.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.donkingliang.banner.CustomBanner;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.RoomInfo;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Date:
 */
public class RoomHolder extends RecyclerView.ViewHolder
{
    private ImageView mRoomImgIv;


    private AutoFitTextView     mTitleNameTv;
    private TextView            mPriceTv;
    private TextView            mAreaTv;
    private TextView            mBedTypeTv;
    private TextView            mReserveTv;
    private CustomBanner        mRoombanner;
    private MyItemClickListener listener1;
    private List<String> picList = new ArrayList<>();

    public RoomHolder(View rootView, MyItemClickListener listener1)
    {
        super(rootView);
        mTitleNameTv = (AutoFitTextView) rootView.findViewById(R.id.tv_title);
        mRoomImgIv = (ImageView) rootView.findViewById(R.id.iv_room_img);
        mPriceTv = (TextView) rootView.findViewById(R.id.tv_price);
        mAreaTv = (TextView) rootView.findViewById(R.id.tv_area);
        mBedTypeTv = (TextView) rootView.findViewById(R.id.tv_bed_type);
        mReserveTv = (TextView) rootView.findViewById(R.id.tv_reserve);
        mRoombanner = (CustomBanner) rootView.findViewById(R.id.room_banner);
        this.listener1 = listener1;
    }


    public void setRoomInfo(RoomInfo mRoomInfo, Context mContext, final int p)
    {
        picList.clear();
        picList.addAll(mRoomInfo.getPicList());


        int width = APPUtils.getScreenWidth(mContext);
        int height = (int) (width * 0.75);
        //        mRoomImgIv.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        //        ImageLoader.getInstance().displayImage(Urls.getImgUrl(mRoomInfo.getPic1()), mRoomImgIv);
        mRoombanner.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        mRoombanner.setPages(new CustomBanner.ViewCreator<String>()
        {
            @Override
            public View createView(Context context, int position)
            {
                //这里返回的是轮播图的项的布局 支持任何的布局
                //position 轮播图的第几个项
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String data)
            {
                ImageLoader.getInstance().displayImage(Urls.getImgUrl(picList.get(position)), (ImageView) view);
            }
        }, picList);


        mTitleNameTv.setText(mRoomInfo.getTitle());
        mPriceTv.setText("￥" + mRoomInfo.getPrice());

        String smokeless = mRoomInfo.getSmokeless();
        String wifi = mRoomInfo.getWifi();
        String window = mRoomInfo.getWindow();
        String price_type = mRoomInfo.getPrice_type();

        String mSmokeless = "有烟房";
        String mWifi = "有WIFI";
        String mWindow = "有窗";
        String zc = "无早餐";
        String floor = mRoomInfo.getFloor();

        if (!StringUtils.stringIsEmpty(floor) && !floor.contains("层"))
        {
            floor = floor + "层";
        }

        if (!"1".equals(smokeless))
        {
            mSmokeless = "无烟房";
        }
        if (!"1".equals(wifi))
        {
            mWifi = "无WIFI";
        }

        if (!"1".equals(window))
        {
            mWindow = "无窗";
        }
        if ("wz".equals(price_type))
        {
            zc = "无早餐";
        }
        else if ("dz".equals(price_type))
        {
            zc = "单早餐";
        }
        else if ("sz".equals(price_type))
        {
            zc = "双早餐";
        }

        if("无烟房".equals(mSmokeless))
        {
            mAreaTv.setText(mRoomInfo.getArea() + "㎡/" + floor + "/" + mWindow + "/" + mSmokeless);
        }
        else
        {
            mAreaTv.setText(mRoomInfo.getArea() + "㎡/" + floor + "/" + mWindow );
        }


        mBedTypeTv.setText(mRoomInfo.getBed_type() + "/" + zc + "/" + mWifi);

        mReserveTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener1.onItemClick(v, p);
            }
        });
    }

}
