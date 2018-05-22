package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.OrderInfo;
import com.twlrg.twsl.entity.WxPayInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.im.ui.ChatActivity;
import com.twlrg.twsl.json.OrderInfoHandler;
import com.twlrg.twsl.json.OrderListHandler;
import com.twlrg.twsl.json.ResultHandler;
import com.twlrg.twsl.json.WxpayHandler;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.DialogUtils;
import com.twlrg.twsl.utils.PayResult;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;
import com.twlrg.twsl.widget.CircleImageView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/23 13:47
 * 邮箱：wangxianyun1@163.com
 * 描述：订单详情
 */
public class OrderDetailActivity extends BaseActivity implements IRequestListener
{


    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.tv_merchant)
    AutoFitTextView tvMerchant;
    @BindView(R.id.tv_name)
    TextView        tvName;
    @BindView(R.id.tv_mobile)
    TextView        tvMobile;
    @BindView(R.id.tv_room)
    TextView        tvRoom;
    @BindView(R.id.tv_buynum)
    TextView        tvBuynum;
    @BindView(R.id.tv_time)
    TextView        tvTime;
    @BindView(R.id.tv_days)
    TextView        tvDays;
    @BindView(R.id.tv_status)
    TextView        tvStatus;
    @BindView(R.id.tv_total_fee)
    TextView        tvTotalFee;
    @BindView(R.id.tv_price_detail)
    TextView        tvPriceDetail;
    @BindView(R.id.iv_user_head)
    CircleImageView ivUserHead;
    @BindView(R.id.tv_salesperson)
    TextView        tvSalesperson;
    @BindView(R.id.tv_position)
    TextView        tvPosition;
    @BindView(R.id.iv_message)
    ImageView       ivMessage;
    @BindView(R.id.iv_phone)
    ImageView       ivPhone;
    @BindView(R.id.rl_sale)
    RelativeLayout  rlSale;
    @BindView(R.id.btn_cancel)
    Button          btnCancel;
    @BindView(R.id.btn_status)
    Button          btnStatus;
    @BindView(R.id.tv_fwf)
    TextView        tvFwf;
    @BindView(R.id.tv_cancel_policy)
    TextView        tvCancelPolicy;
    @BindView(R.id.tv_requirement)
    TextView        tvRequirement;
    @BindView(R.id.tv_invoice)
    TextView        tvInvoice;
    @BindView(R.id.ll_bottom)
    LinearLayout    llBottom;
    private String    order_id;
    private OrderInfo mOrderInfo;


    private IWXAPI api;

    private static final int REQUEST_SUCCESS          = 0x01;
    public static final  int REQUEST_FAIL             = 0x02;
    private static final int ORDER_CANCEL_SUCCESS     = 0x03;
    private static final int GET_ORDER_DETAIL_SUCCESS = 0x04;
    private static final int GET_ALI_SUCCESS          = 0x05;
    private static final int GET_WX_SUCCESS           = 0x06;
    private static final int SDK_PAY_FLAG             = 0x07;
    private final static int WXPAY_SUCCESS_CODE       = 0x09;


    private final static String WXPAY_SUCCESS    = "WXPAY_SUCCESS";
    private static final String GET_ORDER_INFO   = "get_order_info";
    private static final String ORDER_CANCEL     = "order_cancel";
    private static final String GET_ORDER_DETAIL = "get_order_detail";
    private static final String GET_ALI_APY      = "get_ali_apy";
    private static final String GET_WX_APY       = "get_wx_apy";


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
                    OrderInfoHandler mOrderInfoHandler = (OrderInfoHandler) msg.obj;
                    mOrderInfo = mOrderInfoHandler.getOrderInfo();

                    if (null != mOrderInfo)
                    {
                        tvTitle.setText("订单号" + mOrderInfo.getOrdcode());
                        tvName.setText(mOrderInfo.getOccupant());
                        tvMobile.setText(mOrderInfo.getMobile());
                        tvMerchant.setText(mOrderInfo.getMerchant());
                        tvTotalFee.setText("总额:￥" + mOrderInfo.getTotal_fee());

                        tvTime.setText(mOrderInfo.getCheck_in() + "至" + mOrderInfo.getCheck_out());

                        String zc = getZc(mOrderInfo.getPrice_type());
                        tvInvoice.setText(mOrderInfo.getInvoice());
                        tvCancelPolicy.setText(mOrderInfo.getCancel_policy());
                        tvRequirement.setText(mOrderInfo.getRemark());
                        tvRoom.setText(mOrderInfo.getTitle() + "[" + zc + "]");
                        tvBuynum.setText(mOrderInfo.getBuynum() + "间");
                        tvDays.setText(mOrderInfo.getDays() + "晚");
                        tvStatus.setText("0".equals(mOrderInfo.getPayment_trade_status()) ? "未支付" : "已支付");

                        if (StringUtils.stringIsEmpty(mOrderInfo.getSalesperson()) || "-".equals(mOrderInfo.getSalesperson()))
                        {
                            rlSale.setVisibility(View.GONE);
                        }
                        else
                        {
                            rlSale.setVisibility(View.VISIBLE);
                        }

                        ImageLoader.getInstance().displayImage(Urls.getImgUrl(mOrderInfo.getPortrait()), ivUserHead);
                        tvSalesperson.setText(mOrderInfo.getSalesperson());
                        tvPosition.setText(mOrderInfo.getPosition());


                        switch (mOrderInfo.getIs_used())
                        {
                            case 0:
                                if ("1".equals(mOrderInfo.getPayment_trade_status()))
                                {
                                    btnCancel.setVisibility(View.VISIBLE);
                                    btnStatus.setText("待酒店确认");
                                    btnStatus.setEnabled(false);
                                }
                                else
                                {
                                    btnCancel.setVisibility(View.VISIBLE);
                                    btnStatus.setText("去支付");
                                    btnStatus.setEnabled(true);
                                }

                                break;
                            case 1:
                                btnCancel.setVisibility(View.VISIBLE);
                                btnStatus.setText("已预订成功");
                                btnStatus.setEnabled(false);

                                break;
                            case 2:
                                btnCancel.setVisibility(View.GONE);
                                btnStatus.setText("酒店满房拒单");
                                btnStatus.setEnabled(false);
                                break;
                            case 3:
                                btnCancel.setVisibility(View.GONE);
                                btnStatus.setText("取消确认中");
                                btnStatus.setEnabled(false);
                                break;
                            case 4:
                                btnCancel.setVisibility(View.GONE);
                                btnStatus.setText("已取消");
                                btnStatus.setEnabled(false);
                                break;
                            case 5:
                                btnCancel.setVisibility(View.GONE);
                                btnStatus.setText("酒店拒绝取消");
                                btnStatus.setEnabled(false);
                                break;
                        }


                    }

                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(OrderDetailActivity.this, msg.obj.toString());
                    break;


                case ORDER_CANCEL_SUCCESS:
                    ToastUtil.show(OrderDetailActivity.this, "申请退订提交成功");
                    finish();
                    break;

                case GET_ORDER_DETAIL_SUCCESS:
                    OrderListHandler mOrderListHandler = (OrderListHandler) msg.obj;
                    DialogUtils.showPriceDetailDialog(OrderDetailActivity.this, mOrderListHandler.getOrderInfoList());
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
                                PayTask alipay = new PayTask(OrderDetailActivity.this);
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
                        ToastUtil.show(OrderDetailActivity.this, "支付成功");
                    }
                    else
                    {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.show(OrderDetailActivity.this, "支付失败");
                    }
                    break;

                case WXPAY_SUCCESS_CODE:
                    ToastUtil.show(OrderDetailActivity.this, "支付成功");
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void initData()
    {
        order_id = getIntent().getStringExtra("ORDER_ID");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_order_detail);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        ivPhone.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnStatus.setOnClickListener(this);
        tvPriceDetail.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        api = WXAPIFactory.createWXAPI(this, ConstantUtil.WX_APPID);
        setStatusBarTextDeep(true);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));


    }


    @Override
    protected void onResume()
    {
        super.onResume();
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("order_id", order_id);
        DataRequest.instance().request(OrderDetailActivity.this, Urls.getOrderDetailUrl(), this, HttpRequest.POST, GET_ORDER_INFO, valuePairs,
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
        else if (v == ivPhone)
        {
            if (null != mOrderInfo && !StringUtils.stringIsEmpty(mOrderInfo.getSale_mobile()))
            {
                APPUtils.callPhone(OrderDetailActivity.this, mOrderInfo.getSale_mobile());
            }
            else
            {
                ToastUtil.show(OrderDetailActivity.this, "暂时无法电话联系销售人员,请尝试其他方式");
            }
        }
        else if (v == ivMessage)
        {
            ChatActivity.navToChat(this, "slbl_serve_" + mOrderInfo.getSale_uid(), TIMConversationType.C2C);
        }
        else if (v == btnCancel)//取消订单
        {

            DialogUtils.showToastDialog2Button(this, "是否确认取消订单", new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showProgressDialog();
                    Map<String, String> valuePairs = new HashMap<>();
                    valuePairs.put("token", ConfigManager.instance().getToken());
                    valuePairs.put("uid", ConfigManager.instance().getUserID());
                    valuePairs.put("order_id", order_id);
                    DataRequest.instance().request(OrderDetailActivity.this, Urls.getOrderCancelUrl(), OrderDetailActivity.this, HttpRequest.POST, ORDER_CANCEL, valuePairs,
                            new ResultHandler());
                }
            });


        }
        else if (v == btnStatus)//去支付
        {

            //            SubOrderInfo mSubOrderInfo = new SubOrderInfo();
            //            mSubOrderInfo.setBuynum(mOrderInfo.getBuynum());
            //            mSubOrderInfo.setOccupant(mOrderInfo.getOccupant());
            //            mSubOrderInfo.setHotelName(mOrderInfo.getMerchant());
            //            mSubOrderInfo.setRoomTitle(mOrderInfo.getTitle() + "(" + getZc(mOrderInfo.getPrice_type()) + ")");
            //            mSubOrderInfo.setS_data(mOrderInfo.getCheck_in());
            //            mSubOrderInfo.setE_data(mOrderInfo.getCheck_out());
            //            mSubOrderInfo.setPhone(mOrderInfo.getMobile());
            //            mSubOrderInfo.setDays(mOrderInfo.getDays());
            //            mSubOrderInfo.setTotal_feel(mOrderInfo.getTotal_fee());
            //            mSubOrderInfo.setMerchant_id(mOrderInfo.getMerchant_id());
            //            mSubOrderInfo.setOrder_id(mOrderInfo.getId());
            //            Bundle b = new Bundle();
            //            b.putSerializable("SubOrderInfo", mSubOrderInfo);
            //            startActivity(new Intent(OrderDetailActivity.this, SubmitOrderActivity.class).putExtras(b));

            if (StringUtils.stringIsEmpty(mOrderInfo.getSalesperson()) || "-".equals(mOrderInfo.getSalesperson()))
            {
                startActivity(new Intent(OrderDetailActivity.this, SubmitOrderActivity.class).putExtra("ORDER_ID", order_id));
            }
            else
            {
                DialogUtils.showPayDialog(OrderDetailActivity.this, new MyItemClickListener()
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
            }


        }
        else if (v == tvPriceDetail)
        {
            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("order_id", order_id);
            DataRequest.instance().request(OrderDetailActivity.this, Urls.getOrderDetailedUrl(), this, HttpRequest.POST, GET_ORDER_DETAIL, valuePairs,
                    new OrderListHandler());
        }
    }


    private void toAlipay()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("order_id", order_id);
        valuePairs.put("ordcode", mOrderInfo.getOrdcode());
        valuePairs.put("payment_type", "ali");
        valuePairs.put("product", mOrderInfo.getTitle());
        valuePairs.put("total_fee", mOrderInfo.getTotal_fee());
        //valuePairs.put("total_fee", "0.01");
        DataRequest.instance().request(OrderDetailActivity.this, Urls.getAlipayUrl(), OrderDetailActivity.this, HttpRequest.POST, GET_ALI_APY, valuePairs,
                new ResultHandler());
    }

    private void toWxpay()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("order_id", order_id);
        valuePairs.put("ordcode", mOrderInfo.getOrdcode());
        valuePairs.put("payment_type", "wx");
        valuePairs.put("product", mOrderInfo.getTitle());
        valuePairs.put("total_fee", mOrderInfo.getTotal_fee());
        DataRequest.instance().request(OrderDetailActivity.this, Urls.getWxpayUrl(), OrderDetailActivity.this, HttpRequest.POST, GET_WX_APY, valuePairs,
                new WxpayHandler());
    }

    private String getZc(String price_type)
    {
        String zc = "无早餐";
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

        return zc;
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_ORDER_INFO.equals(action))
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
        else if (ORDER_CANCEL.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(ORDER_CANCEL_SUCCESS, obj));
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
