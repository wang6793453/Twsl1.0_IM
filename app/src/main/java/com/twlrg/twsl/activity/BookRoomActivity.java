package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suke.widget.SwitchButton;
import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.SubOrderInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.SubOrderInfoHandler;
import com.twlrg.twsl.listener.MyOnClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.DialogUtils;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/23 16:48
 * 邮箱：wangxianyun1@163.com
 * 描述：房间预订
 */
public class BookRoomActivity extends BaseActivity implements IRequestListener
{

    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.tv_room_title)
    TextView        tvRoomTitle;
    @BindView(R.id.tv_buynum)
    TextView        tvBuynum;
    @BindView(R.id.ll_room)
    LinearLayout    llRoom;
    @BindView(R.id.tv_count1)
    TextView        tvCount1;
    @BindView(R.id.tv_count2)
    TextView        tvCount2;
    @BindView(R.id.tv_count3)
    TextView        tvCount3;
    @BindView(R.id.tv_count4)
    TextView        tvCount4;
    @BindView(R.id.tv_count5)
    TextView        tvCount5;
    @BindView(R.id.tv_count6)
    TextView        tvCount6;
    @BindView(R.id.tv_count7)
    TextView        tvCount7;
    @BindView(R.id.tv_count8)
    TextView        tvCount8;
    @BindView(R.id.tv_count9)
    TextView        tvCount9;
    @BindView(R.id.tv_count10)
    TextView        tvCount10;
    @BindView(R.id.tv_count)
    TextView        tvCount;
    @BindView(R.id.ll_room_count)
    LinearLayout    llRoomCount;
    @BindView(R.id.et_name1)
    EditText        etName1;
    @BindView(R.id.ll_name1)
    LinearLayout    llName1;
    @BindView(R.id.et_name2)
    EditText        etName2;
    @BindView(R.id.ll_name2)
    LinearLayout    llName2;
    @BindView(R.id.et_name3)
    EditText        etName3;
    @BindView(R.id.ll_name3)
    LinearLayout    llName3;
    @BindView(R.id.et_name4)
    EditText        etName4;
    @BindView(R.id.ll_name4)
    LinearLayout    llName4;
    @BindView(R.id.et_name5)
    EditText        etName5;
    @BindView(R.id.ll_name5)
    LinearLayout    llName5;
    @BindView(R.id.et_name6)
    EditText        etName6;
    @BindView(R.id.ll_name6)
    LinearLayout    llName6;
    @BindView(R.id.et_name7)
    EditText        etName7;
    @BindView(R.id.ll_name7)
    LinearLayout    llName7;
    @BindView(R.id.et_name8)
    EditText        etName8;
    @BindView(R.id.ll_name8)
    LinearLayout    llName8;
    @BindView(R.id.et_name9)
    EditText        etName9;
    @BindView(R.id.ll_name9)
    LinearLayout    llName9;
    @BindView(R.id.et_name10)
    EditText        etName10;
    @BindView(R.id.ll_name10)
    LinearLayout    llName10;
    @BindView(R.id.et_phone)
    EditText        etPhone;
    @BindView(R.id.textView)
    TextView        textView;
    @BindView(R.id.tv_time)
    TextView        tvTime;
    @BindView(R.id.switch_button)
    SwitchButton    switchButton;
    @BindView(R.id.btn_submit)
    Button          btnSubmit;

    @BindView(R.id.ll_occupant)
    LinearLayout llOccupant;


    private List<View>     nameLayoutList = new ArrayList<>();
    private List<TextView> countTvList    = new ArrayList<>();
    private List<EditText> etNameList     = new ArrayList<>();

    private String hotel_name, room_name, merchant_id, room_id, check_in, check_out, city_value, price_type;

    private int buynum = 1;
    private boolean isShowRoom;
    private              boolean isAllName             = true;//判断房间姓名是否全部填写完整
    private              String  zc                    = "无早餐";
    private              int     show_mobile           = 0;
    private static final int     REQUEST_LOGIN_SUCCESS = 0x01;
    public static final  int     REQUEST_FAIL          = 0x02;
    private static final String  CREATE_ORDER          = "create_order";

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {


                case REQUEST_LOGIN_SUCCESS:
                    ToastUtil.show(BookRoomActivity.this, "生成订单成功!");

                    SubOrderInfoHandler mSubOrderInfoHandler = (SubOrderInfoHandler) msg.obj;
                    SubOrderInfo mSubOrderInfo = mSubOrderInfoHandler.getSubOrderInfo();

                    if (null != mSubOrderInfo)
                    {
//                        mSubOrderInfo.setBuynum(buynum + "");
//                        if (!StringUtils.stringIsEmpty(occupant))
//                        {
//                            mSubOrderInfo.setOccupant(occupant.substring(0, occupant.length() - 1));
//                        }
//                        mSubOrderInfo.setHotelName(hotel_name);
//                        mSubOrderInfo.setRoomTitle(room_name + "(" + zc + ")");
//                        mSubOrderInfo.setS_data(check_in);
//                        mSubOrderInfo.setE_data(check_out);
//                        mSubOrderInfo.setPhone(etPhone.getText().toString());
//                        mSubOrderInfo.setMerchant_id(merchant_id);
//
//                        Bundle b = new Bundle();
//                        b.putSerializable("SubOrderInfo", mSubOrderInfo);
                        startActivity(new Intent(BookRoomActivity.this, SubmitOrderActivity.class).putExtra("ORDER_ID",mSubOrderInfo.getOrder_id()));
                        finish();
                    }

                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(BookRoomActivity.this, msg.obj.toString());
                    break;


            }
        }
    };


    @Override
    protected void initData()
    {
        hotel_name = getIntent().getStringExtra("HOTEL_NAME");
        room_name = getIntent().getStringExtra("ROOM_NAME");
        merchant_id = getIntent().getStringExtra("MERCHANT_ID");
        room_id = getIntent().getStringExtra("ROOM_ID");
        check_in = getIntent().getStringExtra("CHECK_IN");
        check_out = getIntent().getStringExtra("CHECK_OUT");
        city_value = getIntent().getStringExtra("CITY_VALUE");
        price_type = getIntent().getStringExtra("PRICE_TYPE");

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
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_book_room);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        btnSubmit.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        llRoom.setOnClickListener(this);
        tvCount.setOnClickListener(this);
        tvCount1.setOnClickListener(this);
        tvCount2.setOnClickListener(this);
        tvCount3.setOnClickListener(this);
        tvCount4.setOnClickListener(this);
        tvCount5.setOnClickListener(this);
        tvCount6.setOnClickListener(this);
        tvCount7.setOnClickListener(this);
        tvCount8.setOnClickListener(this);
        tvCount9.setOnClickListener(this);
        tvCount10.setOnClickListener(this);


        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked)
            {
                switchButton.setChecked(isChecked);

                if (isChecked)
                {
                    show_mobile = 1;
                }
                else
                {
                    show_mobile = 0;
                }
            }
        });
    }

    @Override
    protected void initViewData()
    {
        nameLayoutList.add(llName1);
        nameLayoutList.add(llName2);
        nameLayoutList.add(llName3);
        nameLayoutList.add(llName4);
        nameLayoutList.add(llName5);
        nameLayoutList.add(llName6);
        nameLayoutList.add(llName7);
        nameLayoutList.add(llName8);
        nameLayoutList.add(llName9);
        nameLayoutList.add(llName10);

        countTvList.add(tvCount);
        countTvList.add(tvCount1);
        countTvList.add(tvCount2);
        countTvList.add(tvCount3);
        countTvList.add(tvCount4);
        countTvList.add(tvCount5);
        countTvList.add(tvCount6);
        countTvList.add(tvCount7);
        countTvList.add(tvCount8);
        countTvList.add(tvCount9);
        countTvList.add(tvCount10);

        etNameList.add(etName1);
        etNameList.add(etName2);
        etNameList.add(etName3);
        etNameList.add(etName4);
        etNameList.add(etName5);
        etNameList.add(etName6);
        etNameList.add(etName7);
        etNameList.add(etName8);
        etNameList.add(etName9);
        etNameList.add(etName10);


        setStatusBarTextDeep(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));


        tvTitle.setText(hotel_name);
        tvRoomTitle.setText(room_name);
        tvTime.setText(check_in + " 至 " + check_out);
        tvBuynum.setText(buynum + "间");
        setRoomCount(1);
        updateCountTextView(tvCount1);

    }

    private void setRoomCount(int count)
    {
        if (!llOccupant.isShown())
        {
            llOccupant.setVisibility(View.VISIBLE);
        }

        isShowRoom = false;
        llRoomCount.setVisibility(View.GONE);
        for (int i = 0; i < nameLayoutList.size(); i++)
        {
            if (i < count)
            {
                nameLayoutList.get(i).setVisibility(View.VISIBLE);
            }
            else
            {
                nameLayoutList.get(i).setVisibility(View.GONE);
            }
        }
    }

    String occupant;

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == llRoom)
        {
            if (isShowRoom)
            {
                isShowRoom = false;
                llRoomCount.setVisibility(View.GONE);
            }
            else
            {
                isShowRoom = true;
                llRoomCount.setVisibility(View.VISIBLE);
            }

        }
        else if (countTvList.contains(v))
        {
            updateCountTextView(v);
        }
        else if (v == btnSubmit)
        {

            String mobile = etPhone.getText().toString();

            occupant = getAllName();

            if (StringUtils.stringIsEmpty(occupant) || !isAllName)
            {
                ToastUtil.show(BookRoomActivity.this, "每个房间需要填写一人姓名");
                return;
            }
            if (StringUtils.stringIsEmpty(mobile) || mobile.length() < 11)
            {
                ToastUtil.show(this, "请输入正确的手机号");
                return;
            }


            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("token", ConfigManager.instance().getToken());
            valuePairs.put("uid", ConfigManager.instance().getUserID());
            valuePairs.put("check_in", check_in);
            valuePairs.put("check_out", check_out);
            valuePairs.put("merchant_id", merchant_id);
            valuePairs.put("room_id", room_id);
            valuePairs.put("buynum", buynum + "");
            valuePairs.put("title", room_name);
            valuePairs.put("mobile", mobile);
            valuePairs.put("price_type", price_type);
            valuePairs.put("city_value", city_value);
            valuePairs.put("name", etName1.getText().toString());
            valuePairs.put("occupant", occupant.substring(0, occupant.length() - 1));
            valuePairs.put("show_mobile", show_mobile + "");
            DataRequest.instance().request(BookRoomActivity.this, Urls.getCreatOrderUrl(), this, HttpRequest.POST, CREATE_ORDER, valuePairs,
                    new SubOrderInfoHandler());
        }
    }


    private void updateCountTextView(View v)
    {
        int p = 0;
        for (int i = 0; i < countTvList.size(); i++)
        {
            if (v == countTvList.get(i))
            {
                p = i;
                countTvList.get(i).setSelected(true);
            }
            else
            {
                countTvList.get(i).setSelected(false);
            }
        }


        if (p > 0)//代表不是自定义房间
        {
            buynum = p;
            tvBuynum.setText(buynum + "间");
            setRoomCount(buynum);

        }
        else
        {
            DialogUtils.showRoomCountDialog(BookRoomActivity.this, new MyOnClickListener.OnSubmitListener()
            {
                @Override
                public void onSubmit(String content)
                {
                    buynum = Integer.parseInt(content);
                    tvBuynum.setText(buynum + "间");
                    setRoomCount(1);
                }
            });

        }


    }


    private String getAllName()
    {

        StringBuffer sb = new StringBuffer();

        if (buynum > 10)
        {
            String name = etName1.getText().toString();

            if (StringUtils.stringIsEmpty(name))
            {
                isAllName = false;
            }
            else
            {
                isAllName = true;
                sb.append(name);
                sb.append("/");
            }

            return name;
        }
        else
        {
            for (int i = 0; i < etNameList.size(); i++)
            {
                if (i < buynum)
                {
                    String name = etNameList.get(i).getText().toString();
                    if (StringUtils.stringIsEmpty(name))
                    {
                        isAllName = false;
                        break;
                    }
                    else
                    {
                        isAllName = true;
                        sb.append(name);
                        sb.append("/");
                    }


                }
            }

            return sb.toString();
        }


    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (CREATE_ORDER.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_LOGIN_SUCCESS, obj));
            }

            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }
}
