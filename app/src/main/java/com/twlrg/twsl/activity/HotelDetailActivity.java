package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.donkingliang.banner.CustomBanner;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twlrg.twsl.MyApplication;
import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.ConferenceAdapter;
import com.twlrg.twsl.adapter.RoomAdapter;
import com.twlrg.twsl.entity.ConferenceInfo;
import com.twlrg.twsl.entity.HotelImgInfo;
import com.twlrg.twsl.entity.HotelInfo;
import com.twlrg.twsl.entity.RoomInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.HotelDetailHandler;
import com.twlrg.twsl.json.HotelRoomListHandler;
import com.twlrg.twsl.json.RoomListHandler;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;
import com.twlrg.twsl.widget.EmptyDecoration;
import com.twlrg.twsl.widget.FullyLinearLayoutManager;
import com.twlrg.twsl.widget.ObservableScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/17 22:01
 * 邮箱：wangxianyun1@163.com
 * 描述：酒店详情
 */
public class HotelDetailActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.iv_hotel_img)
    ImageView       ivHotelImg;
    @BindView(R.id.rb_star)
    RatingBar       rbStar;
    @BindView(R.id.tv_address)
    TextView        tvAddress;
    @BindView(R.id.tv_distance)
    TextView        tvDistance;
    @BindView(R.id.iv_location)
    ImageView       ivLocation;
    @BindView(R.id.tv_reviews_label)
    TextView        tvReviewsLabe;
    @BindView(R.id.tv_comment_count)
    TextView        tvCommentCount;
    @BindView(R.id.tv_start_date)
    TextView        tvStartDate;
    @BindView(R.id.tv_end_date)
    TextView        tvEndDate;
    @BindView(R.id.tv_breakfast_type1)
    TextView        tvBreakfastType1;
    @BindView(R.id.tv_breakfast_type2)
    TextView        tvBreakfastType2;
    @BindView(R.id.tv_breakfast_type3)
    TextView        tvBreakfastType3;
    @BindView(R.id.tv_breakfast_type4)
    TextView        tvBreakfastType4;
    @BindView(R.id.ll_breakfast)
    LinearLayout    mBreakfastLayout;

    @BindView(R.id.ll_time)
    LinearLayout         mTimeLayout;
    @BindView(R.id.rv_room)
    RecyclerView         rvRoom;
    @BindView(R.id.iv_room_more)
    ImageView            ivRoomMore;
    @BindView(R.id.ll_room_more)
    LinearLayout         llRoomMore;
    @BindView(R.id.rv_conference)
    RecyclerView         rvConference;
    @BindView(R.id.iv_conference_more)
    ImageView            ivConferenceMore;
    @BindView(R.id.ll_conference_more)
    LinearLayout         llConferenceMore;
    @BindView(R.id.tv_summary)
    TextView             tvSummary;
    @BindView(R.id.tv_summary_more)
    TextView             tvSummaryMore;
    @BindView(R.id.rl_policy)
    RelativeLayout       rlPolicy;
    @BindView(R.id.rl_facilities)
    RelativeLayout       rlFacilities;
    @BindView(R.id.scrollView)
    ObservableScrollView scrollView;
    @BindView(R.id.rl_chat)
    RelativeLayout       llChat;

    @BindView(R.id.ll_breakfast1)
    LinearLayout mTopBreakfastLayout;
    @BindView(R.id.tv_jl)
    TextView     tvJl;
    @BindView(R.id.tv_breakfast_type11)
    TextView     tvBreakfastType11;
    @BindView(R.id.tv_breakfast_type22)
    TextView     tvBreakfastType22;
    @BindView(R.id.tv_breakfast_type33)
    TextView     tvBreakfastType33;
    @BindView(R.id.tv_breakfast_type44)
    TextView     tvBreakfastType44;
    @BindView(R.id.tv_conference)
    TextView     tvConference;
    @BindView(R.id.hotel_banner)
    CustomBanner hotelBanner;
    @BindView(R.id.topView)
    View         topView;
    @BindView(R.id.iv_arrow_right)
    ImageView    ivArrowRight;

    private String id, city_value, s_date, e_date, lng, lat, title;
    private boolean summary_is_open;
    private static final int GET_DATE_CODE = 0x99;

    private RoomAdapter       mRoomAdapter;
    private ConferenceAdapter mConferenceAdapter;

    private List<RoomInfo>       roomInfoListAll       = new ArrayList<>();
    private List<ConferenceInfo> conferenceInfoListAll = new ArrayList<>();

    private List<RoomInfo>       roomInfoList       = new ArrayList<>();
    private List<ConferenceInfo> conferenceInfoList = new ArrayList<>();

    private List<String> picList = new ArrayList<>();


    private String  mBreakfastType; //wz dz sz
    private boolean isShowMoreRoom, isShowMoreConference;
    private HotelInfo mHotelInfo;
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL    = 0x02;

    private static final int    GET_ROOM_LIST_SUCCESS = 0x03;
    private static final String GET_HOTEL_DETAIL      = "GET_HOTEL_DETAIL";
    private static final String GET_ROOM_LIST         = "GET_ROOM_LIST";

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    HotelDetailHandler mHotelDetailHandler = (HotelDetailHandler) msg.obj;
                    mHotelInfo = mHotelDetailHandler.getHotelInfo();
                    if (null != mHotelInfo)
                    {
                        picList.clear();
                        picList.add(mHotelInfo.getHotel_img());
                        List<HotelImgInfo> hotelImgInfoList = mHotelInfo.getHotelImgInfoList();

                        for (int i = 0; i < hotelImgInfoList.size(); i++)
                        {
                            picList.add(hotelImgInfoList.get(i).getPic());
                        }

                        rbStar.setRating(Float.parseFloat(mHotelInfo.getStar() + ""));
                        tvAddress.setText("地址:" + mHotelInfo.getAddress());
                        tvDistance.setText(mHotelInfo.getJl() + "公里");
                        tvReviewsLabe.setText(mHotelInfo.getReviews_label());
                        tvCommentCount.setText(mHotelInfo.getCount() + "条评论");
                        tvStartDate.setText(s_date);
                        tvEndDate.setText(e_date);
                        tvSummary.setText(mHotelInfo.getSummary());
                        tvSummary.setLines(3);

                        int width = APPUtils.getScreenWidth(HotelDetailActivity.this);
                        int height = (int) (width * 0.66);
                        hotelBanner.setLayoutParams(new LinearLayout.LayoutParams(width, height));

                        hotelBanner.setPages(new CustomBanner.ViewCreator<String>()
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
                                //在这里更新轮播图的UI
                                //position 轮播图的第几个项
                                //view 轮播图当前项的布局 它是createView方法的返回值
                                //data 轮播图当前项对应的数据
                                //   Glide.with(context).load(data).into((ImageView) view);
                                ImageLoader.getInstance().displayImage(Urls.getImgUrl(picList.get(position)), (ImageView) view);
                            }
                        }, picList);

                    }
                    roomInfoListAll.clear();
                    conferenceInfoListAll.clear();
                    roomInfoListAll.addAll(mHotelDetailHandler.getRoomInfoList());
                    conferenceInfoListAll.addAll(mHotelDetailHandler.getConferenceInfoList());

                    //                    roomInfoList.addAll(mHotelDetailHandler.getRoomInfoList());
                    //                    conferenceInfoList.addAll(mHotelDetailHandler.getConferenceInfoList());
                    updateRoom();
                    updateConference();

                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(HotelDetailActivity.this, msg.obj.toString());

                    break;

                case GET_ROOM_LIST_SUCCESS:

                    HotelRoomListHandler mRoomListHandler = (HotelRoomListHandler) msg.obj;
                    roomInfoListAll.clear();
                    roomInfoListAll.addAll(mRoomListHandler.getRoomInfoList());
                    updateRoom();

                    break;

            }
        }
    };


    @Override
    protected void initData()
    {
        Intent mIntent = getIntent();
        id = mIntent.getStringExtra("ID");
        city_value = mIntent.getStringExtra("CITY_VALUE");
        s_date = mIntent.getStringExtra("S_DATE");
        e_date = mIntent.getStringExtra("E_DATE");
        lng = mIntent.getStringExtra("LNG");
        lat = mIntent.getStringExtra("LAT");
        title = mIntent.getStringExtra("TITLE");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_hotel_detail);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        llChat.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivLocation.setOnClickListener(this);
        tvCommentCount.setOnClickListener(this);
        llRoomMore.setOnClickListener(this);
        llConferenceMore.setOnClickListener(this);
        tvSummaryMore.setOnClickListener(this);
        rlPolicy.setOnClickListener(this);
        rlFacilities.setOnClickListener(this);

        tvBreakfastType1.setOnClickListener(this);
        tvBreakfastType2.setOnClickListener(this);
        tvBreakfastType3.setOnClickListener(this);
        tvBreakfastType4.setOnClickListener(this);

        tvBreakfastType11.setOnClickListener(this);
        tvBreakfastType22.setOnClickListener(this);
        tvBreakfastType33.setOnClickListener(this);
        tvBreakfastType44.setOnClickListener(this);
        mTimeLayout.setOnClickListener(this);

        scrollView.setOnScrollListener(new ObservableScrollView.OnScrollListener()
        {
            @Override
            public void onScroll(int scrollY)
            {
                int mHeight = mBreakfastLayout.getTop();
                //判断滑动距离scrollY是否大于0，因为大于0的时候就是可以滑动了，此时mTabViewLayout.getTop()才能取到值。

                if (scrollY > 0 && scrollY >= mHeight)
                {
                    if (!mTopBreakfastLayout.isShown())
                    {
                        mTopBreakfastLayout.setVisibility(View.VISIBLE);
                    }

                }
                else
                {
                    if (mTopBreakfastLayout.isShown())
                    {
                        mTopBreakfastLayout.setVisibility(View.GONE);

                    }

                }
            }
        });
    }


    @Override
    protected void initViewData()
    {
        tvTitle.setText(title);

        setStatusBarTextDeep(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvBreakfastType1.setSelected(true);
        tvBreakfastType11.setSelected(true);
        rvRoom.setLayoutManager(new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvRoom.addItemDecoration(new EmptyDecoration(HotelDetailActivity.this, ""));
        mRoomAdapter = new RoomAdapter(roomInfoList, HotelDetailActivity.this, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                //                Bundle b = new Bundle();
                //                b.putSerializable("ROOM", roomInfoList.get(position));


                if (MyApplication.getInstance().isLogin())
                {
                    startActivity(new Intent(HotelDetailActivity.this, BookRoomActivity.class)
                            .putExtra("HOTEL_NAME", mHotelInfo.getTitle())
                            .putExtra("ROOM_NAME", roomInfoList.get(position).getTitle())
                            .putExtra("MERCHANT_ID", mHotelInfo.getId())
                            .putExtra("ROOM_ID", roomInfoList.get(position).getId())
                            .putExtra("CHECK_IN", s_date)
                            .putExtra("CHECK_OUT", e_date)
                            .putExtra("CITY_VALUE", city_value)
                            .putExtra("PRICE_TYPE", roomInfoList.get(position).getPrice_type())

                    );
                }
                else
                {
                    LoginActivity.start(HotelDetailActivity.this, true);
                }


            }
        });
        rvRoom.setAdapter(mRoomAdapter);

        rvConference.setLayoutManager(new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvConference.addItemDecoration(new EmptyDecoration(HotelDetailActivity.this, ""));

        mConferenceAdapter = new ConferenceAdapter(conferenceInfoList, HotelDetailActivity.this);
        rvConference.setAdapter(mConferenceAdapter);
        loadData();
    }


    private void loadData()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("id", id);
        valuePairs.put("lat", lat);
        valuePairs.put("lng", lng);
        valuePairs.put("city_value", city_value);
        valuePairs.put("s_date", s_date);
        valuePairs.put("e_date", e_date);
        DataRequest.instance().request(HotelDetailActivity.this, Urls.getHotelDetailUrl(), this, HttpRequest.POST, GET_HOTEL_DETAIL, valuePairs,
                new HotelDetailHandler());
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        if (v == ivBack)
        {
            finish();
        }
        //地图定位
        else if (v == ivLocation)
        {
            startActivity(new Intent(HotelDetailActivity.this, LocationActivity.class)
                    .putExtra("LAT", mHotelInfo.getLat())
                    .putExtra("LNG", mHotelInfo.getLng())
                    .putExtra("HOTEL_NAME", mHotelInfo.getTitle())

            );
        }
        //评论列表
        else if (v == tvCommentCount)
        {
            startActivity(new Intent(HotelDetailActivity.this, CommentListActivity.class).putExtra("MERCHANT_ID", id));
        }
        //更多房间
        else if (v == llRoomMore)
        {
            updateRoom();
        }
        //更多会议室
        else if (v == llConferenceMore)
        {
            updateConference();
        }
        //查看全部简介
        else if (v == tvSummaryMore)
        {
            if (summary_is_open)
            {
                summary_is_open = false;
                tvSummary.setMaxLines(3);
                tvSummaryMore.setText("查看更多");
            }
            else
            {
                summary_is_open = true;
                tvSummary.setMaxLines(99);
                tvSummaryMore.setText("收起");
            }
        }
        //查看政策
        else if (v == rlPolicy)
        {
            if (null != mHotelInfo)
            {
                Bundle b = new Bundle();
                b.putSerializable("HOTEL", mHotelInfo);
                startActivity(new Intent(HotelDetailActivity.this, HotelPolicyActivity.class).putExtras(b));
            }


        }
        //查看设施
        else if (v == rlFacilities)
        {
            startActivity(new Intent(HotelDetailActivity.this, FacilitiesActivity.class).putExtra("MERCHANT_ID", id));
        }
        //跳转聊天
        else if (v == llChat)
        {
            startActivity(new Intent(HotelDetailActivity.this, HotelSalesmanListActivity.class).putExtra("MERCHANT_ID", id));
        }
        //不限早餐
        else if (v == tvBreakfastType1 || v == tvBreakfastType11)
        {
            mBreakfastType = "";
            isShowMoreRoom = false;
            tvBreakfastType1.setSelected(true);
            tvBreakfastType11.setSelected(true);
            tvBreakfastType2.setSelected(false);
            tvBreakfastType22.setSelected(false);
            tvBreakfastType3.setSelected(false);
            tvBreakfastType33.setSelected(false);
            tvBreakfastType4.setSelected(false);
            tvBreakfastType44.setSelected(false);
            getRoomPrice();
        }
        //无早餐
        else if (v == tvBreakfastType2 || v == tvBreakfastType22)
        {
            mBreakfastType = "wz";
            isShowMoreRoom = false;
            tvBreakfastType1.setSelected(false);
            tvBreakfastType11.setSelected(false);
            tvBreakfastType2.setSelected(true);
            tvBreakfastType22.setSelected(true);
            tvBreakfastType3.setSelected(false);
            tvBreakfastType33.setSelected(false);
            tvBreakfastType4.setSelected(false);
            tvBreakfastType44.setSelected(false);
            getRoomPrice();
        }
        //单早餐
        else if (v == tvBreakfastType3 || v == tvBreakfastType33)
        {
            mBreakfastType = "dz";
            isShowMoreRoom = false;
            tvBreakfastType1.setSelected(false);
            tvBreakfastType11.setSelected(false);
            tvBreakfastType2.setSelected(false);
            tvBreakfastType22.setSelected(false);
            tvBreakfastType3.setSelected(true);
            tvBreakfastType33.setSelected(true);
            tvBreakfastType4.setSelected(false);
            tvBreakfastType44.setSelected(false);
            getRoomPrice();
        }
        //双早餐
        else if (v == tvBreakfastType4 || v == tvBreakfastType44)
        {
            mBreakfastType = "sz";
            isShowMoreRoom = false;
            tvBreakfastType1.setSelected(false);
            tvBreakfastType11.setSelected(false);
            tvBreakfastType2.setSelected(false);
            tvBreakfastType22.setSelected(false);
            tvBreakfastType3.setSelected(false);
            tvBreakfastType33.setSelected(false);
            tvBreakfastType4.setSelected(true);
            tvBreakfastType44.setSelected(true);
            getRoomPrice();
        }
        else if (v == mTimeLayout)
        {
            startActivityForResult(new Intent(HotelDetailActivity.this, HotelTimeActivity.class), GET_DATE_CODE);
        }

    }


    private void getRoomPrice()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("id", id);
        valuePairs.put("price_type", mBreakfastType);
        valuePairs.put("city_value", city_value);
        valuePairs.put("s_date", s_date);
        valuePairs.put("e_date", e_date);
        DataRequest.instance().request(HotelDetailActivity.this, Urls.getRoom_priceUrl(), this, HttpRequest.POST, GET_ROOM_LIST, valuePairs,
                new HotelRoomListHandler());
    }

    private void updateRoom()
    {
        if (isShowMoreRoom)
        {
            isShowMoreRoom = false;
            roomInfoList.clear();
            roomInfoList.addAll(roomInfoListAll);
            ivRoomMore.setImageResource(R.drawable.ic_arrow_up_64);
        }
        else
        {

            ivRoomMore.setImageResource(R.drawable.ic_arrow_down_64);
            roomInfoList.clear();
            if (roomInfoListAll.size() > 2)
            {
                isShowMoreRoom = true;
                for (int i = 0; i < 2; i++)
                {
                    roomInfoList.add(roomInfoListAll.get(i));
                }

                llRoomMore.setVisibility(View.VISIBLE);

            }
            else
            {
                roomInfoList.addAll(roomInfoListAll);
                llRoomMore.setVisibility(View.GONE);
            }


        }
        mRoomAdapter.notifyDataSetChanged();
    }


    private void updateConference()
    {

        if (isShowMoreConference)
        {
            isShowMoreConference = false;
            conferenceInfoList.clear();
            conferenceInfoList.addAll(conferenceInfoListAll);
            ivConferenceMore.setImageResource(R.drawable.ic_arrow_up_64);
        }
        else
        {
            ivConferenceMore.setImageResource(R.drawable.ic_arrow_down_64);
            conferenceInfoList.clear();
            if (conferenceInfoListAll.size() > 1)
            {
                isShowMoreConference = true;
                for (int i = 0; i < 1; i++)
                {
                    conferenceInfoList.add(conferenceInfoListAll.get(i));
                }

                llConferenceMore.setVisibility(View.VISIBLE);

            }
            else
            {
                conferenceInfoList.addAll(conferenceInfoListAll);
                llConferenceMore.setVisibility(View.GONE);
            }


        }

        if (conferenceInfoList.isEmpty())
        {
            tvConference.setVisibility(View.GONE);
        }
        else
        {
            tvConference.setVisibility(View.VISIBLE);
        }
        mConferenceAdapter.notifyDataSetChanged();
    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_HOTEL_DETAIL.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_ROOM_LIST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_ROOM_LIST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_DATE_CODE)
        {
            if (resultCode == Activity.RESULT_OK && null != data)
            {
                s_date = data.getStringExtra("CHEK_IN");
                e_date = data.getStringExtra("CHEK_OUT");

                if (!StringUtils.stringIsEmpty(s_date) && !StringUtils.stringIsEmpty(e_date))
                {
                    tvStartDate.setText(s_date);
                    tvEndDate.setText(e_date);
                    loadData();

                }
            }

        }
    }


}
