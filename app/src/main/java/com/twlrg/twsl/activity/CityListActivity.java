package com.twlrg.twsl.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.twlrg.twsl.R;
import com.twlrg.twsl.adapter.CityAdapter;
import com.twlrg.twsl.entity.CityInfo;
import com.twlrg.twsl.http.DataRequest;
import com.twlrg.twsl.http.HttpRequest;
import com.twlrg.twsl.http.IRequestListener;
import com.twlrg.twsl.json.CityListHandler;
import com.twlrg.twsl.utils.APPUtils;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.ToastUtil;
import com.twlrg.twsl.utils.Urls;
import com.twlrg.twsl.widget.AutoFitTextView;
import com.twlrg.twsl.widget.sidebar.DigitalUtil;
import com.twlrg.twsl.widget.sidebar.IndexBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/27 15:41
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class CityListActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.index_bar)
    IndexBar        indexBar;
    @BindView(R.id.tv_letter)
    TextView        mTvLetter;
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.lv)
    ListView        mLv;


    private              List<CityInfo>    mCityList = new ArrayList<>();
    private              ArrayList<String> letters   = new ArrayList<>();
    private static final String            TAG       = CityListActivity.class.getSimpleName();


    private static final int REQUEST_FAIL     = 0x02;
    private static final int GET_CITY_SUCCESS = 0x03;

    private static final String GET_CITY_LIST = "GET_CITY_LIST";

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(CityListActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {

                case REQUEST_FAIL:
                    ToastUtil.show(CityListActivity.this, msg.obj.toString());

                    break;

                case GET_CITY_SUCCESS:
                    CityListHandler mCityListHandler = (CityListHandler) msg.obj;
                    mCityList.addAll(mCityListHandler.getCityInfoList());
                    fillNameAndSort();
                    mLv.setAdapter(new CityAdapter(mCityList));
                    indexBar.setLetters(letters);
                    indexBar.setOnLetterChangeListener(new IndexBar.OnLetterChangeListener()
                    {
                        @Override
                        public void onLetterChange(int position, String letter)
                        {
                            showTextView(letter);
                            if ("#".equals(letter))
                            {
                                mLv.setSelection(0);
                                return;
                            }
                            for (int i = 0; i < mCityList.size(); i++)
                            {
                                CityInfo girl = mCityList.get(i);
                                String pinyin = girl.getPinyin();
                                String firstPinyin = String.valueOf(TextUtils.isEmpty(pinyin) ? girl.getName().charAt(0) : pinyin.charAt(0));
                                if (letter.compareToIgnoreCase(firstPinyin) == 0)
                                {
                                    mLv.setSelection(i);
                                    break;
                                }
                            }
                        }
                    });
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

        setContentView(R.layout.activity_city_list);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);

        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent();
                intent.putExtra("city_id", mCityList.get(position).getId());
                intent.putExtra("city_name", mCityList.get(position).getName());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(false);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("城市列表");
        Map<String, String> valuePairs = new HashMap<>();
        DataRequest.instance().request(this, Urls.getCityListUrl(), this, HttpRequest.POST, GET_CITY_LIST, valuePairs,
                new CityListHandler());
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

    private Handler mHander = new Handler();

    protected void showTextView(String letter)
    {
        mTvLetter.setVisibility(View.VISIBLE);
        mTvLetter.setText(letter);
        mHander.removeCallbacksAndMessages(null);
        mHander.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                mTvLetter.setVisibility(View.INVISIBLE);
            }
        }, 600);

    }

    // 填充拼音, 排序
    private void fillNameAndSort()
    {
        for (int i = 0; i < mCityList.size(); i++)
        {
            CityInfo mCityInfo = mCityList.get(i);
            if (DigitalUtil.isDigital(mCityInfo.getName()))
            {
                if (!letters.contains("#"))
                {
                    letters.add("#");
                }
                continue;
            }

            String pinyin = mCityInfo.getPinyin();
            String letter;
            if (!TextUtils.isEmpty(pinyin))
            {
                letter = pinyin.substring(0, 1).toUpperCase();

            }
            else
            {
                letter = mCityInfo.getName().substring(0, 1).toUpperCase();
            }
            if (!letters.contains(letter))
            {
                letters.add(letter);
            }
        }
        Collections.sort(mCityList);
        Collections.sort(letters);
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
