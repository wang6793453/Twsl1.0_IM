package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.BindHotelAdapter;
import com.twlrg.twsl.adapter.SelectCityAdapter;
import com.twlrg.twsl.entity.CityInfo;
import com.twlrg.twsl.entity.HotelInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.CityListHandler;
import com.twlrg.twsl.json.HotelInfoListHandler;
import com.twlrg.twsl.json.ResultHandler;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.EmptyDecoration;
import com.twlrg.twsl.widget.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/6/5 15:38
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class BindHotelActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView    ivBack;
    @BindView(R.id.tv_city)
    TextView     tvCity;
    @BindView(R.id.tv_search)
    TextView     tvSearch;
    @BindView(R.id.et_key)
    EditText     etKey;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_bind)
    Button       btnBind;

    private String city_value, merchant_id, uid;
    private BindHotelAdapter mBindHotelAdapter;
    private List<HotelInfo> hotelInfoList = new ArrayList<>();

    private static final int         REQUEST_FAIL       = 0x02;
    private static final int         GET_HOTEL_SUCCESS  = 0x03;
    private static final int         BIND_HOTEL_SUCCESS = 0x04;
    private static final int         GET_CITY_CODE      = 0x98;
    private static final String      GET_HOTEL_LIST     = "GET_CITY_LIST";
    private static final String      BIND_HOTEL         = "BIND_HOTEL";
    @SuppressLint("HandlerLeak")
    private              BaseHandler mHandler           = new BaseHandler(BindHotelActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {

                case REQUEST_FAIL:
                    ToastUtil.show(BindHotelActivity.this, msg.obj.toString());

                    break;

                case GET_HOTEL_SUCCESS:
                    HotelInfoListHandler mHotelInfoListHandler = (HotelInfoListHandler) msg.obj;
                    hotelInfoList.clear();
                    hotelInfoList.addAll(mHotelInfoListHandler.getHotelInfoList());
                    mBindHotelAdapter.notifyDataSetChanged();
                    break;

                case BIND_HOTEL_SUCCESS:
                    LoginActivity.start(BindHotelActivity.this, true);
                    finish();
                    break;

            }
        }
    };


    @Override
    protected void initData()
    {
        uid = getIntent().getStringExtra("uid");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_bind_hotel);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        tvCity.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        btnBind.setOnClickListener(this);

    }

    @Override
    protected void initViewData()
    {
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new EmptyDecoration(this, ""));

        mBindHotelAdapter = new BindHotelAdapter(hotelInfoList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                for (int i = 0; i < hotelInfoList.size(); i++)
                {
                    if (i == position)
                    {
                        hotelInfoList.get(position).setSelected(true);
                        merchant_id = hotelInfoList.get(position).getId();
                    }
                    else
                    {
                        hotelInfoList.get(position).setSelected(false);
                    }
                }
                mBindHotelAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(mBindHotelAdapter);
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        if (v == ivBack)
        {
            finish();
        }
        else if (v == tvCity)
        {
            startActivityForResult(new Intent(BindHotelActivity.this, CityListActivity.class), GET_CITY_CODE);
        }
        else if (v == tvSearch)
        {
            if (TextUtils.isEmpty(city_value))
            {
                ToastUtil.show(BindHotelActivity.this, "请选择城市");
                return;
            }
            String keyword = etKey.getText().toString();

            if (TextUtils.isEmpty(keyword))
            {
                ToastUtil.show(BindHotelActivity.this, "请输入搜索关键字");
                return;
            }
            getHotelByKeyword(keyword);
        }
        else if (v == btnBind)
        {
            if (TextUtils.isEmpty(merchant_id))
            {
                ToastUtil.show(BindHotelActivity.this, "请选择需要绑定的酒店");
                return;
            }
            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("uid", uid);
            valuePairs.put("merchant_id", merchant_id);
            DataRequest.instance().request(BindHotelActivity.this, Urls.getBindHotelUrl(), this, HttpRequest.POST, BIND_HOTEL, valuePairs,
                    new ResultHandler());

        }
    }


    private void getHotelByKeyword(String keyword)
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("lat", "0");
        valuePairs.put("lng", "0");
        valuePairs.put("city_value", city_value);
        valuePairs.put("s_date", StringUtils.getCurrentTime());
        valuePairs.put("e_date", StringUtils.getCurrentTime());
        valuePairs.put("page", "99");
        valuePairs.put("keyword", keyword);
        DataRequest.instance().request(BindHotelActivity.this, Urls.getHotelByKeywordUrl(), this, HttpRequest.POST, GET_HOTEL_LIST, valuePairs,
                new HotelInfoListHandler());
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_HOTEL_LIST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_HOTEL_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (BIND_HOTEL.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(BIND_HOTEL_SUCCESS, obj));
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
        if (requestCode == GET_CITY_CODE)
        {
            if (resultCode == Activity.RESULT_OK && null != data)
            {
                city_value = data.getStringExtra("city_id");
                String city_name = data.getStringExtra("city_name");
                tvCity.setText(city_name);


            }
        }

    }

}
