package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.RoomStatusAdapter;
import com.twlrg.twsl.entity.RoomInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.RoomListHandler;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;
import com.twlrg.twsl.widget.DividerDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/5/14 10:20
 * 邮箱：wangxianyun1@163.com
 * 描述：房状维护
 */
public class RoomStatusListActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView    mRecyclerView;
    @BindView(R.id.btn_load)
    Button          btnLoad;
    @BindView(R.id.ll_no_data)
    LinearLayout    llNoData;

    private List<RoomInfo> roomInfoList = new ArrayList<>();
    private RoomStatusAdapter mRoomStatusAdapter;


    private static final int REQUEST_SUCCESS = 0x01;
    public static final  int REQUEST_FAIL    = 0x02;

    private static final String GET_ROOM_lIST = "get_room_list";

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

                    RoomListHandler mRoomListHandler = (RoomListHandler) msg.obj;
                    roomInfoList.addAll(mRoomListHandler.getRoomInfoList());
                    mRoomStatusAdapter.notifyDataSetChanged();
                    if (roomInfoList.isEmpty())
                    {
                        mRecyclerView.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        llNoData.setVisibility(View.GONE);
                    }
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(RoomStatusListActivity.this, msg.obj.toString());
                    if (roomInfoList.isEmpty())
                    {
                        mRecyclerView.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        llNoData.setVisibility(View.GONE);
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
        setContentView(R.layout.activity_room_status);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        btnLoad.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("房态维护");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(RoomStatusListActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerDecoration(RoomStatusListActivity.this));
        mRoomStatusAdapter = new RoomStatusAdapter(roomInfoList, RoomStatusListActivity.this, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                startActivity(new Intent(RoomStatusListActivity.this, RoomStatusDetailActivity.class)
                        .putExtra("ROOM_ID", roomInfoList.get(position).getId())
                        .putExtra("ROOM_NAME", roomInfoList.get(position).getTitle())
                );
            }
        });


        mRecyclerView.setAdapter(mRoomStatusAdapter);


    }


    private void loadData()
    {
        roomInfoList.clear();
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("token", ConfigManager.instance().getToken());
        valuePairs.put("uid", ConfigManager.instance().getUserID());
        valuePairs.put("city_value", ConfigManager.instance().getCityValue());
        DataRequest.instance().request(RoomStatusListActivity.this, Urls.getRoomListUrl(), this, HttpRequest.POST, GET_ROOM_lIST, valuePairs,
                new RoomListHandler());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadData();
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        if (v == ivBack)
        {
            finish();
        }
        else if (v == btnLoad)
        {
            loadData();
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_ROOM_lIST.equals(action))
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
