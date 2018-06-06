package com.twlrg.twsl.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMManager;
import com.twlrg.twsl.MyApplication;
import com.twlrg.twsl.R;
import com.twlrg.twsl.fragment.HomeFragment;
import com.twlrg.twsl.fragment.MessageFragment;
import com.twlrg.twsl.fragment.OrderFragment;
import com.twlrg.twsl.fragment.OrderListFragment;
import com.twlrg.twsl.fragment.UserCenterFragment;
import com.twlrg.twsl.fragment.UserCenterFragment1;
import com.twlrg.twsl.utils.ConfigManager;
import com.twlrg.twsl.utils.DialogUtils;
import com.twlrg.twsl.utils.LogUtil;

import butterknife.BindView;

public class MainActivity extends BaseActivity
{

    @BindView(android.R.id.tabhost)
    FragmentTabHost fragmentTabHost;

    private String texts[]       = {"首页", "消息", "订单", "我的"};
    private int    imageButton[] = {
            R.drawable.ic_home_selector, R.drawable.ic_message_selector,
            R.drawable.ic_order_selector, R.drawable.ic_user_center_selector};


    private Class fragmentArray[] = {HomeFragment.class, MessageFragment.class, OrderListFragment.class, UserCenterFragment1.class};

    private final String USER_LOGOUT = "USER_LOGOUT";


    private MyBroadCastReceiver mMyBroadCastReceiver;

    @Override
    protected void initData()
    {
        mMyBroadCastReceiver = new MyBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(USER_LOGOUT);
        registerReceiver(mMyBroadCastReceiver, intentFilter);
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_main);
        setTranslucentStatus();

        TIMManager.getInstance().getUserConfig().setConnectionListener(new TIMConnListener()
        {
            @Override
            public void onConnected()
            {
                Log.e("TAG", "onConnected1111111111111111111111111111111");
            }

            @Override
            public void onDisconnected(int i, String s)
            {
                Log.e("TAG", "onDisconnected1111111111111111111111111111111111111111111111111111");
                ConfigManager.instance().setUserId("");
            }

            @Override
            public void onWifiNeedAuth(String s)
            {
                Log.e("TAG", "onWifiNeedAuth1111111111111111111111111111111111111111111111111");
            }
        });

    }

    @Override
    protected void initEvent()
    {
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
        {
            @Override
            public void onTabChanged(String tabId)
            {
                LogUtil.e("TAG", "tabId--->" + tabId);

                if (!"首页".equals(tabId))
                {
                    if (!MyApplication.getInstance().isLogin())
                    {
                        fragmentTabHost.setCurrentTab(0);
                        LoginActivity.start(MainActivity.this, true);
                    }
                }
            }
        });
    }

    @Override
    protected void initViewData()
    {
        fragmentTabHost.setup(this, getSupportFragmentManager(),
                R.id.main_layout);

        for (int i = 0; i < texts.length; i++)
        {
            TabHost.TabSpec spec = fragmentTabHost.newTabSpec(texts[i]).setIndicator(getView(i));

            fragmentTabHost.addTab(spec, fragmentArray[i], null);

            //设置背景(必须在addTab之后，由于需要子节点（底部菜单按钮）否则会出现空指针异常)
            // fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.main_tab_selector);
        }
        fragmentTabHost.getTabWidget().setDividerDrawable(R.color.transparent);
    }

    private View getView(int i)
    {
        //取得布局实例
        View view = View.inflate(MainActivity.this, R.layout.tabcontent, null);
        //取得布局对象
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView = (TextView) view.findViewById(R.id.text);

        //设置图标
        imageView.setImageResource(imageButton[i]);
        //设置标题
        textView.setText(texts[i]);
        return view;
    }


    public void changeTabStatusColor(int index)
    {
        if (index == 0)
        {
            setStatusBarTextDeep(false);
        }
        else
        {
            setStatusBarTextDeep(false);
        }
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (null != mMyBroadCastReceiver)
        {
            unregisterReceiver(mMyBroadCastReceiver);
        }

    }


    public int getTabIndex()
    {
        return fragmentTabHost.getCurrentTab();
    }

    class MyBroadCastReceiver extends BroadcastReceiver
    {
        private static final String TAG = "TestBroadCastReceiver";

        @Override
        public void onReceive(Context context, Intent intent)
        {

            if (USER_LOGOUT.contentEquals(intent.getAction()))
            {
                fragmentTabHost.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        fragmentTabHost.setCurrentTab(0);
                        changeTabStatusColor(0);
                    }
                }, 100);

            }
        }
    }


    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {

            DialogUtils.showToastDialog2Button(MainActivity.this, "是否退出商旅部落", new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    finish();
                }
            });

            return false;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }

    }

}
