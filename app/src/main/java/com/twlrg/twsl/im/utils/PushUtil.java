package com.twlrg.twsl.im.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.twlrg.twsl.MyApplication;
import com.twlrg.twsl.R;
import com.twlrg.twsl.im.model.CustomMessage;
import com.twlrg.twsl.im.model.Message;
import com.twlrg.twsl.im.model.MessageFactory;
import com.twlrg.twsl.im.ui.ChatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 在线消息通知展示
 */
public class PushUtil implements Observer
{

    private static final String TAG = PushUtil.class.getSimpleName();

    private static int pushNum = 0;

    private final int pushId = 1;

    private static PushUtil instance = new PushUtil();

    private PushUtil()
    {
        MessageEvent.getInstance().addObserver(this);
    }

    public static PushUtil getInstance()
    {
        return instance;
    }

    String userNickName = "";

    private void PushNotify(final TIMMessage msg)
    {
        //系统消息，自己发的消息，程序在前台的时候不通知
        if (msg == null || Foreground.get().isForeground() ||
                (msg.getConversation().getType() != TIMConversationType.Group &&
                        msg.getConversation().getType() != TIMConversationType.C2C) ||
                msg.isSelf() ||
                msg.getRecvFlag() == TIMGroupReceiveMessageOpt.ReceiveNotNotify ||
                MessageFactory.getMessage(msg) instanceof CustomMessage) return;

        final String senderStr, contentStr;
        Message message = MessageFactory.getMessage(msg);
        if (message == null) return;
        senderStr = message.getSender();
        contentStr = message.getSummary();


        List<String> users = new ArrayList<>();
        users.add(senderStr);
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>()
        {
            @Override
            public void onError(int code, String desc)
            {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                Log.e("1111", "getUsersProfile failed: " + code + " desc");

                NotificationManager mNotificationManager = (NotificationManager) MyApplication.getContext().getSystemService(MyApplication.getContext()
                        .NOTIFICATION_SERVICE);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MyApplication.getContext());
                Intent notificationIntent = ChatActivity.getIntent(MyApplication.getContext(), msg.getSender(), TIMConversationType.C2C);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(MyApplication.getContext(), 0,
                        notificationIntent, 0);
                mBuilder.setContentTitle(senderStr)//设置通知栏标题
                        .setContentText(contentStr)
                        .setContentIntent(intent) //设置通知栏点击意图
                        //                .setNumber(++pushNum) //设置通知集合的数量
                        .setTicker(senderStr + ":" + contentStr) //通知首次出现在通知栏，带上升动画效果的
                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                        .setSmallIcon(R.drawable.ic_logo);//设置通知小ICON
                Notification notify = mBuilder.build();
                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                mNotificationManager.notify(pushId, notify);
            }

            @Override
            public void onSuccess(List<TIMUserProfile> result)
            {
                Log.e("222", "getUsersProfile succ");
                for (TIMUserProfile res : result)
                {
                    userNickName = res.getNickName();
                }
                NotificationManager mNotificationManager = (NotificationManager) MyApplication.getContext().getSystemService(MyApplication.getContext()
                        .NOTIFICATION_SERVICE);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MyApplication.getContext());
                Intent notificationIntent = ChatActivity.getIntent(MyApplication.getContext(), msg.getSender(), TIMConversationType.C2C);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(MyApplication.getContext(), 0,
                        notificationIntent, 0);
                mBuilder.setContentTitle(userNickName)//设置通知栏标题
                        .setContentText(contentStr)
                        .setContentIntent(intent) //设置通知栏点击意图
                        //                .setNumber(++pushNum) //设置通知集合的数量
                        .setTicker(userNickName + ":" + contentStr) //通知首次出现在通知栏，带上升动画效果的
                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                        .setSmallIcon(R.drawable.ic_logo);//设置通知小ICON
                Notification notify = mBuilder.build();
                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                mNotificationManager.notify(pushId, notify);
            }
        });


        Log.d(TAG, "recv msg " + contentStr);

    }


    private void showNotiy()
    {

    }

    public static void resetPushNum()
    {
        pushNum = 0;
    }

    public void reset()
    {
        NotificationManager notificationManager = (NotificationManager) MyApplication.getContext().getSystemService(MyApplication.getContext()
                .NOTIFICATION_SERVICE);
        notificationManager.cancel(pushId);
    }

    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data)
    {
        if (observable instanceof MessageEvent)
        {
            if (data instanceof TIMMessage)
            {
                TIMMessage msg = (TIMMessage) data;
                if (msg != null)
                {
                    PushNotify(msg);
                }
            }
        }
    }
}
