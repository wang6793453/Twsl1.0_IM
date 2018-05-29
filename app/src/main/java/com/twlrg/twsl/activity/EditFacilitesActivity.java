package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.FacilitiesAdapter1;
import com.twlrg.twsl.entity.FacilitiesInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.FacilitiesHandler;
import com.twlrg.twsl.json.ResultHandler;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;
import com.twlrg.twsl.widget.DividerDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：王先云 on 2018/5/29 13:27
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class EditFacilitesActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView    recyclerView;
    @BindView(R.id.tv_submit)
    TextView        tvSubmit;
    private List<FacilitiesInfo> facilitiesInfoList = new ArrayList<>();
    private FacilitiesAdapter1 mFacilitiesAdapter1;


    private String id;

    private static final int         REQUEST_SUCCESS        = 0x01;
    public static final  int         REQUEST_FAIL           = 0x02;
    private static final int         EDIT_FACILITES_SUCCESS = 0x03;
    private static final String      GET_FACILITES_lIST     = "get_facilites_list";
    private static final String      EDIT_FACILITES         = "EDIT_FACILITES";
    @SuppressLint("HandlerLeak")
    private              BaseHandler mHandler               = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {


                case REQUEST_SUCCESS:
                    FacilitiesHandler mFacilitiesHandler = (FacilitiesHandler) msg.obj;
                    facilitiesInfoList.clear();
                    facilitiesInfoList.addAll(mFacilitiesHandler.getHotelFacilities());
                    mFacilitiesAdapter1.notifyDataSetChanged();

                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(EditFacilitesActivity.this, msg.obj.toString());
                    break;

                case EDIT_FACILITES_SUCCESS:
                    ToastUtil.show(EditFacilitesActivity.this, "操作成功");
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void initData()
    {
        id = getIntent().getStringExtra("ID");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_edit_facilites);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvSubmit.setText("保存");
        tvSubmit.setTextColor(ContextCompat.getColor(this, R.color.green));
        setStatusBarTextDeep(true);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));

        switch (Integer.parseInt(id))
        {
            case 1:
                tvTitle.setText("酒店设施");
                break;
            case 2:
                tvTitle.setText("活动设施");
                break;
            case 3:
                tvTitle.setText("客房设施");
                break;
            case 4:
                tvTitle.setText("会议室设施");
                break;
            case 5:
                tvTitle.setText("餐厅设施");
                break;
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerDecoration(this));
        mFacilitiesAdapter1 = new FacilitiesAdapter1(facilitiesInfoList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                if (facilitiesInfoList.get(position).getIsSelected() == 1)
                {
                    facilitiesInfoList.get(position).setIsSelected(0);
                }
                else
                {
                    facilitiesInfoList.get(position).setIsSelected(1);
                }
                mFacilitiesAdapter1.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(mFacilitiesAdapter1);

        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("id", id);
        valuePairs.put("merchant_id", ConfigManager.instance().getMerchantId());
        DataRequest.instance().request(this, Urls.getFacilitiesListUrl(), this, HttpRequest.POST, GET_FACILITES_lIST, valuePairs,
                new FacilitiesHandler());

    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == tvSubmit)
        {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < facilitiesInfoList.size(); i++)
            {
                if (facilitiesInfoList.get(i).getIsSelected() == 1)
                {
                    sb.append(facilitiesInfoList.get(i).getId());
                    sb.append(",");
                }
            }
            String facilities = sb.toString();
            if (!StringUtils.stringIsEmpty(facilities))
            {
                facilities = facilities.substring(0, facilities.length() - 1);
            }
            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("uid", ConfigManager.instance().getUserID());
            valuePairs.put("id", id);
            valuePairs.put("facilities", facilities);
            valuePairs.put("token", ConfigManager.instance().getToken());
            valuePairs.put("merchant_id", ConfigManager.instance().getMerchantId());
            DataRequest.instance().request(this, Urls.getEditFacilitiesUrl(), this, HttpRequest.POST, EDIT_FACILITES, valuePairs,
                    new ResultHandler());

        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_FACILITES_lIST.equals(action))
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
        else if (EDIT_FACILITES.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(EDIT_FACILITES_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }


}
