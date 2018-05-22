package com.twlrg.twsl.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twlrg.twsl.MyApplication;
import com.twlrg.twsl.R;
import com.twlrg.twsl.activity.LoginActivity;
import com.twlrg.twsl.activity.MainActivity;
import com.twlrg.twsl.adapter.MyViewPagerAdapter;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：王先云 on 2018/4/12 16:50
 * 邮箱：wangxianyun1@163.com
 * 描述：订单
 */
public class OrderListFragment extends BaseFragment implements View.OnClickListener
{
    @BindView(R.id.view)
    View         mViewLayout;
    @BindView(R.id.tv_check)
    TextView     tvCheck;
    @BindView(R.id.tv_leave)
    TextView     tvLeave;
    @BindView(R.id.ll_date)
    LinearLayout llDate;
    @BindView(R.id.et_keyword)
    EditText     etKeyword;
    @BindView(R.id.tabLayout)
    TabLayout    mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager    mViewPager;
    Unbinder unbinder;
    private View rootView = null;


    private String keyword = "", s_date = "", e_date = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_order_list, null);
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
        ((MainActivity) getActivity()).changeTabStatusColor(2);
        if (!MyApplication.getInstance().isLogin())
        {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @Override
    protected void initData()
    {
        s_date = StringUtils.getCurrentTime();
        e_date = StringUtils.getNextMonth();
    }

    @Override
    protected void initViews()
    {
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        mViewLayout = rootView.findViewById(R.id.view);
    }

    @Override
    protected void initEvent()
    {
    }

    @Override
    protected void initViewData()
    {

        tvCheck.setText("起 " + StringUtils.toMonthAndDay(s_date));
        tvLeave.setText("止 " + StringUtils.toMonthAndDay(e_date));

        int widthPixels = APPUtils.getScreenWidth(getActivity());
        //设置状态栏高度
        LinearLayout.LayoutParams topViewParams = new LinearLayout.LayoutParams(widthPixels, APPUtils.getStatusBarHeight(getActivity()));
        mViewLayout.setLayoutParams(topViewParams);
        mViewLayout.setVisibility(View.VISIBLE);
        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(OrderFragment1.newInstance(keyword, s_date, e_date), "待处理");//添加Fragment
        viewPagerAdapter.addFragment(OrderFragment2.newInstance(keyword, s_date, e_date), "今日入住");
        viewPagerAdapter.addFragment(OrderFragment.newInstance(keyword, s_date, e_date), "全部订单");
        mViewPager.setAdapter(viewPagerAdapter);//设置适配器
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.addTab(mTabLayout.newTab().setText("待处理"));//给TabLayout添加Tab
        mTabLayout.addTab(mTabLayout.newTab().setText("今日入住"));
        mTabLayout.addTab(mTabLayout.newTab().setText("全部订单"));
        mTabLayout.setupWithViewPager(mViewPager);//给TabLayout设置关联ViewPager，如果设置了ViewPager，那么ViewPagerAdapter中的getPageTitle()方法返回的就是Tab上的标题


    }

    @Override
    public void onClick(View v)
    {

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
}
