package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.FilterInfo;
import com.twlrg.twsl.entity.HotelInfo;
import com.twlrg.twsl.entity.RegionInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.HotelInfoHandler;
import com.twlrg.twsl.json.RegionListHandler;
import com.twlrg.twsl.json.ResultHandler;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.DialogUtils;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;
import com.twlrg.twsl.widget.FilterPopupWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：王先云 on 2018/5/30 10:57
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class EditHotelActivity extends BaseActivity implements IRequestListener
{

    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.tv_hotel_name)
    AutoFitTextView tvHotelName;
    @BindView(R.id.tv_province)
    TextView        tvProvince;
    @BindView(R.id.tv_city)
    TextView        tvCity;
    @BindView(R.id.tv_region)
    TextView        tvRegion;
    @BindView(R.id.ll_region)
    LinearLayout    llRegion;
    @BindView(R.id.et_address)
    EditText        etAddress;
    @BindView(R.id.tv_start)
    TextView        tvStart;
    @BindView(R.id.ll_start)
    LinearLayout    llStart;
    @BindView(R.id.et_phone)
    EditText        etPhone;
    @BindView(R.id.et_fax)
    EditText        etFax;
    @BindView(R.id.et_brand)
    EditText        etBrand;
    @BindView(R.id.et_rooms)
    EditText        etRooms;
    @BindView(R.id.et_opening_time)
    EditText        etOpeningTime;
    @BindView(R.id.et_decoration_time)
    EditText        etDecorationTime;
    @BindView(R.id.et_summary)
    EditText        etSummary;
    @BindView(R.id.et_reviews_label)
    EditText        etReviewsLabel;
    @BindView(R.id.et_service_label)
    EditText        etServiceLabel;
    @BindView(R.id.et_position_label)
    EditText        etPositionLabel;
    @BindView(R.id.et_restaurant)
    EditText        etRestaurant;
    @BindView(R.id.et_shopping)
    EditText        etShopping;
    @BindView(R.id.et_entertainment)
    EditText        etEntertainment;
    @BindView(R.id.et_metro_station)
    EditText        etMetroStation;
    @BindView(R.id.et_scenic_spot)
    EditText        etScenicSpot;
    @BindView(R.id.btn_submit)
    Button          btnSubmit;
    @BindView(R.id.ll_head)
    LinearLayout    llHead;
    private HotelInfo mHotelInfo;


    private List<RegionInfo> regionInfoList  = new ArrayList<>();
    private List<FilterInfo> moreFilterInfos = new ArrayList<>();
    private FilterPopupWindow mMoreFilterPopupWindow;
    private static final String GET_REGION_LIST = "GET_REGION_LIST";


    private static final int    REQUEST_SUCCESS    = 0x01;
    public static final  int    REQUEST_FAIL       = 0x02;
    private static final int    EDIT_HOTEL_SUCCESS = 0x03;
    private static final int    GET_REGION_SUCCESS = 0x04;
    private static final String GET_HOTEL_INFO     = "get_hotel_info";
    private static final String EDIT_HOTEL_INFO    = "edit_hotel_info";

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

                    HotelInfoHandler mHotelInfoHandler = (HotelInfoHandler) msg.obj;
                    mHotelInfo = mHotelInfoHandler.getHotelInfo();
                    getRegionList(mHotelInfo.getCity());
                    if (null != mHotelInfo)
                    {

                        tvHotelName.setText(mHotelInfo.getTitle());
                        tvProvince.setText(mHotelInfo.getProvince_name());
                        tvCity.setText(mHotelInfo.getCity_name());
                        tvRegion.setText(mHotelInfo.getRegion_name());

                        int star = (int) mHotelInfo.getStar();
                        if (star == 3)
                        {
                            tvStart.setText("三星实惠");
                        }
                        else if (star == 4)
                        {
                            tvStart.setText("四星豪华");
                        }
                        else if (star == 5)
                        {
                            tvStart.setText("五星奢华");
                        }
                        else
                        {
                            tvStart.setText("经济型");
                        }

                        etAddress.setText(mHotelInfo.getAddress());
                        etBrand.setText(mHotelInfo.getBrand());
                        etDecorationTime.setText(mHotelInfo.getDecoration_time());
                        etEntertainment.setText(mHotelInfo.getEntertainment());
                        etFax.setText(mHotelInfo.getFax());
                        etOpeningTime.setText(mHotelInfo.getOpening_time());
                        etMetroStation.setText(mHotelInfo.getMetro_station());
                        etPhone.setText(mHotelInfo.getPhone());
                        etPositionLabel.setText(mHotelInfo.getPosition_label());
                        etRestaurant.setText(mHotelInfo.getRestaurant());
                        etReviewsLabel.setText(mHotelInfo.getReviews_label());
                        etRooms.setText(mHotelInfo.getRooms());
                        etScenicSpot.setText(mHotelInfo.getScenic_spot());
                        etServiceLabel.setText(mHotelInfo.getService_label());
                        etShopping.setText(mHotelInfo.getShopping());
                        etSummary.setText(mHotelInfo.getSummary());
                    }

                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(EditHotelActivity.this, msg.obj.toString());
                    break;

                case EDIT_HOTEL_SUCCESS:
                    ToastUtil.show(EditHotelActivity.this, "操作成功");
                    finish();
                    break;

                case GET_REGION_SUCCESS:
                    RegionListHandler mRegionListHandler = (RegionListHandler) msg.obj;
                    moreFilterInfos.clear();
                    mMoreFilterPopupWindow = null;
                    regionInfoList.clear();
                    regionInfoList.addAll(mRegionListHandler.getRegionInfoList());
                    if (!regionInfoList.isEmpty())
                    {
                        regionInfoList.remove(0);
                    }
                    for (int i = 0; i < regionInfoList.size(); i++)
                    {
                        FilterInfo mFilterInfo = new FilterInfo();
                        mFilterInfo.setTitle(regionInfoList.get(i).getName());
                        moreFilterInfos.add(mFilterInfo);
                    }
                    break;

            }
        }
    };


    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_edit_hotel);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        llRegion.setOnClickListener(this);
        llStart.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(true);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("酒店基本信息");
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("merchant_id", ConfigManager.instance().getMerchantId());
        DataRequest.instance().request(this, Urls.getHotelInfoUrl(), this, HttpRequest.POST, GET_HOTEL_INFO, valuePairs,
                new HotelInfoHandler());
    }

    private void getRegionList(String city_value)
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("city_value", city_value);
        DataRequest.instance().request(this, Urls.getRegionListUrl(), this, HttpRequest.POST, GET_REGION_LIST, valuePairs,
                new RegionListHandler());
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == btnSubmit)
        {
            String address = etAddress.getText().toString();
            String summary = etSummary.getText().toString();
            String rooms = etRooms.getText().toString();


            if (StringUtils.stringIsEmpty(address))
            {
                ToastUtil.show(this, "请输入街道地址");
                return;
            }
            if (StringUtils.stringIsEmpty(summary))
            {
                ToastUtil.show(this, "请输入酒店简介");
                return;
            }

            if (StringUtils.stringIsEmpty(rooms) || "0".equals(rooms))
            {
                ToastUtil.show(this, "请输入酒店房间数");
                return;
            }

            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("uid", ConfigManager.instance().getUserID());
            valuePairs.put("token", ConfigManager.instance().getToken());
            valuePairs.put("merchant_id", ConfigManager.instance().getMerchantId());
            valuePairs.put("region", mHotelInfo.getRegion());
            valuePairs.put("address", address);
            valuePairs.put("phone", etPhone.getText().toString());
            valuePairs.put("fax", etFax.getText().toString());
            valuePairs.put("brand", etBrand.getText().toString());
            valuePairs.put("rooms", rooms);
            valuePairs.put("opening_time", etOpeningTime.getText().toString());
            valuePairs.put("decoration_time", etDecorationTime.getText().toString());
            valuePairs.put("summary", summary);
            valuePairs.put("in_out", mHotelInfo.getIn_out());
            valuePairs.put("children_policy", mHotelInfo.getChildren_policy());
            valuePairs.put("meal_plans", mHotelInfo.getMeal_plans());
            valuePairs.put("pet_policy", mHotelInfo.getPet_policy());
            valuePairs.put("cancel_policy", mHotelInfo.getCancel_policy());
            valuePairs.put("invoice", mHotelInfo.getInvoice());
            valuePairs.put("position_label", etPositionLabel.getText().toString());
            valuePairs.put("service_label", etServiceLabel.getText().toString());
            valuePairs.put("reviews_label", etReviewsLabel.getText().toString());
            valuePairs.put("restaurant", etRestaurant.getText().toString());
            valuePairs.put("shopping", etShopping.getText().toString());
            valuePairs.put("entertainment", etEntertainment.getText().toString());
            valuePairs.put("metro_station", etMetroStation.getText().toString());
            valuePairs.put("scenic_spot", etScenicSpot.getText().toString());


            DataRequest.instance().request(this, Urls.getEditHotelInfoUrl(), this, HttpRequest.POST, EDIT_HOTEL_INFO, valuePairs,
                    new ResultHandler());
        }

        else if (v == llRegion)
        {
            if (null == mMoreFilterPopupWindow)
            {
                if (!moreFilterInfos.isEmpty())
                {
                    mMoreFilterPopupWindow = new FilterPopupWindow(this, moreFilterInfos, new MyItemClickListener()
                    {
                        @Override
                        public void onItemClick(View view, int position)
                        {
                            mHotelInfo.setRegion(regionInfoList.get(position).getId());
                            tvRegion.setText(regionInfoList.get(position).getName());
                            mMoreFilterPopupWindow.dismiss();
                        }
                    });
                }
            }

            mMoreFilterPopupWindow.showAsDropDown(llHead);
        }
        else if (v == llStart)
        {
            final String[] starArr = getResources().getStringArray(R.array.hotel_star);
            DialogUtils.showCategoryDialog(this, Arrays.asList(starArr), new MyItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    mHotelInfo.setStar(position + 2);
                    tvStart.setText(starArr[position]);

                }
            });
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_HOTEL_INFO.equals(action))
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
        else if (EDIT_HOTEL_INFO.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(EDIT_HOTEL_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_REGION_LIST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_REGION_SUCCESS, obj));
            }
            else
            {
                // mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }


}
