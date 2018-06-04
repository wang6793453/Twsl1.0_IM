package com.twlrg.twsl.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.twlrg.twsl.MyApplication;
import com.twlrg.twsl.R;
import com.twlrg.twsl.activity.BaseHandler;
import com.twlrg.twsl.activity.OrderDetailActivity;
import com.twlrg.twsl.adapter.OrderAdapter;
import com.twlrg.twsl.entity.OrderInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.OrderListHandler;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.EmptyDecoration;
import com.twlrg.twsl.widget.list.refresh.PullToRefreshBase;
import com.twlrg.twsl.widget.list.refresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：王先云 on 2018/4/12 16:50
 * 邮箱：wangxianyun1@163.com
 * 描述：订单
 */
public class OrderFragment1 extends BaseFragment implements PullToRefreshBase.OnRefreshListener<RecyclerView>, IRequestListener
{
    @BindView(R.id.btn_load)
    Button                    btnLoad;
    @BindView(R.id.ll_no_data)
    LinearLayout              llNoData;
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    private View rootView = null;
    private Unbinder unbinder;

    private RecyclerView mRecyclerView;
    private int pn = 1;
    private int mRefreshStatus;
    private List<OrderInfo> orderInfoList = new ArrayList<>();
    private OrderAdapter mOrderAdapter;
    private static final String GET_ORDER_LIST = "get_order_list";

    private static final int    REQUEST_SUCCESS = 0x01;
    private static final int    REQUEST_FAIL    = 0x02;
    private              String keyword         = "", s_date = "", e_date = "";

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(getActivity())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    OrderListHandler mOrderListHandler = (OrderListHandler) msg.obj;

                    if(pn==1)
                    {
                        orderInfoList.clear();
                    }

                    orderInfoList.addAll(mOrderListHandler.getOrderInfoList());
                    mOrderAdapter.notifyDataSetChanged();
                    if (orderInfoList.isEmpty())
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
                    // ToastUtil.show(getActivity(), msg.obj.toString());
                    if (orderInfoList.isEmpty())
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

    private static OrderFragment1 instance = null;

    public static OrderFragment1 newInstance(String keyword, String s_date, String e_date)
    {
        if (instance == null)
        {
            instance = new OrderFragment1();
        }
        Bundle b = new Bundle();
        b.putString("keyword", keyword);
        b.putString("s_date", s_date);
        b.putString("e_date", e_date);
        instance.setArguments(b);

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_order, null);
            unbinder = ButterKnife.bind(this, rootView);
            initData();
            initViews();
            initViewData();
            initEvent();
        }
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

        if (MyApplication.getInstance().isLogin())
        {
            orderInfoList.clear();
            pn = 1;
            mRefreshStatus = 0;
            getOrderList();
        }


    }

    @Override
    protected void initData()
    {
        keyword = String.valueOf(getArguments().get("keyword"));
        s_date = String.valueOf(getArguments().get("s_date"));
        e_date = String.valueOf(getArguments().get("e_date"));
    }

    @Override
    protected void initViews()
    {

    }

    @Override
    protected void initEvent()
    {
        btnLoad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                orderInfoList.clear();
                pn = 1;
                mRefreshStatus = 0;
                getOrderList();
            }
        });
    }

    @Override
    protected void initViewData()
    {
        mPullToRefreshRecyclerView.setPullLoadEnabled(true);
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        mPullToRefreshRecyclerView.setOnRefreshListener(this);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new EmptyDecoration(getActivity(), ""));

        mOrderAdapter = new OrderAdapter(orderInfoList, getActivity(), new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                startActivity(new Intent(getActivity(), OrderDetailActivity.class).putExtra("ORDER_ID", orderInfoList.get(position).getId()));
            }
        }
        );
        mRecyclerView.setAdapter(mOrderAdapter);


    }

    private void getOrderList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("uid", ConfigManager.instance().getUserID());
        valuePairs.put("token", ConfigManager.instance().getToken());
        valuePairs.put("role", "2");
        valuePairs.put("type", "2");
        valuePairs.put("page", pn + "");
        valuePairs.put("keyword", keyword);
        valuePairs.put("s_date", s_date);
        valuePairs.put("e_date", e_date);
        DataRequest.instance().request(getActivity(), Urls.getOrderListUrl(), this, HttpRequest.POST, GET_ORDER_LIST, valuePairs,
                new OrderListHandler());
    }


    public void searchOrder(String keyword, String s_date, String e_date)
    {
        this.keyword = keyword;
        this.s_date = s_date;
        this.e_date = e_date;
        orderInfoList.clear();
        pn = 1;
        mRefreshStatus = 0;
        getOrderList();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        orderInfoList.clear();
        pn = 1;
        mRefreshStatus = 0;
        getOrderList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        pn += 1;
        mRefreshStatus = 1;
        getOrderList();
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (mRefreshStatus == 1)
        {
            mPullToRefreshRecyclerView.onPullUpRefreshComplete();
        }
        else
        {
            mPullToRefreshRecyclerView.onPullDownRefreshComplete();
        }

        if (GET_ORDER_LIST.equals(action))
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
