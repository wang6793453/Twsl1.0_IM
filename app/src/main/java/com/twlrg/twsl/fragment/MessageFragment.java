package com.twlrg.twsl.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;

import com.twlrg.twsl.MyApplication;
import com.twlrg.twsl.activity.BaseHandler;
import com.twlrg.twsl.activity.LoginActivity;
import com.twlrg.twsl.activity.MainActivity;
import com.twlrg.twsl.im.event.LoginEvent;
import com.twlrg.twsl.im.ui.ConversationFragment;

import de.greenrobot.event.EventBus;

/**
 * 作者：王先云 on 2018/4/12 20:19
 * 邮箱：wangxianyun1@163.com
 * 描述：消息
 */
public class MessageFragment extends ConversationFragment
{
    public static final int INIT_ONRESUME = 0X04;


    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(getActivity())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case INIT_ONRESUME:
                    if (((MainActivity) getActivity()).getTabIndex() == 1)
                    {
                        ((MainActivity) getActivity()).changeTabStatusColor(1);
                        if (!MyApplication.getInstance().isLogin())
                        {
                            LoginActivity.start(getActivity(), true);
                        }
                    }
                    break;


            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void onEventMainThread(LoginEvent event)
    {
        reloadData();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(INIT_ONRESUME, 200);
    }
}
