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
import com.twlrg.twsl.entity.RoomInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
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
public class RoomDetailActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.et_room_title)
    EditText        etRoomTitle;
    @BindView(R.id.et_area)
    EditText        etArea;
    @BindView(R.id.et_check_in)
    EditText        etCheckIn;
    @BindView(R.id.et_floor)
    EditText        etFloor;
    @BindView(R.id.et_bed_type)
    EditText        etBedType;
    @BindView(R.id.et_add_bed)
    EditText        etAddBed;
    @BindView(R.id.tv_smokeless)
    TextView        tvSmokeless;
    @BindView(R.id.ll_smokeless)
    LinearLayout    llSmokeless;
    @BindView(R.id.tv_wifi)
    TextView        tvWifi;
    @BindView(R.id.ll_wifi)
    LinearLayout    llWifi;
    @BindView(R.id.tv_window)
    TextView        tvWindow;
    @BindView(R.id.ll_window)
    LinearLayout    llWindow;
    @BindView(R.id.btn_save)
    Button          btnSave;

    private int smokeless = 0;//1,//0为无烟房，1为有烟房
    private int wifi      = 0;//0,//0为无WIFI，1为有WIFI
    private int window    = 0;//1,//0为无窗，1为有窗


    private String room_id;


    private static final String      ADD_ROOM             = "add_room";
    private static final String      GET_ROOMINFO         = "get_roominfo";
    private static final int         ADD_ROOM_SUCCESS     = 0x01;
    private static final int         REQUEST_FAIL         = 0x02;
    private static final int         GET_ROOMINFO_SUCCESS = 0x03;
    @SuppressLint("HandlerLeak")
    private final        BaseHandler mHandler             = new BaseHandler(RoomDetailActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case ADD_ROOM_SUCCESS:
                    ToastUtil.show(RoomDetailActivity.this, "操作成功");
                    finish();
                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(RoomDetailActivity.this, msg.obj.toString());

                    break;
                case GET_ROOMINFO_SUCCESS:
                    RoomInfoHandler mRoomInfoHandler = (RoomInfoHandler) msg.obj;
                    RoomInfo mRoomInfo = mRoomInfoHandler.getRoomInfo();

                    if (null != mRoomInfo)
                    {
                        etRoomTitle.setText(mRoomInfo.getTitle());
                        etArea.setText(mRoomInfo.getArea());
                        etCheckIn.setText(mRoomInfo.getCheck_in());
                        etFloor.setText(mRoomInfo.getFloor());
                        etBedType.setText(mRoomInfo.getBed_type());
                        etAddBed.setText(mRoomInfo.getAdd_bed());

                        smokeless = Integer.parseInt(mRoomInfo.getSmokeless());
                        wifi = Integer.parseInt(mRoomInfo.getWifi());
                        window = Integer.parseInt(mRoomInfo.getWindow());

                        if (smokeless == 0)
                        {
                            tvSmokeless.setText("无烟房");
                        }
                        else
                        {
                            tvSmokeless.setText("有烟房");
                        }


                        if (wifi == 0)
                        {
                            tvWifi.setText("无WIFI");
                        }
                        else
                        {
                            tvWifi.setText("有WIFI");
                        }

                        if (window == 0)
                        {
                            tvWindow.setText("无窗");
                        }
                        else
                        {
                            tvWindow.setText("有窗");
                        }

                    }

                    break;

            }
        }
    };


    @Override
    protected void initData()
    {
        room_id = getIntent().getStringExtra("ROOM_ID");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_room_detail);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        llSmokeless.setOnClickListener(this);
        llWifi.setOnClickListener(this);
        llWindow.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("客房详情");


        if (!StringUtils.stringIsEmpty(room_id))
        {
            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("token", ConfigManager.instance().getToken());
            valuePairs.put("uid", ConfigManager.instance().getUserID());
            valuePairs.put("city_value", ConfigManager.instance().getCityValue());
            valuePairs.put("id", room_id);
            DataRequest.instance().request(RoomDetailActivity.this, Urls.getRoomInfoUrl(), this, HttpRequest.POST, GET_ROOMINFO, valuePairs,
                    new RoomInfoHandler());
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
        else if (v == llSmokeless)
        {
            final String[] smokelessArr = getResources().getStringArray(R.array.smokelessType);
            DialogUtils.showCategoryDialog(this, Arrays.asList(smokelessArr), new MyItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    smokeless = position;

                    tvSmokeless.setText(smokelessArr[position]);

                }
            });
        }
        else if (v == llWifi)
        {
            final String[] wifiArr = getResources().getStringArray(R.array.wifiType);
            DialogUtils.showCategoryDialog(this, Arrays.asList(wifiArr), new MyItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    wifi = position;

                    tvWifi.setText(wifiArr[position]);

                }
            });
        }
        else if (v == llWindow)
        {
            final String[] windowArr = getResources().getStringArray(R.array.windowType);
            DialogUtils.showCategoryDialog(this, Arrays.asList(windowArr), new MyItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    window = position;

                    tvWindow.setText(windowArr[position]);

                }
            });
        }
        else if (v == btnSave)
        {
            String mRoomTitle = etRoomTitle.getText().toString();
            String mAre = etArea.getText().toString();
            String mCheckIn = etCheckIn.getText().toString();
            String mFloor = etFloor.getText().toString();
            String mBedType = etBedType.getText().toString();
            String mAddBed = etAddBed.getText().toString();

            if (StringUtils.stringIsEmpty(mRoomTitle))
            {
                ToastUtil.show(RoomDetailActivity.this, "请输入客房名称");
                return;
            }
            if (StringUtils.stringIsEmpty(mAre))
            {
                ToastUtil.show(RoomDetailActivity.this, "请输入客房面积");
                return;
            }
            if (StringUtils.stringIsEmpty(mCheckIn))
            {
                ToastUtil.show(RoomDetailActivity.this, "请输入入住人数");
                return;
            }
            if (StringUtils.stringIsEmpty(mFloor))
            {
                ToastUtil.show(RoomDetailActivity.this, "请输入客房楼层");
                return;
            }

            if (StringUtils.stringIsEmpty(mBedType))
            {
                ToastUtil.show(RoomDetailActivity.this, "请输入客房床型");
                return;
            }

            if (StringUtils.stringIsEmpty(mAddBed))
            {
                ToastUtil.show(RoomDetailActivity.this, "请输入加床价格");
                return;
            }


            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();


            valuePairs.put("token", ConfigManager.instance().getToken());
            valuePairs.put("uid", ConfigManager.instance().getUserID());
            valuePairs.put("city_value", ConfigManager.instance().getCityValue());
            valuePairs.put("title", mRoomTitle);
            valuePairs.put("area", mAre);
            valuePairs.put("check_in", mCheckIn);
            valuePairs.put("floor", mFloor);
            valuePairs.put("add_bed", mAddBed);
            valuePairs.put("bed_type", mBedType);
            valuePairs.put("smokeless", smokeless + "");
            valuePairs.put("wifi", wifi + "");
            valuePairs.put("window", window + "");


            if (StringUtils.stringIsEmpty(room_id))
            {


                DataRequest.instance().request(RoomDetailActivity.this, Urls.getAddRoomUrl(), this, HttpRequest.POST, ADD_ROOM, valuePairs,
                        new ResultHandler());
            }
            else
            {
                valuePairs.put("id", room_id);
                DataRequest.instance().request(RoomDetailActivity.this, Urls.getEditRoomUrl(), this, HttpRequest.POST, ADD_ROOM, valuePairs,
                        new ResultHandler());
            }


        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {

        hideProgressDialog();
        if (ADD_ROOM.equals(action))
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
        else if (GET_ROOMINFO.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_ROOMINFO_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }
}
