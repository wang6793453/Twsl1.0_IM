package com.twlrg.twsl.im.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;
import com.tencent.qcloud.presentation.presenter.ConversationPresenter;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.presentation.presenter.GroupManagerPresenter;
import com.tencent.qcloud.presentation.viewfeatures.ConversationView;
import com.tencent.qcloud.presentation.viewfeatures.FriendshipMessageView;
import com.tencent.qcloud.presentation.viewfeatures.GroupManageMessageView;
import com.twlrg.twsl.R;
import com.twlrg.twsl.activity.LoginActivity;
import com.twlrg.twsl.im.adapters.ConversationAdapter;
import com.twlrg.twsl.im.model.Conversation;
import com.twlrg.twsl.im.model.CustomMessage;
import com.twlrg.twsl.im.model.FriendshipConversation;
import com.twlrg.twsl.im.model.GroupManageConversation;
import com.twlrg.twsl.im.model.MessageFactory;
import com.twlrg.twsl.im.model.NomalConversation;
import com.twlrg.twsl.im.utils.PushUtil;
import com.twlrg.twsl.utils.APPUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 会话列表界面
 */
public class ConversationFragment extends Fragment implements ConversationView, FriendshipMessageView, GroupManageMessageView
{

    private final String TAG = "ConversationFragment";

    private View view;
    private View mTopView;
    private List<Conversation> conversationList = new LinkedList<>();
    private ConversationAdapter        adapter;
    private ListView                   listView;
    private ConversationPresenter      presenter;
    private FriendshipManagerPresenter friendshipManagerPresenter;
  //  private GroupManagerPresenter      groupManagerPresenter;
    private List<String>               groupList;
    private FriendshipConversation     friendshipConversation;
    private GroupManageConversation    groupManageConversation;


    public ConversationFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_conversation, container, false);
            listView = (ListView) view.findViewById(R.id.list);
            mTopView = view.findViewById(R.id.topView);
            mTopView.setVisibility(View.VISIBLE);
            mTopView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(getActivity())));
            adapter = new ConversationAdapter(getActivity(), R.layout.item_conversation, conversationList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    conversationList.get(position).navToDetail(getActivity());
                    if (conversationList.get(position) instanceof GroupManageConversation)
                    {
                      //  groupManagerPresenter.getGroupManageLastMessage();
                    }

                }
            });
            friendshipManagerPresenter = new FriendshipManagerPresenter(this);
          //  groupManagerPresenter = new GroupManagerPresenter(this);
            reloadData();
            registerForContextMenu(listView);
        }
        adapter.notifyDataSetChanged();
        return view;

    }

    @Override
    public void onResume()
    {
        super.onResume();
        refresh();
        PushUtil.getInstance().reset();
    }

    public void reloadData()
    {
        if (presenter == null)
        {
            presenter = new ConversationPresenter(this);
        }
        try
        {
            presenter.getConversation();
        }
        catch (Exception e)
        {
            LoginActivity.start(getActivity(),true);
        }


    }

    /**
     * 初始化界面或刷新界面
     *
     * @param conversationList
     */
    @Override
    public void initView(List<TIMConversation> conversationList)
    {
        this.conversationList.clear();
        groupList = new ArrayList<>();
        for (TIMConversation item : conversationList)
        {
            switch (item.getType())
            {
                case C2C:
                case Group:
                    this.conversationList.add(new NomalConversation(item));
                    groupList.add(item.getPeer());
                    break;
            }
        }
        friendshipManagerPresenter.getFriendshipLastMessage();
        //groupManagerPresenter.getGroupManageLastMessage();
    }

    /**
     * 更新最新消息显示
     *
     * @param message 最后一条消息
     */
    @Override
    public void updateMessage(TIMMessage message)
    {
        if (message == null)
        {
            adapter.notifyDataSetChanged();
            return;
        }
        if (message.getConversation().getType() == TIMConversationType.System)
        {
            //groupManagerPresenter.getGroupManageLastMessage();
            return;
        }
        if (MessageFactory.getMessage(message) instanceof CustomMessage) return;
        NomalConversation conversation = new NomalConversation(message.getConversation());
        Iterator<Conversation> iterator = conversationList.iterator();
        while (iterator.hasNext())
        {
            Conversation c = iterator.next();
            if (conversation.equals(c))
            {
                conversation = (NomalConversation) c;
                iterator.remove();
                break;
            }
        }
        conversation.setLastMessage(MessageFactory.getMessage(message));
        conversationList.add(conversation);
        Collections.sort(conversationList);
        refresh();
    }

    /**
     * 更新好友关系链消息
     */
    @Override
    public void updateFriendshipMessage()
    {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    /**
     * 删除会话
     *
     * @param identify
     */
    @Override
    public void removeConversation(String identify)
    {
        Iterator<Conversation> iterator = conversationList.iterator();
        while (iterator.hasNext())
        {
            Conversation conversation = iterator.next();
            if (conversation.getIdentify() != null && conversation.getIdentify().equals(identify))
            {
                iterator.remove();
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 更新群信息
     *
     * @param info
     */
    @Override
    public void updateGroupInfo(TIMGroupCacheInfo info)
    {
        for (Conversation conversation : conversationList)
        {
            if (conversation.getIdentify() != null && conversation.getIdentify().equals(info.getGroupInfo().getGroupId()))
            {
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 刷新
     */
    @Override
    public void refresh()
    {
        Collections.sort(conversationList);


        List<String> users = new ArrayList<>();
        for (int i = 0; i < conversationList.size(); i++)
        {
            users.add(conversationList.get(i).getIdentify());
        }


        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>()
        {
            @Override
            public void onError(int code, String desc)
            {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                Log.e("1111", "getUsersProfile failed: " + code + " desc");
            }

            @Override
            public void onSuccess(List<TIMUserProfile> result)
            {
                Log.e("222", "getUsersProfile succ");
                for (TIMUserProfile res : result)
                {

                    for (int i = 0; i < conversationList.size(); i++)
                    {
                        if (res.getIdentifier().equals(conversationList.get(i).getIdentify()))
                        {
                            conversationList.get(i).setFaceUrl(res.getFaceUrl());
                            conversationList.get(i).setName(res.getNickName());
                            conversationList.get(i).setNickName(res.getNickName());
                        }
                    }
                    Log.e("2222", "identifier: " + res.getIdentifier() + " nickName: " + res.getNickName()
                            + " remark: " + res.getRemark());
                }

                adapter.notifyDataSetChanged();
            }
        });


        //        if (getActivity() instanceof HomeActivity)
        //            ((HomeActivity) getActivity()).setMsgUnread(getTotalUnreadNum() == 0);
    }


    /**
     * 获取好友关系链管理系统最后一条消息的回调
     *
     * @param message     最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount)
    {
        if (friendshipConversation == null)
        {
            friendshipConversation = new FriendshipConversation(message);
            conversationList.add(friendshipConversation);
        }
        else
        {
            friendshipConversation.setLastMessage(message);
        }
        friendshipConversation.setUnreadCount(unreadCount);
        Collections.sort(conversationList);
        refresh();
    }

    /**
     * 获取好友关系链管理最后一条系统消息的回调
     *
     * @param message 消息列表
     */
    @Override
    public void onGetFriendshipMessage(List<TIMFriendFutureItem> message)
    {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    /**
     * 获取群管理最后一条系统消息的回调
     *
     * @param message     最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount)
    {
        if (groupManageConversation == null)
        {
            groupManageConversation = new GroupManageConversation(message);
            conversationList.add(groupManageConversation);
        }
        else
        {
            groupManageConversation.setLastMessage(message);
        }
        groupManageConversation.setUnreadCount(unreadCount);
        Collections.sort(conversationList);
        refresh();
    }

    /**
     * 获取群管理系统消息的回调
     *
     * @param message 分页的消息列表
     */
    @Override
    public void onGetGroupManageMessage(List<TIMGroupPendencyItem> message)
    {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Conversation conversation = conversationList.get(info.position);
        if (conversation instanceof NomalConversation)
        {
            menu.add(0, 1, Menu.NONE, getString(R.string.conversation_del));
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        NomalConversation conversation = (NomalConversation) conversationList.get(info.position);
        switch (item.getItemId())
        {
            case 1:
                if (conversation != null)
                {
                    if (presenter.delConversation(conversation.getType(), conversation.getIdentify()))
                    {
                        conversationList.remove(conversation);
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private long getTotalUnreadNum()
    {
        long num = 0;
        for (Conversation conversation : conversationList)
        {
            num += conversation.getUnreadNum();
        }
        return num;
    }


}
