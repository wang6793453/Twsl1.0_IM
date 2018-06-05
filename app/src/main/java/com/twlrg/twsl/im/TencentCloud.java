package com.twlrg.twsl.im;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2018/05/11 5.6.6 
 */

import android.app.Application;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.presentation.event.RefreshEvent;
import com.tencent.qcloud.tlslibrary.service.TLSService;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;
import com.twlrg.twsl.R;
import com.twlrg.twsl.im.event.LoginEvent;
import com.twlrg.twsl.im.model.UserInfo;
import com.twlrg.twsl.im.utils.Foreground;
import com.twlrg.twsl.utils.LogUtil;

import de.greenrobot.event.EventBus;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSHelper;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

public class TencentCloud
{


    public static final  String UID_PREFIX = "slbl_serve_";
    private static final String PASSWORD   = "slbl123456";

    private static final String TAG = "TencentCloud";

    public static void init(final Application applicationContext)
    {


        Foreground.init(applicationContext);

        if (MsfSdkUtils.isMainProcess(applicationContext))
        {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener()
            {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification)
                {
                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify)
                    {
                        //消息被设置为需要提醒
                        notification.doNotify(applicationContext, R.drawable.ic_launcher);
                    }
                }
            });
        }

    }


    public static void login(final String identifier, final LoginListener listener)
    {
        TLSHelper instance = TLSHelper.getInstance();


        if (!instance.needLogin(identifier))
        {
            listener.onSuccess(identifier);
            return;
        }

        LogUtil.d(TAG, identifier + " login TLS");

        instance.TLSPwdLogin(identifier, PASSWORD.getBytes(), new TLSPwdLoginListener()
        {
            @Override
            public void OnPwdLoginSuccess(TLSUserInfo tlsUserInfo)
            {
                LogUtil.d(TAG, "TLS Success");
                TLSService tlsService = TLSService.getInstance();
                UserInfo.getInstance().setUserSig(tlsService.getUserSig(tlsUserInfo.identifier));
                UserInfo.getInstance().setId(tlsUserInfo.identifier);
                listener.onSuccess(tlsUserInfo.identifier);
            }

            @Override
            public void OnPwdLoginReaskImgcodeSuccess(byte[] bytes)
            {
                LogUtil.d(TAG, "TLS OnPwdLoginReaskImgCodeSuccess");
                listener.onFail("OnPwdLoginReaskImgCodeSuccess", -1);
            }

            @Override
            public void OnPwdLoginNeedImgcode(byte[] bytes, TLSErrInfo tlsErrInfo)
            {
                onFailed(tlsErrInfo);
            }

            @Override
            public void OnPwdLoginFail(TLSErrInfo tlsErrInfo)
            {
                try
                {
                    RefreshEvent.getInstance().notify();
                    onFailed(tlsErrInfo);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    onFailed(tlsErrInfo);
                }


            }

            @Override
            public void OnPwdLoginTimeout(TLSErrInfo tlsErrInfo)
            {
                onFailed(tlsErrInfo);
            }

            void onFailed(TLSErrInfo tlsErrInfo)
            {
                String error = TencentCloud.toString(tlsErrInfo);

                LogUtil.d(TAG, "TLS failed:" + error);
                if (tlsErrInfo.ErrCode == 229)
                {
                    //账号未注册
                    LogUtil.d(TAG, identifier + " not register");
                    register(identifier, new LoginListener()
                    {
                        @Override
                        public void onSuccess(String identifier)
                        {
                            LogUtil.d(TAG, identifier + " register success ");
                            login(identifier, listener);
                        }

                        @Override
                        public void onFail(String msg, int code2)
                        {
                            LogUtil.d(TAG, " register fail " + msg);
                            listener.onFail(msg, code2);

                        }
                    });
                }
                else
                {
                    listener.onFail(error, tlsErrInfo.ErrCode);
                }
            }
        });


    }

    private static void register(final String identifier, final LoginListener listener)
    {
        LogUtil.d(TAG, "register " + identifier);
        TLSHelper instance = TLSHelper.getInstance();

        instance.TLSStrAccReg(identifier, PASSWORD, new TLSStrAccRegListener()
        {
            @Override
            public void OnStrAccRegSuccess(TLSUserInfo tlsUserInfo)
            {
                LogUtil.d(TAG, "OnStrAccRegSuccess:" + tlsUserInfo.identifier + "");
                listener.onSuccess(tlsUserInfo.identifier);
            }

            @Override
            public void OnStrAccRegFail(TLSErrInfo tlsErrInfo)
            {
                LogUtil.d(TAG, "OnStrAccRegFail:" + tlsErrInfo.Msg + " " + tlsErrInfo.ExtraMsg);
                listener.onFail(TencentCloud.toString(tlsErrInfo), tlsErrInfo.ErrCode);

            }

            @Override
            public void OnStrAccRegTimeout(TLSErrInfo tlsErrInfo)
            {
                LogUtil.d(TAG, "OnStrAccRegTimeout:" + tlsErrInfo.Msg + " " + tlsErrInfo.ExtraMsg);
                listener.onFail(TencentCloud.toString(tlsErrInfo), tlsErrInfo.ErrCode);
            }
        });


    }

    public static void IMLogin(final String identifier, final LoginListener listener)
    {
        LogUtil.d(TAG, "IMLogin");
        login(identifier, new LoginListener()
        {
            @Override
            public void onSuccess(final String identifier)
            {

                String userSig = TLSService.getInstance().getUserSig(identifier);
                LoginBusiness.loginIm(identifier, userSig, new TIMCallBack()
                {
                    @Override
                    public void onError(int i, String s)
                    {
                        LogUtil.d(TAG, "IM onError:" + s);
                        listener.onFail(s, i);
                    }

                    @Override
                    public void onSuccess()
                    {
                        LogUtil.d(TAG, "IM onSuccess:");
                        listener.onSuccess(identifier);
                        EventBus.getDefault().post(new LoginEvent(LoginEvent.STATUS_LOGIN));

                    }
                });
            }

            @Override
            public void onFail(String msg, int code2)
            {

            }
        });

    }

    public static void logout()
    {

        try
        {
            TlsBusiness.logout(TLSService.getInstance().getLastUserIdentifier());

            TIMManager.getInstance().logout(new TIMCallBack()
            {
                @Override
                public void onError(int i, String s)
                {
                    LogUtil.d(TAG, "onError:" + s);

                }

                @Override
                public void onSuccess()
                {
                    LogUtil.d(TAG, "onSuccess:");
                    EventBus.getDefault().post(new LoginEvent(LoginEvent.STATUS_LOGIN));
                }
            });
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static String toString(TLSErrInfo errInfo)
    {
        if (errInfo == null) return "null";
        return "code:" + errInfo.ErrCode + " Msg:" + errInfo.Msg + " extra:" + errInfo.ExtraMsg;
    }


    public interface LoginListener
    {
        void onSuccess(String identifier);

        void onFail(String msg, int code2);
    }


}
