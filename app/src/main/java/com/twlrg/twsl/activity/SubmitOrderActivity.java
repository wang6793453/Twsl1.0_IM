package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.SaleAdapter;
import com.twlrg.twsl.entity.OrderInfo;
import com.twlrg.twsl.entity.SaleInfo;
import com.twlrg.twsl.entity.WxPayInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.OrderInfoHandler;
import com.twlrg.twsl.json.OrderListHandler;
import com.twlrg.twsl.json.ResultHandler;
import com.twlrg.twsl.json.SaleInfoListHandler;
import com.twlrg.twsl.json.WxpayHandler;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.DialogUtils;
import com.twlrg.twsl.utils.PayResult;
import com.twlrg.twsl.utils.StringUtils;
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
 * 作者：王先云 on 2018/4/24 20:18
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class SubmitOrderActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.tv_hotel_name)
    TextView        tvHotelName;
    @BindView(R.id.tv_occupant)
    TextView        tvOccupant;
    @BindView(R.id.tv_room_title)
    TextView        tvRoomTitle;
    @BindView(R.id.tv_days)
    TextView        tvDays;
    @BindView(R.id.tv_phone)
    TextView        tvPhone;
    @BindView(R.id.tv_time)
    TextView        tvTime;
    @BindView(R.id.tv_total_fee)
    TextView        tvTotalFee;
    @BindView(R.id.tv_price_detail)
    TextView        tvPriceDetail;
    @BindView(R.id.tv_cancel_policy)
    TextView        tvCancelPolicy;
    @BindView(R.id.recyclerView)
    RecyclerView    recyclerView;
    @BindView(R.id.btn_submit)
    Button          btnSubmit;
    @BindView(R.id.et_mark)
    EditText        etMark;
    //private SubOrderInfo mSubOrderInfo;
    private SaleAdapter mSaleAdapter;
    private List<SaleInfo> saleInfoList = new ArrayList<>();
    private IWXAPI                 api;
    private String                 orderId;
    private OrderInfo              mOrderInfo;
    private WxpayBroadCastReceiver mWxpayBroadCastReceiver;


    private static final int REQUEST_SUCCESS          = 0x01;
    public static final  int REQUEST_FAIL             = 0x02;
    private static final int GET_SALE_SUCCESS         = 0x03;
    private static final int GET_ORDER_DETAIL_SUCCESS = 0x04;
    private static final int GET_ALI_SUCCESS          = 0x05;
    private static final int GET_WX_SUCCESS           = 0x06;
    private static final int SDK_PAY_FLAG             = 0x07;
    private static final int GET_ORDER_INFO_SUCCESS   = 0x08;
    private final static int WXPAY_SUCCESS_CODE       = 0x09;


    private final static String WXPAY_SUCCESS    = "WXPAY_SUCCESS";
    private static final String GET_ORDER_DETAIL = "get_order_detail";
    private static final String SUB_ORDER        = "sub_order";
    private static final String GET_SALE         = "get_sale";
    private static final String GET_ALI_APY      = "get_ali_apy";
    private static final String GET_WX_APY       = "get_wx_apy";
    private static final String GET_ORDER_INFO   = "get_order_info";


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
                    //ToastUtil.show(SubmitOrderActivity.this, "操作成功!");

                    DialogUtils.showPayDialog(SubmitOrderActivity.this, new MyItemClickListener()
                    {
                        @Override
                        public void onItemClick(View view, int position)
                        {
                            if (position == 0)
                            {
                                toWxpay();
                            }
                            else
                            {
                                toAlipay();
                            }
                        }
                    });
                    //finish();
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(SubmitOrderActivity.this, msg.obj.toString());
                    break;

                case GET_SALE_SUCCESS:
                    SaleInfoListHandler mSaleInfoListHandler = (SaleInfoListHandler) msg.obj;
                    saleInfoList.clear();
                    saleInfoList.addAll(mSaleInfoListHandler.getSaleInfoList());
                    mSaleAdapter.notifyDataSetChanged();
                    break;

                case GET_ORDER_DETAIL_SUCCESS:
                    OrderListHandler mOrderListHandler = (OrderListHandler) msg.obj;
                    DialogUtils.showPriceDetailDialog(SubmitOrderActivity.this, mOrderListHandler.getOrderInfoList());
                    break;

                case GET_ALI_SUCCESS:

                    ResultHandler mResultHandler = (ResultHandler) msg.obj;
                    final String orderInfo = mResultHandler.getData();

                    if (!StringUtils.stringIsEmpty(orderInfo))
                    {

                        Runnable payRunnable = new Runnable()
                        {

                            @Override
                            public void run()
                            {
                                PayTask alipay = new PayTask(SubmitOrderActivity.this);
                                Map<String, String> result = alipay.payV2(orderInfo, true);
                                Log.i("msp", result.toString());
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };

                        Thread payThread = new Thread(payRunnable);
                        payThread.start();

                    }

                    break;

                case GET_WX_SUCCESS:
                    WxpayHandler mWxpayHandler = (WxpayHandler) msg.obj;
                    WxPayInfo mWxPayInfo = mWxpayHandler.getWxPayInfo();

                    if (null != mWxPayInfo)
                    {
                        PayReq req = new PayReq();
                        req.appId = mWxPayInfo.getAppid();
                        req.partnerId = mWxPayInfo.getPartnerid();
                        req.prepayId = mWxPayInfo.getPrepayid();
                        req.nonceStr = mWxPayInfo.getNoncestr();
                        req.timeStamp = mWxPayInfo.getTimestamp();
                        req.packageValue = mWxPayInfo.getPackage1();
                        req.sign = mWxPayInfo.getSign();
                        req.extData = "app data"; // optional
                        api.sendReq(req);
                    }
                    break;


                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000"))
                    {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtil.show(SubmitOrderActivity.this, "支付成功");
                        finish();
                    }
                    else
                    {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.show(SubmitOrderActivity.this, "支付失败");
                    }
                    break;

                case GET_ORDER_INFO_SUCCESS:
                    OrderInfoHandler mOrderInfoHandler = (OrderInfoHandler) msg.obj;
                    mOrderInfo = mOrderInfoHandler.getOrderInfo();

                    if (null != mOrderInfo)
                    {
                        tvHotelName.setText(mOrderInfo.getMerchant());
                        tvOccupant.setText(mOrderInfo.getOccupant());
                        tvPhone.setText(mOrderInfo.getMobile());
                        tvRoomTitle.setText(mOrderInfo.getTitle());
                        tvDays.setText(mOrderInfo.getDays() + "晚  " + mOrderInfo.getBuynum() + "间");
                        tvTime.setText(mOrderInfo.getCheck_in() + " 至 " + mOrderInfo.getCheck_out());
                        tvCancelPolicy.setText(mOrderInfo.getCancel_policy());
                        tvTotalFee.setText("￥" + mOrderInfo.getTotal_fee());

                        if (saleInfoList.isEmpty())
                        {
                            showProgressDialog();
                            Map<String, String> valuePairs = new HashMap<>();
                            valuePairs.put("merchant_id", mOrderInfo.getMerchant_id());
                            DataRequest.instance().request(SubmitOrderActivity.this, Urls.getSaleListUrl(), SubmitOrderActivity
                                            .this, HttpRequest.POST,
                                    GET_SALE, valuePairs,
                                    new SaleInfoListHandler());
                        }
                    }


                    break;

                case WXPAY_SUCCESS_CODE:
                    ToastUtil.show(SubmitOrderActivity.this, "支付成功");
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void initData()
    {
        orderId = getIntent().getStringExtra("ORDER_ID");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {

        setContentView(R.layout.activity_submit_order);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        tvPriceDetail.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        api = WXAPIFactory.createWXAPI(this, ConstantUtil.WX_APPID);


        mWxpayBroadCastReceiver = new WxpayBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WXPAY_SUCCESS);
        registerReceiver(mWxpayBroadCastReceiver, intentFilter);

        tvTitle.setText("订单确认");
        setStatusBarTextDeep(true);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(SubmitOrderActivity.this));
        recyclerView.addItemDecoration(new DividerDecoration(SubmitOrderActivity.this));

        mSaleAdapter = new SaleAdapter(saleInfoList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                for (int i = 0; i < saleInfoList.size(); i++)
                {
                    if (i == position)
                    {
                        saleInfoList.get(i).setSelected(true);
                    }
                    else
                    {
                        saleInfoList.get(i).setSelected(false);
                    }
                }
                mSaleAdapter.notifyDataSetChanged();

            }
        });
        recyclerView.setAdapter(mSaleAdapter);


        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("order_id", orderId);
        DataRequest.instance().request(SubmitOrderActivity.this, Urls.getOrderDetailUrl(), this, HttpRequest.POST, GET_ORDER_INFO, valuePairs,
                new OrderInfoHandler());


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
            //            showProgressDialog();
            //            Map<String, String> valuePairs = new HashMap<>();
            //            valuePairs.put("order_id", mSubOrderInfo.getOrder_id());
            //            valuePairs.put("sale_uid", getSaleUid());
            //            valuePairs.put("remark", etMark.getText().toString());
            //            DataRequest.instance().request(SubmitOrderActivity.this, Urls.getSelectSaleUrl(), this, HttpRequest.POST, SUB_ORDER, valuePairs,
            //                    new ResultHandler());


            if (StringUtils.stringIsEmpty(getSaleUid()))
            {
                ToastUtil.show(SubmitOrderActivity.this, "请选择销售人员");
            }
            else
            {


                showProgressDialog();
                Map<String, String> valuePairs = new HashMap<>();
                valuePairs.put("order_id", orderId);
                valuePairs.put("sale_uid", getSaleUid());
                valuePairs.put("remark", etMark.getText().toString());
                DataRequest.instance().request(SubmitOrderActivity.this, Urls.getSelectSaleUrl(), SubmitOrderActivity.this, HttpRequest.POST, SUB_ORDER,
                        valuePairs, new ResultHandler());
            }


        }
        else if (v == tvPriceDetail)
        {
            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("order_id", orderId);
            DataRequest.instance().request(SubmitOrderActivity.this, Urls.getOrderDetailedUrl(), this, HttpRequest.POST, GET_ORDER_DETAIL, valuePairs, new
                    OrderListHandler());
        }
    }

    private void toAlipay()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("order_id", orderId);
        valuePairs.put("ordcode", mOrderInfo.getOrdcode());
        valuePairs.put("payment_type", "ali");
        valuePairs.put("product", mOrderInfo.getTitle());
        // valuePairs.put("total_fee", mOrderInfo.getTotal_fee());
        valuePairs.put("total_fee", "0.01");
        DataRequest.instance().request(SubmitOrderActivity.this, Urls.getAlipayUrl(), SubmitOrderActivity.this, HttpRequest.POST, GET_ALI_APY,
                valuePairs,
                new ResultHandler());
    }

    private void toWxpay()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("order_id", orderId);
        valuePairs.put("ordcode", mOrderInfo.getOrdcode());
        valuePairs.put("payment_type", "wx");
        valuePairs.put("product", mOrderInfo.getTitle());
        // valuePairs.put("total_fee", mOrderInfo.getTotal_fee());
        valuePairs.put("total_fee", "0.01");
        DataRequest.instance().request(SubmitOrderActivity.this, Urls.getWxpayUrl(), SubmitOrderActivity.this, HttpRequest.POST, GET_WX_APY,
                valuePairs,
                new WxpayHandler());
    }

    private String getSaleUid()
    {
        String sale_uid = "";
        for (int i = 0; i < saleInfoList.size(); i++)
        {
            if (saleInfoList.get(i).isSelected())
            {
                sale_uid = saleInfoList.get(i).getId();
            }
        }

        return sale_uid;
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (SUB_ORDER.equals(action))
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
        else if (GET_SALE.equals(action))
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
        else if (GET_ORDER_DETAIL.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_ORDER_DETAIL_SUCCESS, obj));
            }

            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_ALI_APY.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_ALI_SUCCESS, obj));
            }

            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_WX_APY.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_WX_SUCCESS, obj));
            }

            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }

        else if (GET_ORDER_INFO.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_ORDER_INFO_SUCCESS, obj));
            }

            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (null != mWxpayBroadCastReceiver)
        {
            unregisterReceiver(mWxpayBroadCastReceiver);
        }

    }


    //微信支付回调
    class WxpayBroadCastReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {

            if (WXPAY_SUCCESS.contentEquals(intent.getAction()))
            {

                mHandler.sendEmptyMessageDelayed(WXPAY_SUCCESS_CODE, 500);
            }
        }
    }

}
