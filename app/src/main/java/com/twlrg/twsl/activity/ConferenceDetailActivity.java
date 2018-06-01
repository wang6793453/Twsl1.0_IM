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
import com.twlrg.twsl.entity.ConferenceInfo;
import com.twlrg.twsl.entity.RoomInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.ConferenceInfoHandler;
import com.twlrg.twsl.json.ConferenceListHandler;
import com.twlrg.twsl.json.ResultHandler;
import com.twlrg.twsl.json.RoomInfoHandler;
import com.twlrg.twsl.json.RoomListHandler;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.DialogUtils;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/5/15 23:49
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class ConferenceDetailActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.et_title)
    EditText        etTitle;
    @BindView(R.id.et_area)
    EditText        etArea;
    @BindView(R.id.et_ckg)
    EditText        etCkg;
    @BindView(R.id.et_floor)
    EditText        etFloor;
    @BindView(R.id.tv_led)
    TextView        tvLed;
    @BindView(R.id.ll_led)
    LinearLayout    llLed;
    @BindView(R.id.et_theatre)
    EditText        etTheatre;
    @BindView(R.id.et_desk)
    EditText        etDesk;
    @BindView(R.id.et_banquet)
    EditText        etBanquet;
    @BindView(R.id.et_fishbone)
    EditText        etFishbone;
    @BindView(R.id.et_price)
    EditText        etPrice;
    @BindView(R.id.btn_save)
    Button          btnSave;


    private String conference_id;
    private int led = 0;


    private static final String      ADD_CONFERENCE              = "add_conference";
    private static final String      GET_CONFERENCE_INFO         = "GET_CONFERENCE_INFO";
    private static final int         ADD_ROOM_SUCCESS            = 0x01;
    private static final int         REQUEST_FAIL                = 0x02;
    private static final int         GET_CONFERENCE_INFO_SUCCESS = 0x03;
    @SuppressLint("HandlerLeak")
    private final        BaseHandler mHandler                    = new BaseHandler(ConferenceDetailActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case ADD_ROOM_SUCCESS:
                    ToastUtil.show(ConferenceDetailActivity.this, "操作成功");
                    finish();
                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(ConferenceDetailActivity.this, msg.obj.toString());

                    break;
                case GET_CONFERENCE_INFO_SUCCESS:
                    ConferenceInfoHandler mConferenceInfoHandler = (ConferenceInfoHandler) msg.obj;
                    ConferenceInfo mConferenceInfo = mConferenceInfoHandler.getConferenceInfo();

                    if (null != mConferenceInfo)
                    {
                        etTitle.setText(mConferenceInfo.getTitle());
                        etArea.setText(mConferenceInfo.getArea());
                        etCkg.setText(mConferenceInfo.getCkg());
                        etFloor.setText(mConferenceInfo.getFloor());
                        etTheatre.setText(mConferenceInfo.getTheatre());
                        etDesk.setText(mConferenceInfo.getDesk());
                        etBanquet.setText(mConferenceInfo.getBanquet());
                        etFishbone.setText(mConferenceInfo.getFishbone());
                        etPrice.setText(mConferenceInfo.getPrice() + "");

                        tvLed.setText("1".equals(mConferenceInfo.getLed()) ? "有LED显示屏" : "无LED显示屏");

                    }
                    break;

            }
        }
    };

    @Override
    protected void initData()
    {
        conference_id = getIntent().getStringExtra("CONFERENCE_ID");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_conference_detail);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        llLed.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("会议室详情");


        if (!StringUtils.stringIsEmpty(conference_id))
        {
            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("token", ConfigManager.instance().getToken());
            valuePairs.put("uid", ConfigManager.instance().getUserID());
            valuePairs.put("city_value", ConfigManager.instance().getCityValue());
            valuePairs.put("id", conference_id);
            DataRequest.instance().request(ConferenceDetailActivity.this, Urls.getConferenceInfoUrl(), this, HttpRequest.POST, GET_CONFERENCE_INFO, valuePairs,
                    new ConferenceInfoHandler());
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
        else if (v == llLed)
        {
            final String[] ledArr = getResources().getStringArray(R.array.ledType);
            DialogUtils.showCategoryDialog(this, Arrays.asList(ledArr), new MyItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    led = position;

                    tvLed.setText(ledArr[position]);

                }
            });
        }
        else if (v == btnSave)
        {
            String mRoomTitle = etTitle.getText().toString();
            String mAre = etArea.getText().toString();
            String mCkg = etCkg.getText().toString();
            String mFloor = etFloor.getText().toString();
            String mTheatre = etTheatre.getText().toString();
            String mDesk = etDesk.getText().toString();
            String mBanquet = etBanquet.getText().toString();
            String mFishbone = etFishbone.getText().toString();
            String mPrice = etPrice.getText().toString();


            if (StringUtils.stringIsEmpty(mRoomTitle))
            {
                ToastUtil.show(ConferenceDetailActivity.this, "请输入会议室名称");
                return;
            }
            if (StringUtils.stringIsEmpty(mAre))
            {
                ToastUtil.show(ConferenceDetailActivity.this, "请输入会议室面积");
                return;
            }
            if (StringUtils.stringIsEmpty(mCkg))
            {
                ToastUtil.show(ConferenceDetailActivity.this, "请输入会议室长宽高");
                return;
            }
            if (StringUtils.stringIsEmpty(mFloor))
            {
                ToastUtil.show(ConferenceDetailActivity.this, "请输入会议室楼层");
                return;
            }

            if (StringUtils.stringIsEmpty(mTheatre))
            {
                ToastUtil.show(ConferenceDetailActivity.this, "请输入剧院容纳人数");
                return;
            }


            if(Integer.parseInt(mTheatre)<=0)
            {
                ToastUtil.show(ConferenceDetailActivity.this, "请输入剧院容纳的人数大于0");
                return;
            }

            if (StringUtils.stringIsEmpty(mDesk))
            {
                ToastUtil.show(ConferenceDetailActivity.this, "请输入课桌人数");
                return;
            }


            if(Integer.parseInt(mDesk)<=0)
            {
                ToastUtil.show(ConferenceDetailActivity.this, "请输入课桌的人数大于0");
                return;
            }


            if (StringUtils.stringIsEmpty(mBanquet))
            {
                ToastUtil.show(ConferenceDetailActivity.this, "请输入宴会人数");
                return;
            }

            if(Integer.parseInt(mBanquet)<=0)
            {
                ToastUtil.show(ConferenceDetailActivity.this, "请输入宴会的人数大于0");
                return;
            }

            if (StringUtils.stringIsEmpty(mPrice))
            {
                ToastUtil.show(ConferenceDetailActivity.this, "请输入价格");
                return;
            }


            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("token", ConfigManager.instance().getToken());
            valuePairs.put("uid", ConfigManager.instance().getUserID());
            valuePairs.put("city_value", ConfigManager.instance().getCityValue());
            valuePairs.put("title", mRoomTitle);
            valuePairs.put("area", mAre);
            valuePairs.put("ckg", mCkg);
            valuePairs.put("floor", mFloor);
            valuePairs.put("theatre", mTheatre);
            valuePairs.put("desk", mDesk);
            valuePairs.put("banquet", mBanquet);
            valuePairs.put("fishbone", mFishbone);
            valuePairs.put("price", mPrice);
            valuePairs.put("led", led + "");


            if (StringUtils.stringIsEmpty(conference_id))
            {


                DataRequest.instance().request(ConferenceDetailActivity.this, Urls.getAddConferenceInfoUrl(), this, HttpRequest.POST, ADD_CONFERENCE,
                        valuePairs,
                        new ResultHandler());
            }
            else
            {
                valuePairs.put("id", conference_id);
                DataRequest.instance().request(ConferenceDetailActivity.this, Urls.getEditConferenceInfoUrl(), this, HttpRequest.POST, ADD_CONFERENCE,
                        valuePairs,
                        new ResultHandler());
            }


        }

    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (ADD_CONFERENCE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(ADD_ROOM_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_CONFERENCE_INFO.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_CONFERENCE_INFO_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }
}
