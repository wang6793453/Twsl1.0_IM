package com.twlrg.twsl.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.twlrg.twsl.utils.ConstantUtil;
import com.twlrg.twsl.utils.ToastUtil;

/**
 * 作者：王先云 on 2018/4/26 16:28
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler
{

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, ConstantUtil.WX_APPID, false);
        try
        {
            api.handleIntent(getIntent(), this);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req)
    {
    }

    @Override
    public void onResp(BaseResp resp)
    {
        int result = 0;
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
        {
            if (resp.errCode == 0)
            {
                sendBroadcast(new Intent().setAction("WXPAY_SUCCESS"));
            }
            else if (resp.errCode == -2)
            {
                ToastUtil.show(this, "取消支付");
            }
            else
            {
                ToastUtil.show(this, "支付失败");
            }
            finish();
        }
    }
}