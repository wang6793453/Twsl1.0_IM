package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.HotelSaleAdapter;
import com.twlrg.twsl.entity.SaleInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.SaleInfoListHandler;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;
import com.twlrg.twsl.widget.DividerDecoration;
import com.twlrg.twsl.widget.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/5/17 15:29
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class HotelSalesmanListActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView    recyclerView;
    private HotelSaleAdapter mSaleAdapter;
    private List<SaleInfo> saleInfoList = new ArrayList<>();

    private String merchant_id;

    private static final int    GET_SALE_SUCCESS = 0x03;
    public static final  int    REQUEST_FAIL     = 0x02;
    private static final String GET_SALE         = "get_sale";


    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {


                case REQUEST_FAIL:
                    ToastUtil.show(HotelSalesmanListActivity.this, msg.obj.toString());
                    break;

                case GET_SALE_SUCCESS:
                    SaleInfoListHandler mSaleInfoListHandler = (SaleInfoListHandler) msg.obj;
                    saleInfoList.clear();
                    saleInfoList.addAll(mSaleInfoListHandler.getSaleInfoList());
                    mSaleAdapter.notifyDataSetChanged();
                    break;


            }
        }
    };

    @Override
    protected void initData()
    {
        merchant_id = getIntent().getStringExtra("MERCHANT_ID");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_hotel_sales);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("销售列表");
        setStatusBarTextDeep(true);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(HotelSalesmanListActivity.this));
        recyclerView.addItemDecoration(new DividerDecoration(HotelSalesmanListActivity.this));

        mSaleAdapter = new HotelSaleAdapter(saleInfoList, HotelSalesmanListActivity.this);
        recyclerView.setAdapter(mSaleAdapter);
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("merchant_id", merchant_id);
        DataRequest.instance().request(HotelSalesmanListActivity.this, Urls.getSaleListUrl(), HotelSalesmanListActivity
                        .this, HttpRequest.POST,
                GET_SALE, valuePairs,
                new SaleInfoListHandler());
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_SALE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_SALE_SUCCESS, obj));
            }

            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }
}
