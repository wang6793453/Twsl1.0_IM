package com.twlrg.twsl.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.HotelInfo;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.widget.AutoFitTextView;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/24 10:27
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class HotelPolicyActivity extends BaseActivity
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.tv_time)
    TextView        tvTime;
    @BindView(R.id.tv_children_policy)
    TextView        tvChildrenPolicy;
    @BindView(R.id.tv_meal_plans)
    TextView        tvMealPlans;
    @BindView(R.id.tv_pet_policy)
    TextView        tvPetPolicy;

    private HotelInfo mHotelInfo;

    @Override
    protected void initData()
    {
        mHotelInfo = (HotelInfo) getIntent().getSerializableExtra("HOTEL");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_hotel_policy);
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
        tvTitle.setText("酒店政策");
        setStatusBarTextDeep(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));

        if (null != mHotelInfo)
        {
            tvTime.setText(mHotelInfo.getIn_out());
            tvChildrenPolicy.setText(mHotelInfo.getChildren_policy());
            tvMealPlans.setText(mHotelInfo.getMeal_plans());
            tvPetPolicy.setText(mHotelInfo.getPet_policy());
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
    }
}
