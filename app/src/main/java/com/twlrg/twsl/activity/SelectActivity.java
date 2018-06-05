package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.SelectCityAdapter;
import com.twlrg.twsl.entity.CityInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.CityListHandler;
import com.twlrg.twsl.listener.MyItemClickListener;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.EmptyDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/6/5 14:58
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class SelectActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView    ivBack;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private SelectCityAdapter mSelectCityAdapter;
    private List<CityInfo> cityInfoList = new ArrayList<>();

    private static final int REQUEST_FAIL     = 0x02;
    private static final int GET_CITY_SUCCESS = 0x03;

    private static final String GET_CITY_LIST = "GET_CITY_LIST";

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(SelectActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {

                case REQUEST_FAIL:
                    ToastUtil.show(SelectActivity.this, msg.obj.toString());

                    break;

                case GET_CITY_SUCCESS:
                    CityListHandler mCityListHandler = (CityListHandler) msg.obj;
                    cityInfoList.addAll(mCityListHandler.getCityInfoList());
                    mSelectCityAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_selecte_city);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    @Override
    protected void initViewData()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new EmptyDecoration(this, ""));
        mSelectCityAdapter = new SelectCityAdapter(cityInfoList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                Intent intent = new Intent();
                intent.putExtra("city_id", cityInfoList.get(position).getId());
                intent.putExtra("city_name", cityInfoList.get(position).getName());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        recyclerView.setAdapter(mSelectCityAdapter);

        Map<String, String> valuePairs = new HashMap<>();
        DataRequest.instance().request(this, Urls.getCityListUrl(), this, HttpRequest.POST, GET_CITY_LIST, valuePairs,
                new CityListHandler());
    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (GET_CITY_LIST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_CITY_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }
}
