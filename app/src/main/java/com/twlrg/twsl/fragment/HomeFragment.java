package com.twlrg.twsl.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.twlrg.twsl.MyApplication;
import com.twlrg.twsl.R;
import com.twlrg.twsl.activity.BaseHandler;
import com.twlrg.twsl.activity.CityListActivity;
import com.twlrg.twsl.activity.HotelDetailActivity;
import com.twlrg.twsl.activity.HotelTimeActivity;
import com.twlrg.twsl.activity.MainActivity;
import com.twlrg.twsl.adapter.HotelAdapter;
import com.twlrg.twsl.entity.CityInfo;
import com.twlrg.twsl.entity.FilterInfo;
import com.twlrg.twsl.entity.HotelInfo;
import com.twlrg.twsl.entity.RegionInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.CityListHandler;
import com.twlrg.twsl.json.HotelInfoListHandler;
import com.twlrg.twsl.json.RegionListHandler;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.service.LocationService;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.DialogUtils;
import com.twlrg.twsl.utils.LogUtil;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.ClearEditText;
import com.twlrg.twsl.widget.EmptyDecoration;
import com.twlrg.twsl.widget.FilterPopupWindow;
import com.twlrg.twsl.widget.list.refresh.PullToRefreshBase;
import com.twlrg.twsl.widget.list.refresh.PullToRefreshRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：王先云 on 2018/4/12 16:50
 * 邮箱：wangxianyun1@163.com
 * 描述：首页
 */
public class HomeFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener<RecyclerView>, View.OnClickListener, IRequestListener
{

    @BindView(R.id.tv_city)
    TextView                  tvCity;
    @BindView(R.id.rl_city)
    RelativeLayout            rlCity;
    @BindView(R.id.tv_check)
    TextView                  tvCheck;
    @BindView(R.id.tv_leave)
    TextView                  tvLeave;
    @BindView(R.id.tv_star)
    TextView                  tvStar;
    @BindView(R.id.rl_star)
    RelativeLayout            rlStar;
    @BindView(R.id.tv_distance)
    TextView                  tvDistance;
    @BindView(R.id.rl_distance)
    RelativeLayout            rlDistance;
    @BindView(R.id.tv_price)
    TextView                  tvPrice;
    @BindView(R.id.rl_price)
    RelativeLayout            rlPrice;
    @BindView(R.id.tv_more)
    TextView                  tvMore;
    @BindView(R.id.rl_more)
    RelativeLayout            rlMore;
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    @BindView(R.id.topView)
    View                      topView;
    @BindView(R.id.ll_top)
    LinearLayout              llTopLayout;

    @BindView(R.id.ll_date)
    LinearLayout llDateLayout;


    @BindView(R.id.et_keyword)
    ClearEditText mEtKeyword;
    @BindView(R.id.btn_load)
    Button        btnLoad;
    @BindView(R.id.ll_no_data)
    LinearLayout  llNoData;

    private RecyclerView mRecyclerView;
    private View rootView = null;
    private Unbinder unbinder;


    private int pn = 1;
    private int mRefreshStatus;


    //手机定位的经纬度
    private double lng = 0;
    private double lat = 0;
    private int    star;
    private int    range;
    private int    price;
    private String region;
    private String currentCity = "深圳市";
    private String city_value  = "2158";

    private List<FilterInfo> starFilterInfos     = new ArrayList<>();
    private List<FilterInfo> distanceFilterInfos = new ArrayList<>();
    private List<FilterInfo> priceFilterInfos    = new ArrayList<>();
    private List<FilterInfo> moreFilterInfos     = new ArrayList<>();


    private String mStartDate;
    private String mEndDate;
    private List<HotelInfo>  hotelInfoList  = new ArrayList<>();
    private List<CityInfo>   cityInfoList   = new ArrayList<>();
    private List<RegionInfo> regionInfoList = new ArrayList<>();

    private HotelAdapter    mHotelAdapter;
    private LocationService locationService;

    private int    count;
    private Dialog mToastDialog;
    private static final int REQUEST_SUCCESS    = 0x01;
    private static final int REQUEST_FAIL       = 0x02;
    private static final int GET_CITY_SUCCESS   = 0x03;
    private static final int GET_REGION_SUCCESS = 0x04;
    private static final int GET_REGION_REQUEST = 0X05;

    private static final int    GET_DATE_CODE   = 0x99;
    private static final int    GET_CITY_CODE   = 0x98;
    private static final String GET_HOTEL_LIST  = "GET_HOTEL_LIST";
    private static final String GET_CITY_LIST   = "GET_CITY_LIST";
    private static final String GET_REGION_LIST = "GET_REGION_LIST";

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(getActivity())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    HotelInfoListHandler mHotelInfoListHandler = (HotelInfoListHandler) msg.obj;
                    hotelInfoList.addAll(mHotelInfoListHandler.getHotelInfoList());
                    mHotelAdapter.notifyDataSetChanged();

                    if (hotelInfoList.isEmpty())
                    {
                        llNoData.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        llNoData.setVisibility(View.GONE);
                    }

                    break;

                case REQUEST_FAIL:
                    // ToastUtil.show(getActivity(), msg.obj.toString());

                    mHotelAdapter.notifyDataSetChanged();
                    if (hotelInfoList.isEmpty())
                    {
                        llNoData.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        llNoData.setVisibility(View.GONE);
                    }
                    break;

                case GET_CITY_SUCCESS:
                    CityListHandler mCityListHandler = (CityListHandler) msg.obj;
                    cityInfoList.addAll(mCityListHandler.getCityInfoList());

                    break;

                case GET_REGION_SUCCESS:
                    RegionListHandler mRegionListHandler = (RegionListHandler) msg.obj;
                    moreFilterInfos.clear();
                    mMoreFilterPopupWindow = null;
                    regionInfoList.clear();
                    regionInfoList.addAll(mRegionListHandler.getRegionInfoList());
                    for (int i = 0; i < regionInfoList.size(); i++)
                    {
                        FilterInfo mFilterInfo = new FilterInfo();
                        mFilterInfo.setTitle(regionInfoList.get(i).getName());
                        moreFilterInfos.add(mFilterInfo);
                    }
                    break;

                case GET_REGION_REQUEST:
                    getRegionList();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_home, null);
            unbinder = ButterKnife.bind(this, rootView);
            initData();
            initViews();
            initViewData();
            initEvent();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }
        return rootView;
    }


    @Override
    protected void initData()
    {

        mStartDate = StringUtils.getCurrentTime();
        mEndDate = StringUtils.getTomorrowTime();

        String[] starArr = getActivity().getResources().getStringArray(R.array.home_star);
        String[] distanceArr = getActivity().getResources().getStringArray(R.array.home_distance);
        String[] priceArr = getActivity().getResources().getStringArray(R.array.home_price);

        for (int i = 0; i < starArr.length; i++)
        {
            FilterInfo mFilterInfo = new FilterInfo();
            mFilterInfo.setTitle(starArr[i]);
            if (i == 0)
            {
                mFilterInfo.setSelected(true);
            }

            starFilterInfos.add(mFilterInfo);

        }

        for (int i = 0; i < priceArr.length; i++)
        {
            FilterInfo mFilterInfo = new FilterInfo();
            mFilterInfo.setTitle(priceArr[i]);
            if (i == 0)
            {
                mFilterInfo.setSelected(true);
            }

            priceFilterInfos.add(mFilterInfo);

        }


        for (int i = 0; i < distanceArr.length; i++)
        {
            FilterInfo mFilterInfo = new FilterInfo();
            mFilterInfo.setTitle(distanceArr[i]);
            if (i == 0)
            {
                mFilterInfo.setSelected(true);
            }

            distanceFilterInfos.add(mFilterInfo);
        }

    }

    @Override
    protected void initViews()
    {

    }

    @Override
    protected void initEvent()
    {
        btnLoad.setOnClickListener(this);
        rlStar.setOnClickListener(this);
        rlDistance.setOnClickListener(this);
        rlPrice.setOnClickListener(this);
        rlMore.setOnClickListener(this);
        llDateLayout.setOnClickListener(this);
        rlCity.setOnClickListener(this);

        mEtKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    ((MainActivity) getActivity()).showProgressDialog();
                    hotelInfoList.clear();
                    pn = 1;
                    mRefreshStatus = 0;

                    String mKeyword = mEtKeyword.getText().toString();

                    if (StringUtils.stringIsEmpty(mKeyword))
                    {
                        getHotelList();

                    }
                    else
                    {
                        getHotelByKeyword(mKeyword);
                    }


                    return true;
                }
                return false;
            }
        });


        mEtKeyword.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.length() == 0)
                {
                    ((MainActivity) getActivity()).showProgressDialog();
                    hotelInfoList.clear();
                    pn = 1;
                    mRefreshStatus = 0;
                    getHotelList();
                }
            }
        });


        //不需要用复制
        mEtKeyword.setCustomSelectionActionModeCallback(new ActionMode.Callback()
        {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu)
            {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu)
            {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem)
            {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode)
            {

            }
        });
    }


    @Override
    public void onResume()
    {
        super.onResume();

        if (null != mToastDialog)
        {
            mToastDialog.dismiss();
        }
        ((MainActivity) getActivity()).changeTabStatusColor(0);
        openGPSSettings();


    }

    @Override
    protected void initViewData()
    {
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(getActivity())));
        mPullToRefreshRecyclerView.setPullLoadEnabled(true);
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        mPullToRefreshRecyclerView.setOnRefreshListener(this);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new EmptyDecoration(getActivity(), ""));

        mHotelAdapter = new HotelAdapter(hotelInfoList, getActivity(), new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                HotelInfo mHotelInfo = hotelInfoList.get(position);
                startActivity(new Intent(getActivity(), HotelDetailActivity.class)
                        .putExtra("ID", mHotelInfo.getId())
                        .putExtra("TITLE", mHotelInfo.getTitle())
                        .putExtra("CITY_VALUE", city_value)
                        .putExtra("S_DATE", mStartDate)
                        .putExtra("E_DATE", mEndDate)
                        .putExtra("PRICE", price + "")
                        .putExtra("LNG", String.valueOf(lng))
                        .putExtra("LAT", String.valueOf(lat))


                );
            }
        }
        );
        mRecyclerView.setAdapter(mHotelAdapter);

        if (Build.VERSION.SDK_INT >= 23)
        {
            showContacts();
        }

        tvCheck.setText("住 " + StringUtils.toMonthAndDay(mStartDate));
        tvLeave.setText("离 " + StringUtils.toMonthAndDay(mEndDate));


    }


    private void initLocation()
    {
        locationService = ((MyApplication) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getActivity().getIntent().getIntExtra("from", 0);
        if (type == 0)
        {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        }
        else if (type == 1)
        {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (null != unbinder)
        {
            unbinder.unbind();
            unbinder = null;
        }
    }

    //star 星级（0代表不限；2代表经济型；3代表三星实惠；4代表四星豪华；5代表五星奢华）
    //range 距离（0代表不限；1代表500米内；2代表1公里内；3代表2公里内；4代表5公里内；5代表10公里内；6代表20公里内）
    //price 价格（0代表不限；1代表150元以下；2代表150-300元；3代表301-450元；4代表451-600元；5代表600元以上）
    private void getHotelList()
    {

        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("lat", lat + "");
        valuePairs.put("lng", lng + "");
        valuePairs.put("city_value", city_value);
        valuePairs.put("star", star + "");
        valuePairs.put("range", range + "");
        valuePairs.put("price", price + "");
        valuePairs.put("s_date", mStartDate);
        valuePairs.put("e_date", mEndDate);
        valuePairs.put("page", pn + "");
        valuePairs.put("region", region);
        DataRequest.instance().request(getActivity(), Urls.getHotelListUrl(), this, HttpRequest.POST, GET_HOTEL_LIST, valuePairs,
                new HotelInfoListHandler());
    }


    private void getHotelByKeyword(String keyword)
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("lat", lat + "");
        valuePairs.put("lng", lng + "");
        valuePairs.put("city_value", city_value);
        valuePairs.put("s_date", mStartDate);
        valuePairs.put("e_date", mEndDate);
        valuePairs.put("page", "99");
        valuePairs.put("keyword", keyword);
        DataRequest.instance().request(getActivity(), Urls.getHotelByKeywordTitleUrl(), this, HttpRequest.POST, GET_HOTEL_LIST, valuePairs,
                new HotelInfoListHandler());
    }


    private void getRegionList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("city_value", city_value);
        DataRequest.instance().request(getActivity(), Urls.getRegionListUrl(), this, HttpRequest.POST, GET_REGION_LIST, valuePairs,
                new RegionListHandler());
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {

        if (StringUtils.stringIsEmpty(mEtKeyword.getText().toString()))
        {
            hotelInfoList.clear();
            pn = 1;
            mRefreshStatus = 0;
            getHotelList();
        }
        else
        {
            //getHotelByKeyword(mEtKeyword.getText().toString());
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {

        if (StringUtils.stringIsEmpty(mEtKeyword.getText().toString()))
        {
            pn += 1;
            mRefreshStatus = 1;
            getHotelList();
        }
        else
        {
            // getHotelByKeyword(mEtKeyword.getText().toString());
        }
    }

    private FilterPopupWindow mStarFilterPopupWindow;
    private FilterPopupWindow mDistanceFilterPopupWindow;
    private FilterPopupWindow mPriceFilterPopupWindow;
    private FilterPopupWindow mMoreFilterPopupWindow;

    @Override
    public void onClick(View v)
    {
        if (v == rlStar)
        {
            if (null == mStarFilterPopupWindow)
            {
                mStarFilterPopupWindow = new FilterPopupWindow(getActivity(), starFilterInfos, new MyItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        star = position;
                        tvStar.setText(starFilterInfos.get(position).getTitle());
                        tvStar.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
                        mStarFilterPopupWindow.dismiss();
                        if (star > 0)
                        {
                            star += 1;
                        }
                        ((MainActivity) getActivity()).showProgressDialog();
                        hotelInfoList.clear();
                        pn = 1;
                        mRefreshStatus = 0;
                        getHotelList();
                    }
                });

            }
            mStarFilterPopupWindow.showAsDropDown(llTopLayout);

        }
        else if (v == rlDistance)
        {
            if (null == mDistanceFilterPopupWindow)
            {
                mDistanceFilterPopupWindow = new FilterPopupWindow(getActivity(), distanceFilterInfos, new MyItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        range = position;
                        tvDistance.setText(distanceFilterInfos.get(position).getTitle());
                        tvDistance.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
                        mDistanceFilterPopupWindow.dismiss();
                        ((MainActivity) getActivity()).showProgressDialog();
                        hotelInfoList.clear();
                        pn = 1;
                        mRefreshStatus = 0;
                        getHotelList();
                    }
                });

            }
            mDistanceFilterPopupWindow.showAsDropDown(llTopLayout);
        }
        else if (v == rlPrice)
        {
            if (null == mPriceFilterPopupWindow)
            {
                mPriceFilterPopupWindow = new FilterPopupWindow(getActivity(), priceFilterInfos, new MyItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        price = position;
                        tvPrice.setText(priceFilterInfos.get(position).getTitle());
                        tvPrice.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
                        mPriceFilterPopupWindow.dismiss();
                        ((MainActivity) getActivity()).showProgressDialog();
                        hotelInfoList.clear();
                        pn = 1;
                        mRefreshStatus = 0;
                        getHotelList();
                    }
                });

            }
            mPriceFilterPopupWindow.showAsDropDown(llTopLayout);
        }
        else if (v == rlMore)
        {
            if (null == mMoreFilterPopupWindow)
            {
                mMoreFilterPopupWindow = new FilterPopupWindow(getActivity(), moreFilterInfos, new MyItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {

                        // city_value = regionInfoList.get(position).getId();

                        region = regionInfoList.get(position).getId();
                        tvMore.setText(moreFilterInfos.get(position).getTitle());
                        tvMore.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
                        mMoreFilterPopupWindow.dismiss();
                        ((MainActivity) getActivity()).showProgressDialog();
                        hotelInfoList.clear();
                        pn = 1;
                        mRefreshStatus = 0;
                        getHotelList();
                    }
                });

            }
            mMoreFilterPopupWindow.showAsDropDown(llTopLayout);
        }


        else if (v == llDateLayout)
        {
            startActivityForResult(new Intent(getActivity(), HotelTimeActivity.class), GET_DATE_CODE);
        }
        else if (v == rlCity)
        {
            Bundle bundle = new Bundle();
            bundle.putSerializable("CITY_LIST", (Serializable) cityInfoList);
            startActivityForResult(new Intent(getActivity(), CityListActivity.class).putExtras(bundle), GET_CITY_CODE);
        }
        else if (v == btnLoad)
        {
            ((MainActivity) getActivity()).showProgressDialog();
            hotelInfoList.clear();
            pn = 1;
            mRefreshStatus = 0;
            getHotelList();
        }
    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        ((MainActivity) getActivity()).hideProgressDialog();
        if (mRefreshStatus == 1)
        {
            mPullToRefreshRecyclerView.onPullUpRefreshComplete();
        }
        else
        {
            mPullToRefreshRecyclerView.onPullDownRefreshComplete();
        }

        if (GET_HOTEL_LIST.equals(action))
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
        else if (GET_CITY_LIST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_CITY_SUCCESS, obj));
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_DATE_CODE)
        {
            if (resultCode == Activity.RESULT_OK && null != data)
            {
                mStartDate = data.getStringExtra("CHEK_IN");
                mEndDate = data.getStringExtra("CHEK_OUT");

                if (!StringUtils.stringIsEmpty(mStartDate) && !StringUtils.stringIsEmpty(mEndDate))
                {
                    tvCheck.setText("住 " + StringUtils.toMonthAndDay(mStartDate));
                    tvLeave.setText("离 " + StringUtils.toMonthAndDay(mEndDate));
                }
            }

        }
        else if (requestCode == GET_CITY_CODE)
        {
            if (resultCode == Activity.RESULT_OK && null != data)
            {
                String city_id = data.getStringExtra("city_id");
                tvCity.setText(getCityById(city_id));

                if (!StringUtils.stringIsEmpty(getCityById(city_id)))
                {
                    tvDistance.setText("距离");
                    tvDistance.setTextColor(ContextCompat.getColor(getActivity(), R.color.blackA));
                    tvPrice.setText("价格");
                    tvPrice.setTextColor(ContextCompat.getColor(getActivity(), R.color.blackA));
                    tvStar.setText("星级");
                    tvStar.setTextColor(ContextCompat.getColor(getActivity(), R.color.blackA));
                    tvMore.setText("更多");
                    tvMore.setTextColor(ContextCompat.getColor(getActivity(), R.color.blackA));
                    star = 0;
                    price = 0;
                    range = 0;

                    ((MainActivity) getActivity()).showProgressDialog();
                    city_value = city_id;
                    hotelInfoList.clear();
                    pn = 1;
                    mRefreshStatus = 0;
                    getHotelList();

                    mHandler.sendEmptyMessageDelayed(GET_REGION_REQUEST, 1 * 1000);

                }


            }
        }
        if (requestCode == GPS_REQUEST_CODE)
        {
            openGPSSettings();
        }
    }


    private String getCityById(String city_id)
    {
        String city = "深圳市";


        for (int i = 0; i < cityInfoList.size(); i++)
        {
            if (city_id.equals(cityInfoList.get(i).getId()))
            {
                city = cityInfoList.get(i).getName();
                break;
            }
        }

        return city;
    }


    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener()
    {

        @Override
        public void onReceiveLocation(BDLocation location)
        {
            LogUtil.e("TAG", "1111111111111111111111111");

            if (null != location && location.getLocType() != BDLocation.TypeServerError)
            {
                LogUtil.e("TAG", "22222222222222222222222");
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息


                lat = location.getLatitude();
                lng = location.getLongitude();
                currentCity = location.getCity();

                if (location.getPoiList() != null && !location.getPoiList().isEmpty())
                {
                    for (int i = 0; i < location.getPoiList().size(); i++)
                    {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation)
                {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                }
                else if (location.getLocType() == BDLocation.TypeNetWorkLocation)
                {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude())
                    {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                }
                else if (location.getLocType() == BDLocation.TypeOffLineLocation)
                {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                }
                else if (location.getLocType() == BDLocation.TypeServerError)
                {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                }
                else if (location.getLocType() == BDLocation.TypeNetWorkException)
                {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                }
                else if (location.getLocType() == BDLocation.TypeCriteriaException)
                {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }


                LogUtil.e("TAG", sb.toString());

                //定位结束
                locationService.unregisterListener(mListener); //注销掉监听
                locationService.stop(); //停止定位服务

                if (null == tvCity)
                    tvCity = (TextView) rootView.findViewById(R.id.tv_city);
                int index = getCityIndex(currentCity);

                String title = "";

                try
                {
                    if (index < 0)
                    {
                        if (StringUtils.stringIsEmpty(currentCity))
                        {
                            if (!cityInfoList.isEmpty())
                            {
                                CityInfo mCityInfo = cityInfoList.get(0);
                                title = "定位失败,已为您自动切换到" + mCityInfo.getName();
                                tvCity.setText(mCityInfo.getName());
                                city_value = mCityInfo.getId();
                            }
                            else
                            {
                                title = "定位失败,已为您自动切换到深圳市";
                                tvCity.setText("深圳市");
                                city_value = "2158";
                            }


                        }
                        else
                        {
                            if (cityInfoList.isEmpty())
                            {
                                title = "定位成功,您的城市为" + currentCity + ",已为你自动切换到深圳";
                                tvCity.setText("深圳市");
                                city_value = "2158";
                            }
                            else
                            {
                                CityInfo mCityInfo = cityInfoList.get(0);
                                title = "定位成功,您的城市为" + currentCity + ",已为你自动切换到" + mCityInfo.getName();
                                tvCity.setText(mCityInfo.getName());
                                city_value = mCityInfo.getId();
                            }

                        }


                    }
                    else
                    {
                        title = "定位成功,您的城市为" + currentCity;
                        tvCity.setText(currentCity);
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                    title = "定位失败,已为您自动切换到深圳市";
                    city_value = "2158";
                    tvCity.setText("深圳市");
                }

                DialogUtils.showPromptDialog(getActivity(), title, new MyItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        ((MainActivity) getActivity()).showProgressDialog();
                        getHotelList();
                        mHandler.sendEmptyMessageDelayed(GET_REGION_REQUEST, 1 * 1000);
                    }
                });
            }
            else
            {
                LogUtil.e("TAG", "33333333333333");
                String title = "定位失败,已为您自动切换到深圳市";
                tvCity.setText("深圳市");
                city_value = "2158";
                DialogUtils.showPromptDialog(getActivity(), title, new MyItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        ((MainActivity) getActivity()).showProgressDialog();
                        getHotelList();
                        mHandler.sendEmptyMessageDelayed(GET_REGION_REQUEST, 1 * 1000);
                    }
                });
            }
        }

    };


    private int getCityIndex(String city)
    {
        int index = -99;

        for (int i = 0; i < cityInfoList.size(); i++)
        {
            if (cityInfoList.get(i).getName().contains(city))
            {
                index = i;
                city_value = cityInfoList.get(i).getId();
                break;
            }
        }


        return index;
    }


    private static final int BAIDU_READ_PHONE_STATE = 100;

    public void showContacts()
    {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ToastUtil.show(getActivity(), "没有权限,请手动开启定位权限");
            openGPSSettings();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
                    .ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        }
        else
        {
            openGPSSettings();
        }
    }


    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    openGPSSettings();
                }
                else
                {
                    // 没有获取到权限，做特殊处理
                    ToastUtil.show(getActivity(), "没有权限,请手动开启定位权限");
                    openGPSSettings();
                }
                break;
            default:
                break;
        }
    }


    private int GPS_REQUEST_CODE = 10;

    /**
     * 检测GPS是否打开
     *
     * @return
     */
    private boolean checkGPSIsOpen()
    {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    /**
     * 跳转GPS设置
     */
    private void openGPSSettings()
    {
        if (checkGPSIsOpen())
        {
            if (count == 0)
            {
                count++;
                initLocation();
                Map<String, String> valuePairs = new HashMap<>();
                DataRequest.instance().request(getActivity(), Urls.getCityListUrl(), this, HttpRequest.POST, GET_CITY_LIST, valuePairs,
                        new CityListHandler());
            }
        }
        else
        {
            if (null != mToastDialog)
            {
                mToastDialog.show();
            }
            else
            {
                mToastDialog = DialogUtils.showToastDialog2Button(getActivity(), "请打开定位功能", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, GPS_REQUEST_CODE);
                    }
                }, new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        openGPSSettings();
                    }
                });
                mToastDialog.show();
            }

        }
    }


}
