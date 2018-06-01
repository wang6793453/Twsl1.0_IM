package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.RoomMonthListHandler;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
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
 * 作者：王先云 on 2018/5/28 14:58
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class SettingRoomPriceActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.tv_selected_date)
    TextView        tvDate;
    @BindView(R.id.tv_start_date)
    TextView        tvStartDate;
    @BindView(R.id.tv_end_date)
    TextView        tvEndDate;
    @BindView(R.id.tv_week_1)
    TextView        tvWeek1;
    @BindView(R.id.tv_week_2)
    TextView        tvWeek2;
    @BindView(R.id.tv_week_3)
    TextView        tvWeek3;
    @BindView(R.id.tv_week_4)
    TextView        tvWeek4;
    @BindView(R.id.tv_week_5)
    TextView        tvWeek5;
    @BindView(R.id.tv_week_6)
    TextView        tvWeek6;
    @BindView(R.id.tv_week_7)
    TextView        tvWeek7;
    @BindView(R.id.ll_week)
    LinearLayout    llWeek;
    @BindView(R.id.et_wz)
    EditText        etWz;
    @BindView(R.id.et_dz)
    EditText        etDz;
    @BindView(R.id.et_sz)
    EditText        etSz;
    @BindView(R.id.btn_save)
    Button          btnSave;
    private String s_date, e_date, id;
    private              List<TextView> mWeekViewList = new ArrayList<>();
    private static final int            GET_DATE_CODE = 0x99;


    private static final int REQUEST_SUCCESS = 0x01;
    public static final  int REQUEST_FAIL    = 0x02;

    private static final String      EDIT_ROOM_PRICE = "edit_room_status";
    @SuppressLint("HandlerLeak")
    private              BaseHandler mHandler        = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    ToastUtil.show(SettingRoomPriceActivity.this, "修改成功");
                    finish();
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(SettingRoomPriceActivity.this, msg.obj.toString());

                    break;


            }
        }
    };


    @Override
    protected void initData()
    {
        s_date = getIntent().getStringExtra("S_DATE");
        e_date = getIntent().getStringExtra("E_DATE");
        id = getIntent().getStringExtra("ID");

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_setting_room_price);
        setTranslucentStatus();

    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("房价维护");
        if (StringUtils.stringIsEmpty(e_date) || StringUtils.stringIsEmpty(e_date))
        {
            s_date = StringUtils.getCurrentTime();
            e_date = StringUtils.getNextMonth();
            tvDate.setVisibility(View.VISIBLE);
        }
        else
        {
            tvDate.setVisibility(View.GONE);
        }

        tvStartDate.setText(s_date);
        tvEndDate.setText(e_date);
        showWeek();
        mWeekViewList.add(tvWeek1);
        mWeekViewList.add(tvWeek2);
        mWeekViewList.add(tvWeek3);
        mWeekViewList.add(tvWeek4);
        mWeekViewList.add(tvWeek5);
        mWeekViewList.add(tvWeek6);
        mWeekViewList.add(tvWeek7);

        for (int i = 0; i < mWeekViewList.size(); i++)
        {
            final TextView mWeekView = mWeekViewList.get(i);
            mWeekViewList.get(i).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mWeekView.isSelected())
                    {
                        mWeekView.setSelected(false);
                    }
                    else
                    {
                        mWeekView.setSelected(true);
                    }
                }
            });
        }
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        if (v == ivBack)
        {
            finish();
        }
        else if (v == tvDate)
        {
            startActivityForResult(new Intent(SettingRoomPriceActivity.this, HotelTimeActivity.class), GET_DATE_CODE);
        }
        else if (v == btnSave)
        {

            String wz = etWz.getText().toString();
            String dz = etDz.getText().toString();
            String sz = etSz.getText().toString();


            if (StringUtils.stringIsEmpty(wz))
            {
                ToastUtil.show(this, "请输入无早的价格");
                return;
            }
            if (StringUtils.stringIsEmpty(dz))
            {
                ToastUtil.show(this, "请输入单早的价格");
                return;
            }
            if (StringUtils.stringIsEmpty(sz))
            {
                ToastUtil.show(this, "请输入双早的价格");
                return;
            }

            //单修改
            if (s_date.equals(e_date))
            {
                showProgressDialog();
                Map<String, String> valuePairs = new HashMap<>();
                valuePairs.put("token", ConfigManager.instance().getToken());
                valuePairs.put("uid", ConfigManager.instance().getUserID());
                valuePairs.put("id", id);
                valuePairs.put("city_value", ConfigManager.instance().getCityValue());
                valuePairs.put("date", s_date);
                valuePairs.put("wz", wz);
                valuePairs.put("dz", dz);
                valuePairs.put("sz", sz);
                DataRequest.instance().request(SettingRoomPriceActivity.this, Urls.getEditRoomPriceUrl(), this, HttpRequest.POST, EDIT_ROOM_PRICE, valuePairs,
                        new RoomMonthListHandler());
            }
        }
        else
        {

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
                }
                showWeek();
            }

        }
    }


    private void showWeek()
    {
        int days = StringUtils.differentDaysByMillisecond(s_date, e_date);

        if (days > 0)
        {
            llWeek.setVisibility(View.VISIBLE);
        }
        else
        {
            llWeek.setVisibility(View.GONE);
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (EDIT_ROOM_PRICE.equals(action))
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
    }
}
