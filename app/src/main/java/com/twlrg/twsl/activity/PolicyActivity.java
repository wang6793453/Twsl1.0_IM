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

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.HotelInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.HotelInfoHandler;
import com.twlrg.twsl.json.ResultHandler;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.StringUtils;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：王先云 on 2018/5/29 14:59
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class PolicyActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.et_in_out)
    EditText        etInOut;
    @BindView(R.id.et_meal_plans)
    EditText        etMealPlans;

    @BindView(R.id.et_children_policy)
    EditText etChildrenPolicy;
    @BindView(R.id.et_pet_policy)
    EditText etPetPolicy;
    @BindView(R.id.btn_submit)
    Button   btnSubmit;
    @BindView(R.id.et_cancel_policy)
    EditText etCancelPolicy;
    @BindView(R.id.et_invoice)
    EditText etInvoice;
    private HotelInfo mHotelInfo;
    private static final int    REQUEST_SUCCESS    = 0x01;
    public static final  int    REQUEST_FAIL       = 0x02;
    private static final int    EDIT_HOTEL_SUCCESS = 0x03;
    private static final String GET_HOTEL_INFO     = "get_hotel_info";
    private static final String EDIT_HOTEL_INFO    = "edit_hotel_info";

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

                    HotelInfoHandler mHotelInfoHandler = (HotelInfoHandler) msg.obj;
                    mHotelInfo = mHotelInfoHandler.getHotelInfo();

                    if (null != mHotelInfo)
                    {
                        etInOut.setText(mHotelInfo.getIn_out());
                        etCancelPolicy.setText(mHotelInfo.getCancel_policy());
                        etChildrenPolicy.setText(mHotelInfo.getChildren_policy());
                        etPetPolicy.setText(mHotelInfo.getPet_policy());
                        etInvoice.setText(mHotelInfo.getInvoice());
                        etMealPlans.setText(mHotelInfo.getMeal_plans());

                    }

                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(PolicyActivity.this, msg.obj.toString());
                    break;

                case EDIT_HOTEL_SUCCESS:
                    ToastUtil.show(PolicyActivity.this, "操作成功");
                    finish();
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
        setContentView(R.layout.activity_policy);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(true);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("酒店政策");
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("merchant_id", ConfigManager.instance().getMerchantId());
        DataRequest.instance().request(this, Urls.getHotelInfoUrl(), this, HttpRequest.POST, GET_HOTEL_INFO, valuePairs,
                new HotelInfoHandler());
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

            String mInOut = etInOut.getText().toString();
            String mChildrenPolicy = etChildrenPolicy.getText().toString();
            String mCancelPolicy = etCancelPolicy.getText().toString();
            String mPetPolicy = etPetPolicy.getText().toString();
            String mInvoice = etInvoice.getText().toString();
            String mtMealPlans = etMealPlans.getText().toString();

            if (StringUtils.stringIsEmpty(mInOut))
            {
                ToastUtil.show(this, "请输入入住离时间");
                return;
            }
            if (StringUtils.stringIsEmpty(mChildrenPolicy))
            {
                ToastUtil.show(this, "请输入儿童政策");
                return;
            }
            if (StringUtils.stringIsEmpty(mPetPolicy))
            {
                ToastUtil.show(this, "请输入宠物政策");
                return;
            }

            if (StringUtils.stringIsEmpty(mtMealPlans))
            {
                ToastUtil.show(this, "请输入早餐政策");
                return;
            }
            if (StringUtils.stringIsEmpty(mCancelPolicy))
            {
                ToastUtil.show(this, "请输入取消政策");
                return;
            }
            if (StringUtils.stringIsEmpty(mInvoice))
            {
                ToastUtil.show(this, "请输入发票政策");
                return;
            }

            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("uid", ConfigManager.instance().getUserID());
            valuePairs.put("token", ConfigManager.instance().getToken());
            valuePairs.put("merchant_id", ConfigManager.instance().getMerchantId());
            valuePairs.put("region", mHotelInfo.getRegion());
            valuePairs.put("address", mHotelInfo.getAddress());
            valuePairs.put("phone", mHotelInfo.getPhone());
            valuePairs.put("fax", mHotelInfo.getFax());
            valuePairs.put("brand", mHotelInfo.getBrand());
            valuePairs.put("rooms", mHotelInfo.getRooms());
            valuePairs.put("opening_time", mHotelInfo.getOpening_time());
            valuePairs.put("decoration_time", mHotelInfo.getDecoration_time());
            valuePairs.put("summary", mHotelInfo.getSummary());
            valuePairs.put("in_out", mInOut);
            valuePairs.put("children_policy", mChildrenPolicy);
            valuePairs.put("meal_plans", mtMealPlans);
            valuePairs.put("pet_policy", mPetPolicy);
            valuePairs.put("cancel_policy", mCancelPolicy);
            valuePairs.put("invoice", mInvoice);
            valuePairs.put("position_label", mHotelInfo.getPosition_label());
            valuePairs.put("service_label", mHotelInfo.getService_label());
            valuePairs.put("reviews_label", mHotelInfo.getReviews_label());
            valuePairs.put("restaurant", mHotelInfo.getRestaurant());
            valuePairs.put("shopping", mHotelInfo.getShopping());
            valuePairs.put("entertainment", mHotelInfo.getEntertainment());
            valuePairs.put("metro_station", mHotelInfo.getMetro_station());
            valuePairs.put("scenic_spot", mHotelInfo.getScenic_spot());


            DataRequest.instance().request(this, Urls.getEditHotelInfoUrl(), this, HttpRequest.POST, EDIT_HOTEL_INFO, valuePairs,
                    new ResultHandler());
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_HOTEL_INFO.equals(action))
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
        else if (EDIT_HOTEL_INFO.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(EDIT_HOTEL_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }


}
