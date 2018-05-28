package com.twlrg.twsl.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.twlrg.twsl.MyApplication;
import com.twlrg.twsl.R;
import com.twlrg.twsl.activity.BillListActivity;
import com.twlrg.twsl.activity.CommentListActivity;
import com.twlrg.twsl.activity.ConferenceManageActivity;
import com.twlrg.twsl.activity.FacilitiesActivity;
import com.twlrg.twsl.activity.HotelDetailActivity;
import com.twlrg.twsl.activity.LocationActivity;
import com.twlrg.twsl.activity.MainActivity;
import com.twlrg.twsl.activity.MyCenterActivity;
import com.twlrg.twsl.activity.PictureManageActivity;
import com.twlrg.twsl.activity.RoomManageActivity;
import com.twlrg.twsl.activity.RoomPriceListActivity;
import com.twlrg.twsl.activity.RoomStatusListActivity;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConfigManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：王先云 on 2018/5/12 16:50
 * 邮箱：wangxianyun1@163.com
 * 描述：我的
 */
public class UserCenterFragment1 extends BaseFragment implements View.OnClickListener
{
    @BindView(R.id.topView)
    View         topView;
    @BindView(R.id.ll_ftwh)
    LinearLayout llFtwh;
    @BindView(R.id.ll_fjwh)
    LinearLayout llFjwh;
    @BindView(R.id.ll_jddp)
    LinearLayout llJddp;
    @BindView(R.id.ll_zdzf)
    LinearLayout llZdzf;
    @BindView(R.id.ll_kfgl)
    LinearLayout llKfgl;
    @BindView(R.id.ll_hysgl)
    LinearLayout llHysgl;
    @BindView(R.id.ll_ctgl)
    LinearLayout llCtgl;
    @BindView(R.id.ll_tpgl)
    LinearLayout llTpgl;
    @BindView(R.id.ll_jdss)
    LinearLayout llJdss;
    @BindView(R.id.ll_jdxx)
    LinearLayout llJdxx;
    @BindView(R.id.ll_jdzz)
    LinearLayout llJdzz;
    @BindView(R.id.ll_grzc)
    LinearLayout llGrzc;
    private View rootView = null;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_user_center1, null);
            unbinder = ButterKnife.bind(this, rootView);
            initData();
            initViews();
            initViewData();
            initEvent();
        }
        // 缓存的rootView需要判断是否已经被加过parent
        // 如果有parent需要从parent删除，否则会发生这个rootView已经有parent的错误
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }

        return rootView;
    }


    @Override
    public void onResume()
    {
        super.onResume();
        ((MainActivity) getActivity()).changeTabStatusColor(3);
    }

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews()
    {

    }

    @Override
    protected void initEvent()
    {
        llFtwh.setOnClickListener(this);
        llFjwh.setOnClickListener(this);
        llJddp.setOnClickListener(this);
        llZdzf.setOnClickListener(this);
        llKfgl.setOnClickListener(this);
        llHysgl.setOnClickListener(this);
        llCtgl.setOnClickListener(this);
        llTpgl.setOnClickListener(this);
        llJdss.setOnClickListener(this);
        llJdxx.setOnClickListener(this);
        llJdzz.setOnClickListener(this);
        llGrzc.setOnClickListener(this);

    }

    @Override
    protected void initViewData()
    {
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(getActivity())));

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (null != unbinder)
        {
            unbinder.unbind();
            unbinder = null;
        }
    }


    @Override
    public void onClick(View v)
    {

        //房态维护
        if (v == llFtwh)
        {
            gotoActivity(RoomStatusListActivity.class);
        }
        //房价维护
        else if (v == llFjwh)
        {
            gotoActivity(RoomPriceListActivity.class);
        }

        else if (v == llJddp)
        {
            if (MyApplication.getInstance().isLogin())
            {
                startActivity(new Intent(getActivity(), CommentListActivity.class).putExtra("MERCHANT_ID", ConfigManager.instance().getMerchantId()));
            }
            else
            {
                startActivity(new Intent(getActivity(), LocationActivity.class));
            }

        }
        else if (v == llZdzf)
        {
            gotoActivity(BillListActivity.class);
        }
        else if (v == llKfgl)
        {
            gotoActivity(RoomManageActivity.class);
        }
        else if (v == llHysgl)
        {
            gotoActivity(ConferenceManageActivity.class);

        }
        else if (v == llCtgl)
        {

        }
        else if (v == llTpgl)
        {
            gotoActivity(PictureManageActivity.class);
        }
        else if (v == llJdss)
        {
            gotoActivity(FacilitiesActivity.class);

        }
        else if (v == llJdxx)
        {

        }
        else if (v == llJdzz)
        {

        }
        else if (v == llGrzc)
        {
            gotoActivity(MyCenterActivity.class);

        }
    }


    private void gotoActivity(Class mClass)
    {
        if (MyApplication.getInstance().isLogin())
        {
            startActivity(new Intent(getActivity(), mClass));
        }
        else
        {
            startActivity(new Intent(getActivity(), LocationActivity.class));
        }
    }


}
