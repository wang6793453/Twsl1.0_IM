package com.twlrg.twsl.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.twlrg.twsl.utils.WXShare;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler
{

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Logger.i("WXEntryActivity");
        WXShare share = new WXShare(this);
        api = share .getApi();
                //        .register()


        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try
        {
            api.handleIntent(getIntent(), this);
//            if (!api.handleIntent(getIntent(), this))
//            {
//                finish();
//            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        //Logger.i("onNewIntent");
        setIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
//        if (!api.handleIntent(intent, this))
//        {
//            finish();
//        }
    }

    @Override
    public void onReq(BaseReq baseReq)
    {
    }

    @Override
    public void onResp(BaseResp baseResp)
    {
        Intent intent = new Intent(WXShare.ACTION_SHARE_RESPONSE);
        intent.putExtra(WXShare.EXTRA_RESULT, new WXShare.Response(baseResp));
        sendBroadcast(intent);
        finish();
    }

}
