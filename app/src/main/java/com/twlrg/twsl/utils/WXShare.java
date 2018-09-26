package com.twlrg.twsl.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.twlrg.twsl.R;
import com.twlrg.twsl.entity.ShareInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class WXShare
{
    public static final String APP_ID = "wx427ee617b358b917";
    public static final String ACTION_SHARE_RESPONSE = "action_wx_share_response";
    public static final String EXTRA_RESULT = "result";

    private final Context context;
    private final IWXAPI api;
    private OnResponseListener listener;
    private ResponseReceiver receiver;

    public WXShare(Context context)
    {
        api = WXAPIFactory.createWXAPI(context, APP_ID);
        this.context = context;
    }

    public WXShare register()
    {
        // 微信分享
        api.registerApp(APP_ID);
        receiver = new ResponseReceiver();
        IntentFilter filter = new IntentFilter(ACTION_SHARE_RESPONSE);
        context.registerReceiver(receiver, filter);
        return this;
    }

    public void unregister()
    {
        try
        {
            api.unregisterApp();
            context.unregisterReceiver(receiver);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public WXShare share(String text)
    {
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        //    msg.title = "Will be ignored";
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;

        boolean result = api.sendReq(req);
        //  Logger.i("text shared: " + result);
        return this;
    }

    public WXShare shareWebpage(Context context,ShareInfo shareInfo)
    {

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareInfo.getUrl();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareInfo.getTitle();
        msg.description = shareInfo.getContent();
        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_share_logo);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = shareInfo.getShareStyle();
        api.sendReq(req);

        boolean result = api.sendReq(req);
        //  Logger.i("text shared: " + result);
        return this;
    }


    private void  sharePic(final Context context, final ShareInfo shareInfo)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                try
                {
                    WXImageObject imgObj = new WXImageObject();
                    //                    设置图片的url
                    imgObj.imagePath = Urls.getImgUrl(shareInfo.getImg());
                    //                    第二步：创建WXMediaMeaasge对象，包装WXImageObject对象
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = shareInfo.getUrl();

                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.mediaObject = imgObj;
                    msg.title = shareInfo.getTitle();
                    msg.description = shareInfo.getContent();
                    //                    第三步：压缩图片
                    Bitmap bitmap = BitmapFactory.decodeStream(new URL(Urls.getImgUrl(shareInfo.getImg())).openStream());
                    // Bitmap thumBitmap = bitmap.createScaledBitmap(bitmap, 120, 150, true);
                    //                    释放资源
                    bitmap.recycle();
                    msg.thumbData = Util.bitmap2Bytes(bitmap, 32);


                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = msg;
                    req.scene = shareInfo.getShareStyle();
                    api.sendReq(req);

                    boolean result = api.sendReq(req);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public IWXAPI getApi()
    {
        return api;
    }

    public void setListener(OnResponseListener listener)
    {
        this.listener = listener;
    }

    private String buildTransaction(final String type)
    {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private class ResponseReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            Response response = intent.getParcelableExtra(EXTRA_RESULT);
            //            Logger.d("type: " + response.getType());
            //            Logger.d("errCode: " + response.errCode);
            String result;
            if (listener != null)
            {
                if (response.errCode == BaseResp.ErrCode.ERR_OK)
                {
                    listener.onSuccess();
                }
                else if (response.errCode == BaseResp.ErrCode.ERR_USER_CANCEL)
                {
                    listener.onCancel();
                }
                else
                {
                    switch (response.errCode)
                    {
                        case BaseResp.ErrCode.ERR_AUTH_DENIED:
                            result = "发送被拒绝";
                            break;
                        case BaseResp.ErrCode.ERR_UNSUPPORT:
                            result = "不支持错误";
                            break;
                        default:
                            result = "发送返回";
                            break;
                    }
                    listener.onFail(result);
                }
            }
        }
    }

    public static class Response extends BaseResp implements Parcelable
    {

        public int errCode;
        public String errStr;
        public String transaction;
        public String openId;

        private int type;
        private boolean checkResult;

        public Response(BaseResp baseResp)
        {
            errCode = baseResp.errCode;
            errStr = baseResp.errStr;
            transaction = baseResp.transaction;
            openId = baseResp.openId;
            type = baseResp.getType();
            checkResult = baseResp.checkArgs();
        }

        @Override
        public int getType()
        {
            return type;
        }

        @Override
        public boolean checkArgs()
        {
            return checkResult;
        }


        @Override
        public int describeContents()
        {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeInt(this.errCode);
            dest.writeString(this.errStr);
            dest.writeString(this.transaction);
            dest.writeString(this.openId);
            dest.writeInt(this.type);
            dest.writeByte(this.checkResult ? (byte) 1 : (byte) 0);
        }

        protected Response(Parcel in)
        {
            this.errCode = in.readInt();
            this.errStr = in.readString();
            this.transaction = in.readString();
            this.openId = in.readString();
            this.type = in.readInt();
            this.checkResult = in.readByte() != 0;
        }

        public static final Creator<Response> CREATOR = new Creator<Response>()
        {
            @Override
            public Response createFromParcel(Parcel source)
            {
                return new Response(source);
            }

            @Override
            public Response[] newArray(int size)
            {
                return new Response[size];
            }
        };
    }

    public interface OnResponseListener
    {
        void onSuccess();

        void onCancel();

        void onFail(String message);
    }
}