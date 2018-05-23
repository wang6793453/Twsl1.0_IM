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
import com.twlrg.twsl.entity.ConferenceInfo;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Date:
 */
public class ConferenceHolder extends RecyclerView.ViewHolder
{
    private ImageView mRoomImgIv;

    private AutoFitTextView mtTitleTv;
    private TextView        mPriceTv;
    private TextView        mAreaTv;
    private TextView        mFloorTv;
    private TextView        mCkgTv;
    private TextView        mLedTv;

    private TextView mTheatreTv;
    private TextView mDeskTv;
    private TextView mBanquetTv;
    private List<String> picList = new ArrayList<>();
    private CustomBanner customBanner;

    public ConferenceHolder(View rootView)
    {
        super(rootView);
        mtTitleTv = (AutoFitTextView) rootView.findViewById(R.id.tv_title);
        mRoomImgIv = (ImageView) rootView.findViewById(R.id.iv_room_img);
        mPriceTv = (TextView) rootView.findViewById(R.id.tv_price);
        mAreaTv = (TextView) rootView.findViewById(R.id.tv_area);
        mFloorTv = (TextView) rootView.findViewById(R.id.tv_floor);
        mCkgTv = (TextView) rootView.findViewById(R.id.tv_ckg);
        mLedTv = (TextView) rootView.findViewById(R.id.tv_led);
        mTheatreTv = (TextView) rootView.findViewById(R.id.tv_theatre);
        mDeskTv = (TextView) rootView.findViewById(R.id.tv_desk);
        mBanquetTv = (TextView) rootView.findViewById(R.id.tv_banquet);
        customBanner = (CustomBanner) rootView.findViewById(R.id.conferenc_banner);
    }


    public void setConferenceInfo(ConferenceInfo mConference, Context mContext)
    {

        int width = APPUtils.getScreenWidth(mContext);
        int height = (int) (width * 0.75);
        //        mRoomImgIv.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        //        ImageLoader.getInstance().displayImage(Urls.getImgUrl(mConference.getPic1()), mRoomImgIv);


        picList.clear();
        picList.addAll(mConference.getPicList());

        customBanner.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        customBanner.setPages(new CustomBanner.ViewCreator<String>()
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


        int price = mConference.getPrice();
        String priceStr = price + "";

        if (price > 10000)
        {
            double n = (double) price / 10000;
            priceStr = n + "万";
        }

        mtTitleTv.setText(mConference.getTitle());
        mPriceTv.setText("￥" + priceStr);
        mAreaTv.setText(mConference.getArea() + "㎡");
        mFloorTv.setText(mConference.getFloor() + "楼");
        mTheatreTv.setText("剧院" + mConference.getTheatre() + "人");
        mDeskTv.setText("课桌" + mConference.getDesk() + "人");
        mBanquetTv.setText("宴会" + mConference.getBanquet() + "人");
        mCkgTv.setText(mConference.getCkg());

        if (StringUtils.stringIsEmpty(mConference.getLed()))
        {
            mLedTv.setText("");
        }
        else
        {
            mLedTv.setText("有LED显示屏");
        }
    }

}
